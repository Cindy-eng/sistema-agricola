#!/usr/bin/env python3
"""
Script de teste para os endpoints do Sistema Agrícola
Uso: python test-endpoints.py
"""

import requests
import json
from datetime import datetime, timezone

BASE_URL = "http://localhost:8080"
token = None

# Cores para output
class Colors:
    GREEN = '\033[92m'
    RED = '\033[91m'
    YELLOW = '\033[93m'
    BLUE = '\033[94m'
    END = '\033[0m'

def print_colored(message, color=Colors.END):
    print(f"{color}{message}{Colors.END}")

def make_request(method, endpoint, data=None, use_token=True):
    """Faz uma requisição HTTP"""
    url = f"{BASE_URL}{endpoint}"
    headers = {"Content-Type": "application/json"}
    
    if use_token and token:
        headers["Authorization"] = f"Bearer {token}"
    elif use_token and not token:
        print_colored("Erro: Token não disponível. Faça login primeiro.", Colors.RED)
        return None
    
    try:
        if method == "GET":
            response = requests.get(url, headers=headers)
        elif method == "POST":
            response = requests.post(url, headers=headers, json=data)
        elif method == "PUT":
            response = requests.put(url, headers=headers, json=data)
        elif method == "DELETE":
            response = requests.delete(url, headers=headers)
        else:
            print_colored(f"Método {method} não suportado", Colors.RED)
            return None
        
        print_colored(f"Status: {response.status_code}", Colors.YELLOW)
        
        try:
            result = response.json()
            print(json.dumps(result, indent=2, ensure_ascii=False))
            return result
        except:
            print(response.text)
            return {"text": response.text, "status": response.status_code}
            
    except requests.exceptions.ConnectionError:
        print_colored("Erro: Não foi possível conectar ao servidor. Verifique se está rodando em http://localhost:8080", Colors.RED)
        return None
    except Exception as e:
        print_colored(f"Erro: {str(e)}", Colors.RED)
        return None

def main():
    global token
    
    print_colored("=== Testando Endpoints do Sistema Agrícola ===\n", Colors.BLUE)
    
    # 1. Registrar usuário
    print_colored("1. Registrando novo usuário...", Colors.GREEN)
    register_data = {
        "nome": "Teste User",
        "email": "teste@example.com",
        "password": "senha123"
    }
    result = make_request("POST", "/api/auth/register", register_data, use_token=False)
    
    if result and "token" in result:
        token = result["token"]
        print_colored(f"Token obtido: {token[:50]}...\n", Colors.GREEN)
    else:
        # Tentar fazer login
        print_colored("2. Fazendo login...", Colors.GREEN)
        login_data = {
            "email": "teste@example.com",
            "password": "senha123"
        }
        result = make_request("POST", "/api/auth/login", login_data, use_token=False)
        
        if result and "token" in result:
            token = result["token"]
            print_colored(f"Token obtido: {token[:50]}...\n", Colors.GREEN)
        else:
            print_colored("Erro: Não foi possível obter token.", Colors.RED)
            return
    
    # 3. Criar Parcela
    print_colored("\n3. Criando parcela...", Colors.GREEN)
    parcela_data = {
        "nome": "Parcela Teste",
        "lat": -15.7975,
        "lon": -47.8919
    }
    result = make_request("POST", "/api/parcelas", parcela_data)
    parcela_id = result.get("id") if result else None
    print_colored(f"Parcela criada com ID: {parcela_id}\n", Colors.GREEN) if parcela_id else None
    
    # 4. Listar Parcelas
    print_colored("4. Listando parcelas...", Colors.GREEN)
    make_request("GET", "/api/parcelas")
    
    # 5. Criar Cultura
    print_colored("\n5. Criando cultura...", Colors.GREEN)
    cultura_data = {
        "nome": "Milho"
    }
    make_request("POST", "/api/culturas", cultura_data)
    
    # 6. Listar Culturas
    print_colored("\n6. Listando culturas...", Colors.GREEN)
    make_request("GET", "/api/culturas")
    
    # 7. Criar Dispositivo
    if parcela_id:
        print_colored("\n7. Criando dispositivo...", Colors.GREEN)
        dispositivo_data = {
            "deviceKey": "DEVICE-TEST-001",
            "tipo": "Sensor de Umidade",
            "modelo": "SM-2024",
            "firmware": "v1.0.0",
            "parcela": {
                "id": parcela_id,
                "nome": "Parcela Teste",
                "lat": -15.7975,
                "lon": -47.8919
            },
            "estado": "ACTIVO"
        }
        result = make_request("POST", "/api/dispositivos", dispositivo_data)
        dispositivo_id = result.get("id") if result else None
        print_colored(f"Dispositivo criado com ID: {dispositivo_id}\n", Colors.GREEN) if dispositivo_id else None
        
        # 8. Listar Dispositivos
        print_colored("8. Listando dispositivos...", Colors.GREEN)
        make_request("GET", "/api/dispositivos")
        
        # 9. Criar Sensor
        if dispositivo_id:
            print_colored("\n9. Criando sensor...", Colors.GREEN)
            sensor_data = {
                "dispositivoId": dispositivo_id,
                "tipo": "UMIDADE_SOLO",
                "unidade": "%"
            }
            result = make_request("POST", "/api/sensores", sensor_data)
            sensor_id = result.get("id") if result else None
            print_colored(f"Sensor criado com ID: {sensor_id}\n", Colors.GREEN) if sensor_id else None
            
            # 10. Ingestão de Telemetria (público)
            if sensor_id:
                print_colored("10. Enviando telemetria...", Colors.GREEN)
                telemetria_data = {
                    "deviceKey": "DEVICE-TEST-001",
                    "sensorId": sensor_id,
                    "ts": datetime.now(timezone.utc).isoformat().replace("+00:00", "Z"),
                    "valor": 65.5
                }
                make_request("POST", "/api/telemetria/ingestao", telemetria_data, use_token=False)
                
                # 11. Obter Última Telemetria
                print_colored("\n11. Obtendo última telemetria...", Colors.GREEN)
                make_request("GET", f"/api/telemetria/ultima?sensorId={sensor_id}")
    
    print_colored("\n=== Testes concluídos! ===", Colors.BLUE)

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print_colored("\n\nTeste interrompido pelo usuário.", Colors.YELLOW)

