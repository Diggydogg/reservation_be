package com.reservation.reservation_be.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String userId;
    private String password;
}