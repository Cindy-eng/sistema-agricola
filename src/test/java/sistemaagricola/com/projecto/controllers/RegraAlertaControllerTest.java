package sistemaagricola.com.projecto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sistemaagricola.com.projecto.dto.CreateRegraDTO;
import sistemaagricola.com.projecto.models.RegraAlerta;
import sistemaagricola.com.projecto.service.RegraAlertaService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegraAlertaController.class)
class RegraAlertaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegraAlertaService regraAlertaService;

    @Test
    void should_criar_when_validDTO() throws Exception {
        CreateRegraDTO dto = new CreateRegraDTO(
                1L,
                "tipo==HUM_SOLO && valor<18 por 120m",
                "AVISO",
                "APP",
                true
        );

        RegraAlerta regra = sistemaagricola.com.projecto.util.TestDataBuilder.regraAlerta()
                .id(1L)
                .build();

        when(regraAlertaService.criar(any(CreateRegraDTO.class))).thenReturn(regra);

        mockMvc.perform(post("/api/regras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void should_activar_when_validParameters() throws Exception {
        RegraAlerta regra = sistemaagricola.com.projecto.util.TestDataBuilder.regraAlerta()
                .id(1L)
                .activo(false)
                .build();

        when(regraAlertaService.activar(1L, false)).thenReturn(regra);

        mockMvc.perform(patch("/api/regras/1/activo/false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    void should_listarActivas_when_parcelaExists() throws Exception {
        RegraAlerta regra1 = sistemaagricola.com.projecto.util.TestDataBuilder.regraAlerta()
                .id(1L)
                .build();
        RegraAlerta regra2 = sistemaagricola.com.projecto.util.TestDataBuilder.regraAlerta()
                .id(2L)
                .build();

        List<RegraAlerta> regras = Arrays.asList(regra1, regra2);
        when(regraAlertaService.listarActivasPorParcela(1L)).thenReturn(regras);

        mockMvc.perform(get("/api/regras/activas/por-parcela/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void should_remover_when_regraExists() throws Exception {
        mockMvc.perform(delete("/api/regras/1"))
                .andExpect(status().isOk());
    }
}

