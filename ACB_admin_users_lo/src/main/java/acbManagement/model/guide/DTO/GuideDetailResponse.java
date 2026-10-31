package acbManagement.model.guide.DTO;

public record GuideDetailResponse(
        Long userId,
        String documentType,
        String documentNumber,
        String name,
        String address,
        String phone,
        String contractNumber,
        String email,
        String zone
) {
}
