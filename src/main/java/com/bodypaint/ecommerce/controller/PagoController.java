package com.bodypaint.ecommerce.controller;

import com.bodypaint.ecommerce.service.PagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping("/mercadopago")
    public ResponseEntity<?> crearPago(@RequestBody Map<String, String> body) {
        try {
            String titulo = body.get("titulo");
            Double monto = Double.parseDouble(body.get("monto"));
            String url = pagoService.crearPreferencia(titulo, monto);
            return ResponseEntity.ok(Map.of("url", url));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
