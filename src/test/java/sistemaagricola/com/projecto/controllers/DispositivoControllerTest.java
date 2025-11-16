package sistemaagricola.com.projecto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sistemaagricola.com.projecto.models.Dispositivo;
import sistemaagricola.com.projecto.service.DispositivoService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DispositivoController.class)
class DispositivoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DispositivoService dispositivoService;

    @Test
    void should_criar_when_validDispositivo() throws Exception {
        Dispositivo dispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .id(1L)
                .deviceKey("DEV-001")
                .build();

        when(dispositivoService.criar(any(Dispositivo.class))).thenReturn(dispositivo);

        mockMvc.perform(post("/api/dispositivos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dispositivo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.deviceKey").value("DEV-001"));
    }

    @Test
    void should_obter_when_dispositivoExists() throws Exception {
        Dispositivo dispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .id(1L)
                .deviceKey("DEV-001")
                .build();

        when(dispositivoService.obter(1L)).thenReturn(dispositivo);

        mockMvc.perform(get("/api/dispositivos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.deviceKey").value("DEV-001"));
    }

    @Test
    void should_listarTodos_when_dispositivosExist() throws Exception {
        Dispositivo dispositivo1 = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .id(1L)
                .deviceKey("DEV-001")
                .build();
        Dispositivo dispositivo2 = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .id(2L)
                .deviceKey("DEV-002")
                .build();

        List<Dispositivo> dispositivos = Arrays.asList(dispositivo1, dispositivo2);
        when(dispositivoService.listarTodos()).thenReturn(dispositivos);

        mockMvc.perform(get("/api/dispositivos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].deviceKey").value("DEV-001"))
                .andExpect(jsonPath("$[1].deviceKey").value("DEV-002"));
    }

    @Test
    void should_listarPorParcela_when_parcelaExists() throws Exception {
        Dispositivo dispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .id(1L)
                .deviceKey("DEV-001")
                .build();

        Page<Dispositivo> page = new PageImpl<>(Arrays.asList(dispositivo));
        when(dispositivoService.listarPorParcela(eq(1L), any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/dispositivos/por-parcela/1")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].deviceKey").value("DEV-001"));
    }

    @Test
    void should_alterarEstado_when_validEstado() throws Exception {
        Dispositivo dispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .id(1L)
                .estado(Dispositivo.Estado.INACTIVO)
                .build();

        when(dispositivoService.alterarEstado(1L, Dispositivo.Estado.INACTIVO)).thenReturn(dispositivo);

        mockMvc.perform(patch("/api/dispositivos/1/estado/INACTIVO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("INACTIVO"));
    }
}

