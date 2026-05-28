package com.bodypaint.ecommerce.service;

import com.bodypaint.ecommerce.model.Cupon;
import com.bodypaint.ecommerce.repository.CuponRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CuponService {

    private final CuponRepository cuponRepository;

    public CuponService(CuponRepository cuponRepository) {
        this.cuponRepository = cuponRepository;
    }

    public Cupon generarCupon(String clienteEmail, Double descuento, LocalDate validoHasta) {
        Cupon cupon = new Cupon();
        cupon.setCodigo(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        cupon.setClienteEmail(clienteEmail);
        cupon.setDescuento(descuento);
        cupon.setValidoHasta(validoHasta);
        cupon.setUsado(false);
        return cuponRepository.save(cupon);
    }

    public Cupon aplicarCupon(String codigo, Double montoTotal) {
        Cupon cupon = cuponRepository.findByCodigo(codigo)
            .orElseThrow(() -> new RuntimeException("Cupón no encontrado"));
        if (cupon.getUsado()) {
            throw new RuntimeException("El cupón ya fue utilizado");
        }
        if (cupon.getValidoHasta().isBefore(LocalDate.now())) {
            throw new RuntimeException("El cupón está vencido");
        }
        if (montoTotal <= cupon.getDescuento()) {
            throw new RuntimeException("El monto del pedido debe ser mayor al descuento");
        }
        return cupon;
    }

    public Cupon marcarComoUsado(String codigo) {
        Cupon cupon = cuponRepository.findByCodigo(codigo)
            .orElseThrow(() -> new RuntimeException("Cupón no encontrado"));
        cupon.setUsado(true);
        return cuponRepository.save(cupon);
    }

    public List<Cupon> obtenerTodos() {
        return cuponRepository.findAll();
    }

    public List<Cupon> obtenerPorCliente(String email) {
        return cuponRepository.findByClienteEmail(email);
    }
}
