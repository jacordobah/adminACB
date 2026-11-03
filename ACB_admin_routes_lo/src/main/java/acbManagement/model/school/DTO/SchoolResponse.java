package acbManagement.model.school.DTO;

public record SchoolResponse(
        Long id,
        String name,
        String schoolCode,
        String phone,
        String address,
        String zone,
        Double latitude,
        Double longitude,
        Boolean active
) {
}
