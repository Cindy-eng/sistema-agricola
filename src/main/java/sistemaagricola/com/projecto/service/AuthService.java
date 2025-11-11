package sistemaagricola.com.projecto.service;

import sistemaagricola.com.projecto.dto.AuthResponse;
import sistemaagricola.com.projecto.dto.LoginRequest;
import sistemaagricola.com.projecto.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}

