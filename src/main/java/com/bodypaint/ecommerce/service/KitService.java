package com.bodypaint.ecommerce.service;

import com.bodypaint.ecommerce.model.Kit;
import com.bodypaint.ecommerce.model.Producto;
import com.bodypaint.ecommerce.repository.KitRepository;
import com.bodypaint.ecommerce.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class KitService {

    private final KitRepository kitRepository;
    private final ProductoRepository productoRepository;

    public KitService(KitRepository kitRepository, ProductoRepository productoRepository) {
        this.kitRepository = kitRepository;
        this.productoRepository = productoRepository;
    }

    public List<Kit> obtenerTodos() {
        return kitRepository.findByActivoTrue();
    }

    public Kit obtenerPorId(Long id) {
        return kitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Kit no encontrado"));
    }

    public Kit crear(Kit kit) {
        kit.setActivo(true);
        return kitRepository.save(kit);
    }

    @Transactional
    public Kit agregarProducto(Long kitId, Long productoId) {
        Kit kit = kitRepository.findById(kitId)
            .orElseThrow(() -> new RuntimeException("Kit no encontrado"));
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        boolean yaExiste = kit.getProductos().stream()
            .anyMatch(p -> p.getId().equals(productoId));
        if (yaExiste) {
            throw new RuntimeException("El producto ya está en el kit");
        }
        kit.getProductos().add(producto);
        return kitRepository.save(kit);
    }

    public Kit darDeBaja(Long id) {
        Kit kit = obtenerPorId(id);
        kit.setActivo(false);
        return kitRepository.save(kit);
    }

    public Producto darDeBajaProducto(Long id) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setActivo(false);
        return productoRepository.save(producto);
    }

    public List<Kit> obtenerReporteKits() {
        return kitRepository.findAll();
    }
}
