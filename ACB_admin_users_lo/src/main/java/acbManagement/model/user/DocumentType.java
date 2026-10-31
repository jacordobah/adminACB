package acbManagement.model.user;

public enum DocumentType {
    CC("C.C"), CE("C.E"), NUIP("NUIP"), PPT("PPT"), PPE("PPE");

    private final String value;

    DocumentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
