package sistemaagricola.com.projecto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sistemaagricola.com.projecto.dto.SensorCreateDTO;
import sistemaagricola.com.projecto.models.Dispositivo;
import sistemaagricola.com.projecto.models.Sensor;
import sistemaagricola.com.projecto.repositories.DispositivoRepository;
import sistemaagricola.com.projecto.service.SensorService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SensorController.class)
class SensorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SensorService sensorService;

    @MockBean
    private DispositivoRepository dispositivoRepository;

    @Test
    void should_criar_when_validDTO() throws Exception {
        Dispositivo dispositivo = sistemaagricola.com.projecto.util.TestDataBuilder.dispositivo()
                .id(1L)
                .build();

        Sensor sensor = sistemaagricola.com.projecto.util.TestDataBuilder.sensor()
                .id(1L)
                .tipo("HUM_SOLO")
                .unidade("%")
                .dispositivo(dispositivo)
                .build();

        SensorCreateDTO dto = new SensorCreateDTO(1L, "HUM_SOLO", "%");

        when(dispositivoRepository.findById(1L)).thenReturn(Optional.of(dispositivo));
        when(sensorService.criar(any(Sensor.class))).thenReturn(sensor);

        mockMvc.perform(post("/api/sensores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("HUM_SOLO"))
                .andExpect(jsonPath("$.unidade").value("%"));
    }

    @Test
    void should_obter_when_sensorExists() throws Exception {
        Sensor sensor = sistemaagricola.com.projecto.util.TestDataBuilder.sensor()
                .id(1L)
                .tipo("HUM_SOLO")
                .build();

        when(sensorService.obter(1L)).thenReturn(sensor);

        mockMvc.perform(get("/api/sensores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("HUM_SOLO"));
    }

    @Test
    void should_listarPorDispositivo_when_sensorsExist() throws Exception {
        Sensor sensor1 = sistemaagricola.com.projecto.util.TestDataBuilder.sensor()
                .id(1L)
                .tipo("HUM_SOLO")
                .build();
        Sensor sensor2 = sistemaagricola.com.projecto.util.TestDataBuilder.sensor()
                .id(2L)
                .tipo("TEMP_AR")
                .build();

        List<Sensor> sensores = Arrays.asList(sensor1, sensor2);
        when(sensorService.listarPorDispositivo(1L)).thenReturn(sensores);

        mockMvc.perform(get("/api/sensores/por-dispositivo/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].tipo").value("HUM_SOLO"))
                .andExpect(jsonPath("$[1].tipo").value("TEMP_AR"));
    }

    @Test
    void should_remover_when_sensorExists() throws Exception {
        mockMvc.perform(delete("/api/sensores/1"))
                .andExpect(status().isOk());
    }
}

