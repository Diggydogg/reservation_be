package com.reservation.reservation_be.booking.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BookingRequestDTO {

    @JsonFormat(pattern = "HH:mm")
    private LocalTime bookingTime;

    private String  bookingInfo;
    private String userId;
    private LocalDate date;
    private String name;
    private int noPeople;
}
