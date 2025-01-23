package com.reservation.reservation_be.booking.dto;

import com.reservation.reservation_be.booking.entity.Booking;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Builder
public class BookingResponseDTO {
    private UUID bookingId;
    private LocalTime bookingTime;
    private String BookingInfo;
    private LocalDate date;
    private String name;
    private String userId;
    private int noPeople;

    //entity -> DTO
    public static BookingResponseDTO from(Booking booking) {
        return BookingResponseDTO.builder()
                .bookingId(booking.getBookingId())
                .bookingTime(booking.getBookingTime())
                .BookingInfo(booking.getBookingInfo())
                .date(booking.getDate())
                .name(booking.getName())
                .userId(booking.getUserId())
                .noPeople(booking.getNoPeople())
                .build();
    }


}
