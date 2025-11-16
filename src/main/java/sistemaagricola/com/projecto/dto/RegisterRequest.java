package sistemaagricola.com.projecto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para registro de novo usuário")
public record RegisterRequest(
    @Schema(description = "Nome completo do usuário", example = "João Silva", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 3, maxLength = 100)
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    String nome,
    
    @Schema(description = "Email do usuário", example = "joao@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    String email,
    
    @Schema(description = "Senha do usuário (mínimo 6 caracteres)", example = "senha123", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 6)
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    String password
) {}

