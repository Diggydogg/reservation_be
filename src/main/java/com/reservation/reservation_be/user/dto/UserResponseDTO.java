package com.reservation.reservation_be.user.dto;

import com.reservation.reservation_be.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserResponseDTO {
    private UUID id;
    private String userName;
    private String userId;
    private String password;

    //entity -> DTO
    public static UserResponseDTO from(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .password(user.getPassword())
                .userId(user.getUserId())
                .build();
    }
    // for verify  sub DTO
    @Getter
    @Builder
    public static class VerifyResponse{
        private boolean exists;
        private UUID id;
        private String userName;
        private String userId;
        private String password;
    }

}
