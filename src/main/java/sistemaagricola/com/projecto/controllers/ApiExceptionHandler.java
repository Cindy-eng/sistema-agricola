// src/main/java/sistemaagricola/com/projecto/controllers/ApiExceptionHandler.java
package sistemaagricola.com.projecto.controllers;

import sistemaagricola.com.projecto.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Map<String,Object>> notFound(NotFoundException ex){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","NOT_FOUND","message",ex.getMessage()));
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<Map<String,Object>> business(BusinessException ex){
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("error","BUSINESS_RULE","message",ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String,Object>> validation(MethodArgumentNotValidException ex){
    var errors = ex.getBindingResult().getFieldErrors().stream().map(f -> Map.of("field",f.getField(),"msg",f.getDefaultMessage())).toList();
    return ResponseEntity.badRequest().body(Map.of("error","VALIDATION","details",errors));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String,Object>> bad(IllegalArgumentException ex){
    return ResponseEntity.badRequest().body(Map.of("error","BAD_REQUEST","message",ex.getMessage()));
  }
}
