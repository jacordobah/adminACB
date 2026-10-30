package acbManagement.model.route;

public enum ShiftCode {
    SHIFT_541("541"),
    SHIFT_542("542");

    private final String code;

    ShiftCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
