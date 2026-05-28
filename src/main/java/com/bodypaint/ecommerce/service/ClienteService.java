package com.bodypaint.ecommerce.service;

import com.bodypaint.ecommerce.model.Cliente;
import com.bodypaint.ecommerce.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente registrar(Cliente cliente) {
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un cliente con ese email");
        }
        return clienteRepository.save(cliente);
    }

    public Cliente login(String email, String password) {
        Cliente cliente = clienteRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        if (!cliente.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        return cliente;
    }

    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }
}
