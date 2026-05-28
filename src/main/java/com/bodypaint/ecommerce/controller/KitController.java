package com.bodypaint.ecommerce.controller;

import com.bodypaint.ecommerce.model.Kit;
import com.bodypaint.ecommerce.model.Producto;
import com.bodypaint.ecommerce.service.KitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/kits")
public class KitController {

    private final KitService kitService;

    public KitController(KitService kitService) {
        this.kitService = kitService;
    }

    @GetMapping
    public List<Kit> obtenerTodos() {
        return kitService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Kit> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(kitService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Kit> crear(@RequestBody Kit kit) {
        return ResponseEntity.ok(kitService.crear(kit));
    }

    @PostMapping("/{kitId}/productos/{productoId}")
    public ResponseEntity<Kit> agregarProducto(
            @PathVariable Long kitId,
            @PathVariable Long productoId) {
        return ResponseEntity.ok(kitService.agregarProducto(kitId, productoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Kit> darDeBaja(@PathVariable Long id) {
        return ResponseEntity.ok(kitService.darDeBaja(id));
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Producto> darDeBajaProducto(@PathVariable Long id) {
        return ResponseEntity.ok(kitService.darDeBajaProducto(id));
    }

    @GetMapping("/reporte")
    public List<Kit> obtenerReporte() {
        return kitService.obtenerReporteKits();
    }
}
