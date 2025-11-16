package sistemaagricola.com.projecto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sistemaagricola.com.projecto.dto.IngestaoTelemetriaDTO;
import sistemaagricola.com.projecto.models.Telemetria;
import sistemaagricola.com.projecto.repositories.TelemetriaRepository;
import sistemaagricola.com.projecto.service.TelemetriaService;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TelemetriaController.class)
class TelemetriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TelemetriaService telemetriaService;

    @MockBean
    private TelemetriaRepository telemetriaRepository;

    @Test
    void should_ingerir_when_validDTO() throws Exception {
        IngestaoTelemetriaDTO dto = new IngestaoTelemetriaDTO(
                "DEV-001",
                1L,
                Instant.now(),
                25.5
        );

        Telemetria telemetria = sistemaagricola.com.projecto.util.TestDataBuilder.telemetria()
                .id(1L)
                .valor(25.5)
                .build();

        when(telemetriaService.ingerir(any(IngestaoTelemetriaDTO.class))).thenReturn(telemetria);

        mockMvc.perform(post("/api/telemetria/ingestao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.valor").value(25.5));
    }

    @Test
    void should_janela_when_validParameters() throws Exception {
        Instant ini = Instant.now().minusSeconds(3600);
        Instant fim = Instant.now();

        Telemetria telemetria = sistemaagricola.com.projecto.util.TestDataBuilder.telemetria()
                .id(1L)
                .build();

        List<Telemetria> telemetrias = Arrays.asList(telemetria);
        when(telemetriaService.janela(eq(1L), any(Instant.class), any(Instant.class)))
                .thenReturn(telemetrias);

        mockMvc.perform(get("/api/telemetria/janela")
                        .param("sensorId", "1")
                        .param("ini", ini.toString())
                        .param("fim", fim.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void should_ultima_when_sensorExists() throws Exception {
        Telemetria telemetria = sistemaagricola.com.projecto.util.TestDataBuilder.telemetria()
                .id(1L)
                .build();

        when(telemetriaService.ultima(1L)).thenReturn(Optional.of(telemetria));

        mockMvc.perform(get("/api/telemetria/ultima")
                        .param("sensorId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void should_purgar_when_validParameters() throws Exception {
        Instant antesDe = Instant.now().minusSeconds(86400);

        when(telemetriaService.purgarAntes(eq(1L), any(Instant.class))).thenReturn(5L);

        mockMvc.perform(delete("/api/telemetria/purgar")
                        .param("sensorId", "1")
                        .param("antesDe", antesDe.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}

