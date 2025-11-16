package sistemaagricola.com.projecto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
@Schema(description = "Dados para criação de sensor")
public record SensorCreateDTO(
        @Schema(description = "ID do dispositivo ao qual o sensor pertence", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Long dispositivoId,
        
        @Schema(description = "Tipo do sensor (ex: HUM_SOLO, TEMP_AR)", example = "HUM_SOLO", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String tipo,
        
        @Schema(description = "Unidade de medida do sensor (ex: %, °C)", example = "%", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String unidade
) {}