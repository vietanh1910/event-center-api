package com.fpt.etc.services;

import com.fpt.etc.dto.booking.BookingResponse;
import com.fpt.etc.dto.booking.CancelBookingDto;
import com.fpt.etc.dto.booking.CreateBookingDto;
import com.fpt.etc.dto.booking.UpdateBookingStatusDto;
import com.fpt.etc.dto.event.EventResponse;
import com.fpt.etc.dto.event.EventResponseBasic;
import com.fpt.etc.dto.menu.MenuResponse;
import com.fpt.etc.dto.room.RoomResponse;
import com.fpt.etc.dto.service.ServiceResponse;
import com.fpt.etc.dto.venue.VenueResponse;
import com.fpt.etc.dto.venue.VenueResponseBasic;
import com.fpt.etc.entity.Booking;
import com.fpt.etc.helpers.EmailTemplateHelper;
import com.fpt.etc.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EmailService emailService;
    private final MenuRepository menuRepository;
    private final RoomRepository roomRepository;
    private final VenueRepository venueRepository;
    private final ServiceRepository serviceRepository;
    private final EventRepository eventRepository;

    public List<BookingResponse> getAll(String status) {
        List<Booking> bookings = (status != null)
                ? bookingRepository.findByStatusOrderByCreatedAtDesc(status)
                : bookingRepository.findAll();

        return bookings.stream().map(this::mapToResponse).toList();
    }

    private BookingResponse mapToResponse(Booking booking) {
        MenuResponse menu = menuRepository.findById(booking.getMenuId())
                .map(m -> new MenuResponse(m.getId(), m.getName(), m.getPrice()))
                .orElse(null);

        RoomResponse room = (booking.getRoomId() != null)
                ? roomRepository.findById(booking.getRoomId())
                .map(r -> new RoomResponse(r.getId(), r.getName(), r.getPrice()))
                .orElse(null)
                : null;

        VenueResponseBasic venue = (booking.getVenueId() != null)
                ? venueRepository.findById(booking.getVenueId())
                .map(v -> new VenueResponseBasic(v.getId(), v.getName()))
                .orElse(null)
                : null;

        List<ServiceResponse> services = booking.getServiceIds() != null
                ? booking.getServiceIds().stream()
                .map(id -> serviceRepository.findById(id)
                        .map(s -> new ServiceResponse(s.getId(), s.getName(), s.getPrice()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList()
                : List.of();

        EventResponseBasic event = (booking.getEventId() != null)
                ? eventRepository.findById(booking.getEventId())
                .map(v -> new EventResponseBasic(v.getId(), v.getName()))
                .orElse(null)
                : null;

        return BookingResponse.builder()
                .id(booking.getId())
                .orderCode(booking.getOrderCode())
                .name(booking.getName())
                .email(booking.getEmail())
                .phone(booking.getPhone())
                .eventDate(booking.getEventDate())
                .eventTime(booking.getEventTime())
                .people(booking.getPeople())
                .address(booking.getAddress())
                .paymentMethod(booking.getPaymentMethod())
                .status(booking.getStatus())
                .paymentStatus(booking.getPaymentStatus())
                .createdAt(booking.getCreatedAt())
                .cancelledAt(booking.getCancelledAt())
                .cancelReason(booking.getCancelReason())
                .cancelledBy(booking.getCancelledBy())
                .menu(menu)
                .room(room)
                .venue(venue)
                .services(services)
                .event(event)
                .build();
    }


    public Booking getById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Transactional
    public Booking create(CreateBookingDto dto) {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long countToday = bookingRepository.count();
        String orderCode = String.format("CE-%s-%04d", today, countToday + 1);

        Booking booking = Booking.builder()
                .userId(dto.getUserId())
                .orderCode(orderCode)
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .eventDate(dto.getEventDate())
                .eventTime(dto.getEventTime())
                .eventId(dto.getEventId())
                .people(dto.getPeople())
                .address(dto.getAddress())
                .paymentMethod(dto.getPaymentMethod())
                .roomId(dto.getRoomId())
                .menuId(dto.getMenuId())
                .venueId(dto.getVenueId())
                .serviceIds(dto.getServiceIds() != null ? dto.getServiceIds() : Collections.emptyList())
                .notes(dto.getNotes())
                .status("pending")
                .createdAt(LocalDateTime.now())
                .build();

        Booking saved = bookingRepository.save(booking);

        // 🔹 Gửi email xác nhận
//        String emailBody = EmailTemplateHelper.generateBookingConfirmationEmail(
//                saved, null, null, null, null,
//                Collections.emptyList(), Collections.emptyList()
//        );
//
//        emailService.sendEmail(
//                saved.getEmail(),
//                "[CaterEase] Xác nhận đơn đặt tiệc - " + saved.getOrderCode(),
//                emailBody
//        );

        return saved;
    }

    public List<Booking> getByUserId(String userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public Booking cancel(Long id, CancelBookingDto dto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if ("cancelled".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Already cancelled");
        }

        booking.setStatus("cancelled");
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancelReason(dto.getReason());
        booking.setCancelledBy("user"); // TODO: check role từ SecurityContext
        bookingRepository.save(booking);

        // 🔹 Gửi email hủy
        String emailBody = EmailTemplateHelper.generateBookingStatusUpdateEmail(
                booking, "cancelled", null, null, null, null,
                Collections.emptyList(), Collections.emptyList()
        );

        emailService.sendEmail(
                booking.getEmail(),
                "[CaterEase] Đơn hàng đã bị hủy - " + booking.getOrderCode(),
                emailBody
        );

        return booking;
    }

    @Transactional
    public Booking updateStatus(Long id, UpdateBookingStatusDto dto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (dto.getStatus().equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Status already set");
        }

        booking.setStatus(dto.getStatus());
        bookingRepository.save(booking);

        // 🔹 Gửi email cập nhật trạng thái
//        String emailBody = EmailTemplateHelper.generateBookingStatusUpdateEmail(
//                booking, dto.getStatus(), null, null, null, null,
//                Collections.emptyList(), Collections.emptyList()
//        );
//
//        emailService.sendEmail(
//                booking.getEmail(),
//                "[CaterEase] Cập nhật trạng thái đơn hàng - " + booking.getOrderCode(),
//                emailBody
//        );

        return booking;
    }

    @Transactional
    public void delete(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found");
        }
        bookingRepository.deleteById(id);
    }
}

