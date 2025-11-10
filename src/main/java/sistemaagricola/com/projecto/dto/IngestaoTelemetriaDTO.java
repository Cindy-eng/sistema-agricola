package sistemaagricola.com.projecto.dto;

import jakarta.validation.constraints.*; import java.time.Instant;
public record IngestaoTelemetriaDTO(
        @NotBlank String deviceKey,
        @NotNull Long sensorId,
        @NotNull Instant ts,
        @NotNull Double valor
) {}
