package com.fatec.seguranca.service;

import com.fatec.seguranca.repository.UsuarioRepository;
import com.fatec.seguranca.service.model.Usuario;
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

    public Usuario cadastrarUsuario(Usuario usuario) {
        // Verifica se o usuário já existe
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new RuntimeException("Username já existe!");
        }

        // Criptografa a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(usuario.getPassword());

        String role = usuario.getRole();
        if (role == null || role.trim().isEmpty()) {
            role = "ROLE_USER";
        }

        Usuario novoUsuario = new Usuario(
                usuario.getUsername(),
                senhaCriptografada,
                role
        );

        return usuarioRepository.save(novoUsuario);
    }
}
