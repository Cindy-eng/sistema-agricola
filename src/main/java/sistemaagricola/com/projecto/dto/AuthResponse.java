package sistemaagricola.com.projecto.dto;

public record AuthResponse(
    String token,
    String email,
    String nome,
    String role
) {}

