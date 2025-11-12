package sistemaagricola.com.projecto.dto;

import jakarta.validation.constraints.*;

public record CreateRegraDTO(
        @NotNull(message = "Parcela é obrigatória")
        Long parcelaId,
        
        @NotBlank(message = "Expressão é obrigatória")
        @Size(min = 5, max = 160, message = "Expressão deve ter entre 5 e 160 caracteres")
        String expressao,
        
        @NotBlank(message = "Severidade é obrigatória")
        @Pattern(regexp = "INFO|AVISO|CRITICO", message = "Severidade deve ser INFO, AVISO ou CRITICO")
        String severidade,
        
        @NotBlank(message = "Destino é obrigatório")
        @Pattern(regexp = "APP|SMS|EMAIL", message = "Destino deve ser APP, SMS ou EMAIL")
        String destino,
        
        @NotNull(message = "Estado activo é obrigatório")
        Boolean activo
) {}

