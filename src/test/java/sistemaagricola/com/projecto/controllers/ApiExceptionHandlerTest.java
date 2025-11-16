package sistemaagricola.com.projecto.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import sistemaagricola.com.projecto.exception.BusinessException;
import sistemaagricola.com.projecto.exception.NotFoundException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {

    @InjectMocks
    private ApiExceptionHandler exceptionHandler;

    @Test
    void should_handleNotFoundException() {
        NotFoundException exception = new NotFoundException("Recurso não encontrado");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.notFound(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("NOT_FOUND");
        assertThat(response.getBody().get("message")).isEqualTo("Recurso não encontrado");
    }

    @Test
    void should_handleBusinessException() {
        BusinessException exception = new BusinessException("Regra de negócio violada");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.business(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("BUSINESS_RULE");
        assertThat(response.getBody().get("message")).isEqualTo("Regra de negócio violada");
    }

    @Test
    void should_handleMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("object", "email", "Email inválido");
        FieldError fieldError2 = new FieldError("object", "password", "Senha muito curta");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        ResponseEntity<Map<String, Object>> response = exceptionHandler.validation(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("VALIDATION");
        assertThat(response.getBody().get("details")).isNotNull();
    }

    @Test
    void should_handleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Argumento inválido");

        ResponseEntity<Map<String, Object>> response = exceptionHandler.bad(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo("BAD_REQUEST");
        assertThat(response.getBody().get("message")).isEqualTo("Argumento inválido");
    }
}

