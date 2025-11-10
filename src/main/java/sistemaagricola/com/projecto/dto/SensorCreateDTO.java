package sistemaagricola.com.projecto.dto;

import jakarta.validation.constraints.*;
public record SensorCreateDTO(
        @NotNull Long dispositivoId,
        @NotBlank String tipo,
        @NotBlank String unidade
) {}