package acbManagement.model.user;

public enum Role {
    GUIDE("Guía Escolar"),
    LEADER("Líder de Zona"),
    MANAGER("Gerente Operativo"),
    STUDENT("Estudiante"),
    GUARDIAN("Acudiente");

    private final String spanishName;

    Role(String spanishName) {
        this.spanishName = spanishName;
    }

    public String getSpanishName() {
        return spanishName;
    }

    public static Role fromSpanishName(String text) {
        for (Role role : Role.values()) {
            if (role.spanishName.equalsIgnoreCase(text.trim())) {
                return role;
            }
        }
        throw new IllegalArgumentException("El rol '" + text + "' no es un perfil válido para el Distrito de Bogotá.");
    }
}
