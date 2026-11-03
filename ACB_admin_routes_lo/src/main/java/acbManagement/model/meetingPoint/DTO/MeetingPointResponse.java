package acbManagement.model.meetingPoint.DTO;

public record MeetingPointResponse(
        Long meetingPointId,
        String meetingPointCode,
        String address,
        Integer distance,
        Double latitude,
        Double longitude,
        Long routeId,
        String routeCode
) {
}
