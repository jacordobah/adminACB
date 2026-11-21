package acbManagement.dto;

public record LoginResponseCreateUpdate(
        Long guideId,
        String role,
        boolean active
) {
}
