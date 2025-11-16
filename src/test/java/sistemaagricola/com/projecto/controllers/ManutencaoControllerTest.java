package sistemaagricola.com.projecto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sistemaagricola.com.projecto.models.Manutencao;
import sistemaagricola.com.projecto.service.ManutencaoService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManutencaoController.class)
class ManutencaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ManutencaoService manutencaoService;

    @Test
    void should_abrir_when_validManutencao() throws Exception {
        Manutencao manutencao = sistemaagricola.com.projecto.util.TestDataBuilder.manutencao()
                .id(1L)
                .tipo("CALIBRACAO")
                .estado("ABERTA")
                .build();

        when(manutencaoService.abrir(any(Manutencao.class))).thenReturn(manutencao);

        mockMvc.perform(post("/api/manutencoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manutencao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("ABERTA"));
    }

    @Test
    void should_fechar_when_manutencaoExists() throws Exception {
        Manutencao manutencao = sistemaagricola.com.projecto.util.TestDataBuilder.manutencao()
                .id(1L)
                .estado("FECHADA")
                .build();

        when(manutencaoService.fechar(1L)).thenReturn(manutencao);

        mockMvc.perform(patch("/api/manutencoes/1/fechar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("FECHADA"));
    }

    @Test
    void should_listar_when_validParameters() throws Exception {
        Manutencao manutencao1 = sistemaagricola.com.projecto.util.TestDataBuilder.manutencao()
                .id(1L)
                .estado("ABERTA")
                .build();
        Manutencao manutencao2 = sistemaagricola.com.projecto.util.TestDataBuilder.manutencao()
                .id(2L)
                .estado("ABERTA")
                .build();

        List<Manutencao> manutencoes = Arrays.asList(manutencao1, manutencao2);
        when(manutencaoService.listarPorDispositivoEEstado(1L, "ABERTA")).thenReturn(manutencoes);

        mockMvc.perform(get("/api/manutencoes/por-dispositivo/1")
                        .param("estado", "ABERTA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].estado").value("ABERTA"))
                .andExpect(jsonPath("$[1].estado").value("ABERTA"));
    }
}

