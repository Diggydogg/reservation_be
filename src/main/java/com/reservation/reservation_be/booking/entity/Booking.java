package com.reservation.reservation_be.booking.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "booking")
@Getter
@Setter
@NoArgsConstructor
public class Booking {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID bookingId;
    @Column(name = "booking_time", columnDefinition = "DATETIME")
    private LocalTime bookingTime;

    @Column(name =  "booking_info")
    private String bookingInfo;

    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

    @Column(name = "name")
    private String name;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "no_people")
    private int noPeople;

    @Builder
    public Booking(LocalTime bookingTime, String bookingInfo, LocalDate date, String name, String userId, int noPeople) {
        this.bookingId = UUID.randomUUID();
        this.bookingTime = bookingTime;
        this.bookingInfo = bookingInfo;
        this.date = date;
        this.name = name;
        this.userId = userId;
        this.noPeople = noPeople;
    }

}
