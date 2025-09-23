package com.fpt.etc.controller;

import com.fpt.etc.dto.payment.BookingPaymentDto;
import com.fpt.etc.entity.*;
import com.fpt.etc.repository.BookingRepository;
import com.fpt.etc.repository.MenuRepository;
import com.fpt.etc.repository.RoomRepository;
import com.fpt.etc.repository.ServiceRepository;
import com.fpt.etc.services.MomoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final MomoService momoService;
    private final BookingRepository bookingRepo;
    private final MenuRepository menuRepo;
    private final RoomRepository roomRepo;
    private final ServiceRepository serviceRepo;

    @PostMapping("/momo")
    public ResponseEntity<?> createMomoPayment(@RequestBody BookingPaymentDto dto) throws Exception {
        Booking booking = bookingRepo.findById(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Menu menu = menuRepo.findById(booking.getMenuId()).orElse(null);
        Room room = roomRepo.findById(booking.getRoomId()).orElse(null);
        List<ServiceEntity> services = serviceRepo.findAllById(booking.getServiceIds());

        BigDecimal menuTotal = (menu != null ? menu.getPrice() : BigDecimal.ZERO).multiply(BigDecimal.valueOf(booking.getPeople()));
        BigDecimal roomPrice = (room != null ? room.getPrice() : BigDecimal.ZERO);
        BigDecimal serviceTotal = services.stream().map(ServiceEntity::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = menuTotal.add(roomPrice).add(serviceTotal);
        BigDecimal amountToPay = "deposit".equalsIgnoreCase(booking.getPaymentMethod())
                ? totalAmount.multiply(BigDecimal.valueOf(0.3)).setScale(0, BigDecimal.ROUND_HALF_UP)
                : totalAmount;

        MomoCreatePaymentResponse momoResponse = momoService.createPayment(new OrderInfoModel() {{
            setFullName(booking.getName());
            setAmount(amountToPay.toPlainString());
            setOrderInfo("Thanh toán " + booking.getPaymentMethod() + " cho đơn " + booking.getOrderCode());
            setExtraData(String.valueOf(booking.getId()));
        }});

        return ResponseEntity.ok().body(
                new Object() {
                    public final String momoPayUrl = momoResponse.getPayUrl();
                    public final BigDecimal total = totalAmount;
                    public final BigDecimal pay = amountToPay;
                }
        );
    }

    @PostMapping("/momo/callback")
    public ResponseEntity<?> momoCallback(@RequestBody MomoNotifyModel payload) {
        if (!"0".equals(payload.getErrorCode())) {
            return ResponseEntity.ok().build();
        }

        Long bookingId = Long.parseLong(payload.getExtraData());
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        String newStatus = "deposit".equalsIgnoreCase(booking.getPaymentMethod()) ? "confirmed" : "paid";
        String paymentStatus = "deposit".equalsIgnoreCase(booking.getPaymentMethod()) ? "partial" : "paid";

        booking.setStatus(newStatus);
        booking.setPaymentStatus(paymentStatus);
        bookingRepo.save(booking);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/momo/test")
    public ResponseEntity<?> createMomoPaymentTest() throws Exception {
        // Fake data để test
        String bookingId = "999";  // fake booking id
        String bookingName = "Nguyen Van A";
        String bookingCode = "BKTEST001";
        String paymentMethod = "deposit"; // hoặc "full"

        BigDecimal menuTotal = BigDecimal.valueOf(1_000_000);
        BigDecimal roomPrice = BigDecimal.valueOf(500_000);
        BigDecimal serviceTotal = BigDecimal.valueOf(200_000);

        BigDecimal totalAmount = menuTotal.add(roomPrice).add(serviceTotal);
        BigDecimal amountToPay = "deposit".equalsIgnoreCase(paymentMethod)
                ? totalAmount.multiply(BigDecimal.valueOf(0.3)).setScale(0, BigDecimal.ROUND_HALF_UP)
                : totalAmount;

        // Gọi sang momoService như thật
        MomoCreatePaymentResponse momoResponse = momoService.createPayment(new OrderInfoModel() {{
            setFullName(bookingName);
            setAmount(amountToPay.toPlainString());
            setOrderInfo("Thanh toán " + paymentMethod + " cho đơn " + bookingCode);
            setExtraData(bookingId);
        }});

        return ResponseEntity.ok().body(
                new Object() {
                    public final String momoPayUrl = momoResponse.getPayUrl();
                    public final BigDecimal total = totalAmount;
                    public final BigDecimal pay = amountToPay;
                }
        );
    }

}

