package acbManagement.dto;

public record LoginResponse(String token, UserInfo user) {

    public record UserInfo(Long id, String role) {
    }
}