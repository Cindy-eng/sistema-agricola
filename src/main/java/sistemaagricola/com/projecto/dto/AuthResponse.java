package sistemaagricola.com.projecto.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação contendo token JWT e informações do usuário")
public record AuthResponse(
    @Schema(description = "Token JWT para autenticação em requisições subsequentes", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token,
    
    @Schema(description = "Email do usuário autenticado", example = "joao@example.com")
    String email,
    
    @Schema(description = "Nome do usuário autenticado", example = "João Silva")
    String nome,
    
    @Schema(description = "Papel do usuário (USER ou ADMIN)", example = "USER")
    String role
) {}

