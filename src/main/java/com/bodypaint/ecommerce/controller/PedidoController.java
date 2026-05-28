package com.bodypaint.ecommerce.controller;

import com.bodypaint.ecommerce.model.Pedido;
import com.bodypaint.ecommerce.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmar(@RequestBody Map<String, Object> body) {
        try {
            Long clienteId = Long.parseLong(body.get("clienteId").toString());
            List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
            String formaPago = body.get("formaPago").toString();
            String cupon = body.containsKey("cupon") ? body.get("cupon").toString() : null;
            Pedido pedido = pedidoService.confirmarPedido(clienteId, items, formaPago, cupon);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<Pedido> obtenerTodos() {
        return pedidoService.obtenerTodos();
    }

    @GetMapping("/pendientes")
    public List<Pedido> obtenerPendientes() {
        return pedidoService.obtenerPendientes();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(pedidoService.actualizarEstado(id, body.get("estado")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
