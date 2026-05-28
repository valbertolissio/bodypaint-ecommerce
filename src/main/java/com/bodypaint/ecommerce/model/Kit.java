package com.bodypaint.ecommerce.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "kits")
public class Kit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Double precio;
    private Boolean activo;

    @ManyToMany
    @JoinTable(
        name = "kit_productos",
        joinColumns = @JoinColumn(name = "kit_id"),
        inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<Producto> productos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "kit_subkits",
        joinColumns = @JoinColumn(name = "kit_id"),
        inverseJoinColumns = @JoinColumn(name = "subkit_id")
    )
    private List<Kit> subkits = new ArrayList<>();

    public Kit() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }

    public List<Kit> getSubkits() { return subkits; }
    public void setSubkits(List<Kit> subkits) { this.subkits = subkits; }
}
