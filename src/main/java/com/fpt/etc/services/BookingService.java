package com.fpt.etc.services;

import com.fpt.etc.dto.request.CancelBookingDto;
import com.fpt.etc.dto.request.CreateBookingDto;
import com.fpt.etc.dto.request.UpdateBookingStatusDto;
import com.fpt.etc.entity.Booking;
import com.fpt.etc.helpers.EmailTemplateHelper;
import com.fpt.etc.repository.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EmailService emailService;

    public List<Booking> getAll(String status) {
        return (status != null)
                ? bookingRepository.findByStatusOrderByCreatedAtDesc(status)
                : bookingRepository.findAll();
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
        String emailBody = EmailTemplateHelper.generateBookingConfirmationEmail(
                saved, null, null, null, null,
                Collections.emptyList(), Collections.emptyList()
        );

        emailService.sendEmail(
                saved.getEmail(),
                "[CaterEase] Xác nhận đơn đặt tiệc - " + saved.getOrderCode(),
                emailBody
        );

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
        String emailBody = EmailTemplateHelper.generateBookingStatusUpdateEmail(
                booking, dto.getStatus(), null, null, null, null,
                Collections.emptyList(), Collections.emptyList()
        );

        emailService.sendEmail(
                booking.getEmail(),
                "[CaterEase] Cập nhật trạng thái đơn hàng - " + booking.getOrderCode(),
                emailBody
        );

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

