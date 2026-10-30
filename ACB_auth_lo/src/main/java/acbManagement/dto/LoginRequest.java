package acbManagement.dto;

import lombok.NonNull;

public record LoginRequest(
        @NonNull
        String username,
        @NonNull
        String password) {
}