package com.bodypaint.ecommerce.service;

import com.bodypaint.ecommerce.model.Producto;
import com.bodypaint.ecommerce.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> obtenerTodos() {
        return productoRepository.findByActivoTrue();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Producto crear(Producto producto) {
        producto.setActivo(true);
        return productoRepository.save(producto);
    }

    public Producto actualizarStockMinimo(Long id, Integer stockMinimo) {
        Producto producto = obtenerPorId(id);
        if (stockMinimo < 0) {
            throw new RuntimeException("El stock mínimo no puede ser negativo");
        }
        producto.setStockMinimo(stockMinimo);
        return productoRepository.save(producto);
    }

    public List<Producto> obtenerProductosConBajoStock() {
        return productoRepository.findProductosConBajoStock();
    }

    public Producto descontarStock(Long id, Integer cantidad) {
        Producto producto = obtenerPorId(id);
        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
        }
        producto.setStock(producto.getStock() - cantidad);
        return productoRepository.save(producto);
    }
}
