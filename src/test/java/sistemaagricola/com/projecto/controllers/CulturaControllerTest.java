package sistemaagricola.com.projecto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sistemaagricola.com.projecto.models.Cultura;
import sistemaagricola.com.projecto.service.CulturaService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CulturaController.class)
class CulturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CulturaService culturaService;

    @Test
    void should_criar_when_validCultura() throws Exception {
        Cultura cultura = sistemaagricola.com.projecto.util.TestDataBuilder.cultura()
                .id(1L)
                .nome("Milho")
                .build();

        when(culturaService.criar(any(Cultura.class))).thenReturn(cultura);

        mockMvc.perform(post("/api/culturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cultura)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Milho"));
    }

    @Test
    void should_obter_when_culturaExists() throws Exception {
        Cultura cultura = sistemaagricola.com.projecto.util.TestDataBuilder.cultura()
                .id(1L)
                .nome("Milho")
                .build();

        when(culturaService.obter(1L)).thenReturn(cultura);

        mockMvc.perform(get("/api/culturas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Milho"));
    }

    @Test
    void should_listar_when_culturasExist() throws Exception {
        Cultura cultura1 = sistemaagricola.com.projecto.util.TestDataBuilder.cultura()
                .id(1L)
                .nome("Milho")
                .build();
        Cultura cultura2 = sistemaagricola.com.projecto.util.TestDataBuilder.cultura()
                .id(2L)
                .nome("Soja")
                .build();

        List<Cultura> culturas = Arrays.asList(cultura1, cultura2);
        when(culturaService.listar()).thenReturn(culturas);

        mockMvc.perform(get("/api/culturas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("Milho"))
                .andExpect(jsonPath("$[1].nome").value("Soja"));
    }

    @Test
    void should_actualizar_when_validCultura() throws Exception {
        Cultura cultura = sistemaagricola.com.projecto.util.TestDataBuilder.cultura()
                .id(1L)
                .nome("Milho Atualizado")
                .build();

        when(culturaService.actualizar(eq(1L), any(Cultura.class))).thenReturn(cultura);

        mockMvc.perform(put("/api/culturas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cultura)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Milho Atualizado"));
    }

    @Test
    void should_remover_when_culturaExists() throws Exception {
        mockMvc.perform(delete("/api/culturas/1"))
                .andExpect(status().isOk());
    }
}

