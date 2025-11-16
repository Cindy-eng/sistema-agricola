package sistemaagricola.com.projecto.config;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * Converte automaticamente a URL do banco de dados do formato Render
 * (postgresql://user:pass@host/db) para formato JDBC (jdbc:postgresql://host:port/db)
 */
public class DatabaseConfig implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        String datasourceUrl = environment.getProperty("SPRING_DATASOURCE_URL");
        
        if (datasourceUrl != null && !datasourceUrl.isEmpty()) {
            String convertedUrl = convertToJdbcUrl(datasourceUrl);
            
            if (!convertedUrl.equals(datasourceUrl)) {
                Map<String, Object> properties = new HashMap<>();
                properties.put("spring.datasource.url", convertedUrl);
                
                MapPropertySource propertySource = new MapPropertySource("databaseUrlConverter", properties);
                environment.getPropertySources().addFirst(propertySource);
            }
        }
    }

    /**
     * Converte URL do Render (postgresql://user:pass@host/db) 
     * para formato JDBC (jdbc:postgresql://host:port/db)
     */
    private String convertToJdbcUrl(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }

        // Se já está no formato JDBC, retorna como está
        if (url.startsWith("jdbc:postgresql://")) {
            return url;
        }

        // Se está no formato postgresql://, converte
        if (url.startsWith("postgresql://")) {
            try {
                // Remove o prefixo postgresql://
                String urlWithoutPrefix = url.substring("postgresql://".length());
                
                // Encontra o @ que separa credenciais do host
                int atIndex = urlWithoutPrefix.indexOf('@');
                String hostAndPath;
                
                if (atIndex != -1) {
                    // Remove as credenciais (user:password@)
                    hostAndPath = urlWithoutPrefix.substring(atIndex + 1);
                } else {
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
                // Log do erro seria útil aqui, mas não temos logger neste ponto
                return url;
            }
        }

        return url;
    }
}

