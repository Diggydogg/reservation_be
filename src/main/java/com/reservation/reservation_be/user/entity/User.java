package com.reservation.reservation_be.user.entity;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="user")
@Getter
@Setter
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  UUID id;

    @Column(name = "password")
    private String password;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Builder
    public User(String userId,String password, String userName) {
        this.id = UUID.randomUUID();
        this.password = password;
        this.userId = userId;
        this.userName = userName;

    }

}
