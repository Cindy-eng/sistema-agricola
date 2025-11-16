package sistemaagricola.com.projecto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.Instant;

@Schema(description = "Dados para ingestão de telemetria de dispositivos IoT")
public record IngestaoTelemetriaDTO(
        @Schema(description = "Chave única do dispositivo", example = "DEV-001", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String deviceKey,
        
        @Schema(description = "ID do sensor que gerou a leitura", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Long sensorId,
        
        @Schema(description = "Timestamp da leitura (ISO 8601)", example = "2024-01-15T10:30:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Instant ts,
        
        @Schema(description = "Valor da leitura do sensor", example = "25.5", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Double valor
) {}
