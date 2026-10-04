package com.fatec.seguranca.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fatec.seguranca.service.UsuarioService;
import com.fatec.seguranca.service.model.Usuario;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(Authentication authentication) {
        // Se a requisição chegou aqui, a autenticação em Base64 foi um sucesso!
        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Autenticação realizada com sucesso!");
        response.put("usuario", authentication.getName());
        response.put("permissoes", authentication.getAuthorities());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/status")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Endpoint público acessível sem autenticação.");
    }

    @PostMapping("/public/register")
    public ResponseEntity<Object> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
}