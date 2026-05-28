package com.bodypaint.ecommerce.repository;

import com.bodypaint.ecommerce.model.Kit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KitRepository extends JpaRepository<Kit, Long> {

    List<Kit> findByActivoTrue();
}
