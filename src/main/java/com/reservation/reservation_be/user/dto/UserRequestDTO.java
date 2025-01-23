package com.reservation.reservation_be.user.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDTO {
    private String userName;
    private String password;
    private String userId;
}
