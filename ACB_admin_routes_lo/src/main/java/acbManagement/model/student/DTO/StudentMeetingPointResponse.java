package acbManagement.model.student.DTO;

public record StudentMeetingPointResponse(
        String shiftCode,
        Long meetingPointId,
        String meetingPointCode,
        String address
) {
}
