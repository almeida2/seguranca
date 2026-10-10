package com.fatec.seguranca.service;

import com.fatec.seguranca.model.Usuario;
import com.fatec.seguranca.model.UsuarioDTO;
import com.fatec.seguranca.model.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario cadastrarUsuario(UsuarioDTO usuarioDto) {
        // Verifica se o usuário já existe
        if (usuarioRepository.findByUsername(usuarioDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username já existe!");
        }

        // Criptografa a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(usuarioDto.getPassword());

        String role = usuarioDto.getRole();
        if (role == null || role.trim().isEmpty()) {
            role = "ROLE_USER";
        }

        Usuario novoUsuario = new Usuario(
                usuarioDto.getUsername(),
                senhaCriptografada,
                role);

        return usuarioRepository.save(novoUsuario);
    }
}
