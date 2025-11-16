package sistemaagricola.com.projecto.util;

import sistemaagricola.com.projecto.models.*;

import java.time.Instant;

public class TestDataBuilder {

    public static User.UserBuilder user() {
        return User.builder()
                .id(1L)
                .email("test@example.com")
                .nome("Test User")
                .password("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJ5C") // password123
                .role(User.Role.USER)
                .enabled(true);
    }

    public static User.UserBuilder admin() {
        return user()
                .email("admin@example.com")
                .nome("Admin User")
                .role(User.Role.ADMIN);
    }

    public static Cultura.CulturaBuilder cultura() {
        return Cultura.builder()
                .id(1L)
                .nome("Milho");
    }

    public static Parcela.ParcelaBuilder parcela() {
        return Parcela.builder()
                .id(1L)
                .nome("Parcela Norte")
                .lat(-23.5505)
                .lon(-46.6333);
    }

    public static Dispositivo.DispositivoBuilder dispositivo() {
        return Dispositivo.builder()
                .id(1L)
                .deviceKey("DEV-001")
                .tipo("Sensor Hub")
                .modelo("SH-2024")
                .firmware("v1.0.0")
                .estado(Dispositivo.Estado.ACTIVO);
    }

    public static Sensor.SensorBuilder sensor() {
        return Sensor.builder()
                .id(1L)
                .tipo("HUM_SOLO")
                .unidade("%")
                .calibVigente("2024-01-01");
    }

    public static Telemetria.TelemetriaBuilder telemetria() {
        return Telemetria.builder()
                .id(1L)
                .ts(Instant.now())
                .valor(25.5)
                .qualidade("OK");
    }

    public static RegraAlerta.RegraAlertaBuilder regraAlerta() {
        return RegraAlerta.builder()
                .id(1L)
                .expressao("tipo==HUM_SOLO && valor<18 por 120m")
                .severidade("AVISO")
                .destino("APP")
                .activo(true);
    }

    public static Manutencao.ManutencaoBuilder manutencao() {
        return Manutencao.builder()
                .id(1L)
                .tipo("CALIBRACAO")
                .estado("ABERTA")
                .tsAbertura(Instant.now());
    }

    public static EventoIot.EventoIotBuilder eventoIot() {
        return EventoIot.builder()
                .id(1L)
                .tipo("ONLINE")
                .ts(Instant.now())
                .payload("{\"status\":\"online\"}");
    }
}

