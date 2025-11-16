package sistemaagricola.com.projecto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sistemaagricola.com.projecto.models.Parcela;
import sistemaagricola.com.projecto.service.ParcelaService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParcelaController.class)
class ParcelaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ParcelaService parcelaService;

    @Test
    void should_criar_when_validParcela() throws Exception {
        Parcela parcela = sistemaagricola.com.projecto.util.TestDataBuilder.parcela()
                .id(1L)
                .nome("Parcela Norte")
                .lat(-23.5505)
                .lon(-46.6333)
                .build();

        when(parcelaService.criar(any(Parcela.class))).thenReturn(parcela);

        mockMvc.perform(post("/api/parcelas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parcela)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Parcela Norte"));
    }

    @Test
    void should_obter_when_parcelaExists() throws Exception {
        Parcela parcela = sistemaagricola.com.projecto.util.TestDataBuilder.parcela()
                .id(1L)
                .nome("Parcela Norte")
                .build();

        when(parcelaService.obter(1L)).thenReturn(parcela);

        mockMvc.perform(get("/api/parcelas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Parcela Norte"));
    }

    @Test
    void should_listar_when_parcelasExist() throws Exception {
        Parcela parcela1 = sistemaagricola.com.projecto.util.TestDataBuilder.parcela()
                .id(1L)
                .nome("Parcela Norte")
                .build();
        Parcela parcela2 = sistemaagricola.com.projecto.util.TestDataBuilder.parcela()
                .id(2L)
                .nome("Parcela Sul")
                .build();

        List<Parcela> parcelas = Arrays.asList(parcela1, parcela2);
        when(parcelaService.listar()).thenReturn(parcelas);

        mockMvc.perform(get("/api/parcelas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("Parcela Norte"))
                .andExpect(jsonPath("$[1].nome").value("Parcela Sul"));
    }

    @Test
    void should_actualizar_when_validParcela() throws Exception {
        Parcela parcela = sistemaagricola.com.projecto.util.TestDataBuilder.parcela()
                .id(1L)
                .nome("Parcela Norte Atualizada")
                .build();

        when(parcelaService.actualizar(eq(1L), any(Parcela.class))).thenReturn(parcela);

        mockMvc.perform(put("/api/parcelas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parcela)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Parcela Norte Atualizada"));
    }

    @Test
    void should_remover_when_parcelaExists() throws Exception {
        mockMvc.perform(delete("/api/parcelas/1"))
                .andExpect(status().isOk());
    }
}

