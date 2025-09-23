package com.fpt.etc.dto.booking;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class CreateBookingDto {

    @NotNull
    private String userId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "EventDate is required")
    private String eventDate;

    @NotBlank(message = "EventTime is required")
    private String eventTime;

    @NotNull(message = "EventId is required")
    private String eventId;

    @Min(value = 1, message = "People must be greater than 0")
    private int people;

    private String address;

    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "^(full|deposit)$", message = "PaymentMethod must be 'full' or 'deposit'")
    private String paymentMethod;

    private Long roomId;

    @NotNull(message = "MenuId is required")
    private Long menuId;

    private Long venueId;

    private List<Long> serviceIds;
    private String notes;
}

