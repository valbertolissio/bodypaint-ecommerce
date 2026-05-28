package com.bodypaint.ecommerce.service;

import com.bodypaint.ecommerce.model.*;
import com.bodypaint.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final CuponService cuponService;

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository,
                         ProductoRepository productoRepository, CuponService cuponService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.cuponService = cuponService;
    }

    @Transactional
    public Pedido confirmarPedido(Long clienteId, List<Map<String, Object>> itemsData,
                                   String formaPago, String codigoCupon) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFormaPago(formaPago);
        pedido.setEstado("PENDIENTE");
        pedido.setFecha(LocalDateTime.now());
        pedido.setDescuentoAplicado(0.0);

        double total = 0.0;

        for (Map<String, Object> itemData : itemsData) {
            Long productoId = Long.parseLong(itemData.get("productoId").toString());
            Integer cantidad = Integer.parseInt(itemData.get("cantidad").toString());

            Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProducto(producto);
            item.setCantidad(cantidad);
            item.setPrecioUnitario(producto.getPrecio());
            pedido.getItems().add(item);

            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);

            total += producto.getPrecio() * cantidad;
        }

        if (codigoCupon != null && !codigoCupon.isEmpty()) {
            try {
                Cupon cupon = cuponService.aplicarCupon(codigoCupon, total);
                pedido.setCodigoCupon(codigoCupon);
                pedido.setDescuentoAplicado(cupon.getDescuento());
                total -= cupon.getDescuento();
                cuponService.marcarComoUsado(codigoCupon);
            } catch (Exception e) {
                throw new RuntimeException("Error con el cupón: " + e.getMessage());
            }
        }

        pedido.setTotal(total);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> obtenerPendientes() {
        return pedidoRepository.findByEstado("PENDIENTE");
    }

    public Pedido actualizarEstado(Long id, String estado) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }
}
