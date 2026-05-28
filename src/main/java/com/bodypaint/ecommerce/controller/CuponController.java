package com.bodypaint.ecommerce.controller;

import com.bodypaint.ecommerce.model.Cupon;
import com.bodypaint.ecommerce.service.CuponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cupones")
public class CuponController {

    private final CuponService cuponService;

    public CuponController(CuponService cuponService) {
        this.cuponService = cuponService;
    }

    @GetMapping
    public List<Cupon> obtenerTodos() {
        return cuponService.obtenerTodos();
    }

    @PostMapping("/generar")
    public ResponseEntity<Cupon> generarCupon(@RequestBody Map<String, String> body) {
        String email = body.get("clienteEmail");
        Double descuento = Double.parseDouble(body.get("descuento"));
        LocalDate validoHasta = LocalDate.parse(body.get("validoHasta"));
        return ResponseEntity.ok(cuponService.generarCupon(email, descuento, validoHasta));
    }

    @PostMapping("/aplicar")
    public ResponseEntity<?> aplicarCupon(@RequestBody Map<String, String> body) {
        try {
            String codigo = body.get("codigo");
            Double monto = Double.parseDouble(body.get("montoTotal"));
            Cupon cupon = cuponService.aplicarCupon(codigo, monto);
            return ResponseEntity.ok(Map.of(
                "descuento", cupon.getDescuento(),
                "montoFinal", monto - cupon.getDescuento()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/usar/{codigo}")
    public ResponseEntity<Cupon> marcarComoUsado(@PathVariable String codigo) {
        return ResponseEntity.ok(cuponService.marcarComoUsado(codigo));
    }

    @GetMapping("/cliente/{email}")
    public List<Cupon> obtenerPorCliente(@PathVariable String email) {
        return cuponService.obtenerPorCliente(email);
    }
}
