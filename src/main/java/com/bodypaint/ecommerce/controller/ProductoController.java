package com.bodypaint.ecommerce.controller;

import com.bodypaint.ecommerce.model.Producto;
import com.bodypaint.ecommerce.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> obtenerTodos() {
        return productoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.crear(producto));
    }

    @PatchMapping("/{id}/stock-minimo")
    public ResponseEntity<Producto> actualizarStockMinimo(
            @PathVariable Long id,
            @RequestParam Integer stockMinimo) {
        return ResponseEntity.ok(productoService.actualizarStockMinimo(id, stockMinimo));
    }

    @GetMapping("/bajo-stock")
    public List<Producto> obtenerProductosConBajoStock() {
        return productoService.obtenerProductosConBajoStock();
    }
}
