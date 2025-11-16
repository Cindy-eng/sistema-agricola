package sistemaagricola.com.projecto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sistemaagricola.com.projecto.models.EventoIot;
import sistemaagricola.com.projecto.service.EventoIotService;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventoIotController.class)
class EventoIotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventoIotService eventoIotService;

    @Test
    void should_registar_when_validEvento() throws Exception {
        EventoIot evento = sistemaagricola.com.projecto.util.TestDataBuilder.eventoIot()
                .id(1L)
                .tipo("ONLINE")
                .build();

        when(eventoIotService.registar(any(EventoIot.class))).thenReturn(evento);

        mockMvc.perform(post("/api/eventos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("ONLINE"));
    }

    @Test
    void should_listar_when_validParameters() throws Exception {
        Instant ini = Instant.now().minusSeconds(3600);
        Instant fim = Instant.now();

        EventoIot evento1 = sistemaagricola.com.projecto.util.TestDataBuilder.eventoIot()
                .id(1L)
                .tipo("ONLINE")
                .build();
        EventoIot evento2 = sistemaagricola.com.projecto.util.TestDataBuilder.eventoIot()
                .id(2L)
                .tipo("OFFLINE")
                .build();

        List<EventoIot> eventos = Arrays.asList(evento1, evento2);
        when(eventoIotService.listarPorDispositivoEPeriodo(eq(1L), any(Instant.class), any(Instant.class)))
                .thenReturn(eventos);

        mockMvc.perform(get("/api/eventos/por-dispositivo/1")
                        .param("ini", ini.toString())
                        .param("fim", fim.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].tipo").value("ONLINE"))
                .andExpect(jsonPath("$[1].tipo").value("OFFLINE"));
    }

    @Test
    void should_purgar_when_validParameters() throws Exception {
        Instant antesDe = Instant.now().minusSeconds(86400);

        when(eventoIotService.purgarAntes(eq(1L), any(Instant.class))).thenReturn(10L);

        mockMvc.perform(delete("/api/eventos/purgar/1")
                        .param("antesDe", antesDe.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }
}

