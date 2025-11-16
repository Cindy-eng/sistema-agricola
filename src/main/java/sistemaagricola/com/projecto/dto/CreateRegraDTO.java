package sistemaagricola.com.projecto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para criação de regra de alerta")
public record CreateRegraDTO(
        @Schema(description = "ID da parcela associada à regra", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Parcela é obrigatória")
        Long parcelaId,
        
        @Schema(description = "Expressão da regra de alerta", example = "tipo==HUM_SOLO && valor<18 por 120m", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 5, maxLength = 160)
        @NotBlank(message = "Expressão é obrigatória")
        @Size(min = 5, max = 160, message = "Expressão deve ter entre 5 e 160 caracteres")
        String expressao,
        
        @Schema(description = "Severidade do alerta", example = "AVISO", allowableValues = {"INFO", "AVISO", "CRITICO"}, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Severidade é obrigatória")
        @Pattern(regexp = "INFO|AVISO|CRITICO", message = "Severidade deve ser INFO, AVISO ou CRITICO")
        String severidade,
        
        @Schema(description = "Destino do alerta", example = "APP", allowableValues = {"APP", "SMS", "EMAIL"}, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Destino é obrigatório")
        @Pattern(regexp = "APP|SMS|EMAIL", message = "Destino deve ser APP, SMS ou EMAIL")
        String destino,
        
        @Schema(description = "Indica se a regra está ativa", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Estado activo é obrigatório")
        Boolean activo
) {}

