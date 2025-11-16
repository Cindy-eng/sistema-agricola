package sistemaagricola.com.projecto.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema Agrícola API")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de sistema agrícola com IoT. " +
                                "Permite gerenciar parcelas, culturas, dispositivos, sensores, telemetria e alertas.")
                        .contact(new Contact()
                                .name("Sistema Agrícola")
                                .email("suporte@sistemaagricola.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .tags(List.of(
                        new Tag().name("Autenticação").description("Endpoints de autenticação e registro de usuários"),
                        new Tag().name("Parcelas").description("Gerenciamento de parcelas agrícolas"),
                        new Tag().name("Culturas").description("Gerenciamento de culturas"),
                        new Tag().name("Dispositivos").description("Gerenciamento de dispositivos IoT"),
                        new Tag().name("Sensores").description("Gerenciamento de sensores"),
                        new Tag().name("Telemetria").description("Ingestão e consulta de dados de telemetria"),
                        new Tag().name("Eventos IoT").description("Gerenciamento de eventos de dispositivos IoT"),
                        new Tag().name("Manutenções").description("Gerenciamento de manutenções de dispositivos"),
                        new Tag().name("Regras de Alerta").description("Gerenciamento de regras de alerta")
                ));
    }
}

