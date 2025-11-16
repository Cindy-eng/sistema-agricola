package sistemaagricola.com.projecto.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * EnvironmentPostProcessor que converte a URL do banco de dados do formato Render
 * para formato JDBC antes da inicialização do DataSource.
 * 
 * Executa ANTES do ApplicationEnvironmentPreparedEvent, garantindo que a conversão
 * aconteça antes de qualquer bean ser criado.
 */
public class DatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String datasourceUrl = environment.getProperty("SPRING_DATASOURCE_URL");
        
        if (datasourceUrl != null && !datasourceUrl.isEmpty()) {
            String convertedUrl = convertToJdbcUrl(datasourceUrl);
            
            // Sempre substitui se a URL foi convertida ou contém credenciais
            if (!convertedUrl.equals(datasourceUrl) || datasourceUrl.contains("@")) {
                Map<String, Object> properties = new HashMap<>();
                properties.put("spring.datasource.url", convertedUrl);
                
                MapPropertySource propertySource = new MapPropertySource("databaseUrlConverter", properties);
                environment.getPropertySources().addFirst(propertySource);
            }
        }
    }

    /**
     * Converte URL do Render (postgresql://user:pass@host/db ou jdbc:postgresql://user:pass@host/db) 
     * para formato JDBC (jdbc:postgresql://host:port/db) removendo credenciais
     */
    private String convertToJdbcUrl(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }

        boolean isJdbcFormat = url.startsWith("jdbc:postgresql://");
        boolean isPostgresqlFormat = url.startsWith("postgresql://");
        
        // Se não é nenhum dos formatos esperados, retorna como está
        if (!isJdbcFormat && !isPostgresqlFormat) {
            return url;
        }

        // Remove o prefixo apropriado
        String urlWithoutPrefix;
        if (isJdbcFormat) {
            urlWithoutPrefix = url.substring("jdbc:postgresql://".length());
        } else {
            urlWithoutPrefix = url.substring("postgresql://".length());
        }

        // Verifica se tem credenciais embutidas (@)
        boolean hasCredentials = urlWithoutPrefix.contains("@");
        
        // Se está no formato postgresql:// ou tem credenciais, converte
        if (isPostgresqlFormat || hasCredentials) {
            try {
                String hostAndPath;
                
                if (hasCredentials) {
                    // Encontra o @ que separa credenciais do host
                    int atIndex = urlWithoutPrefix.indexOf('@');
                    // Remove as credenciais (user:password@)
                    hostAndPath = urlWithoutPrefix.substring(atIndex + 1);
                } else {
                    // Se não tem credenciais e já está no formato JDBC, retorna como está
                    if (isJdbcFormat) {
                        return url;
                    }
                    hostAndPath = urlWithoutPrefix;
                }
                
                // Separa host:port do path/database
                int slashIndex = hostAndPath.indexOf('/');
                String hostPort;
                String database;
                
                if (slashIndex != -1) {
                    hostPort = hostAndPath.substring(0, slashIndex);
                    database = hostAndPath.substring(slashIndex + 1);
                } else {
                    hostPort = hostAndPath;
                    database = "";
                }
                
                // Separa host e port
                int colonIndex = hostPort.indexOf(':');
                String host;
                int port;
                
                if (colonIndex != -1) {
                    host = hostPort.substring(0, colonIndex);
                    try {
                        port = Integer.parseInt(hostPort.substring(colonIndex + 1));
                    } catch (NumberFormatException e) {
                        port = 5432; // Porta padrão PostgreSQL
                    }
                } else {
                    host = hostPort;
                    port = 5432; // Porta padrão PostgreSQL
                }
                
                // Constrói a URL JDBC
                if (database.isEmpty()) {
                    return String.format("jdbc:postgresql://%s:%d", host, port);
                } else {
                    return String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
                }
            } catch (Exception e) {
                // Se falhar a conversão, retorna a URL original
                return url;
            }
        }

        return url;
    }
}

