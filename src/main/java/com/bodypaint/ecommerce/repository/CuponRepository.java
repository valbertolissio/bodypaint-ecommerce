package com.bodypaint.ecommerce.repository;

import com.bodypaint.ecommerce.model.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CuponRepository extends JpaRepository<Cupon, Long> {

    Optional<Cupon> findByCodigo(String codigo);
    List<Cupon> findByClienteEmail(String clienteEmail);
}
