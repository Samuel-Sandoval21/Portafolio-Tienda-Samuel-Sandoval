/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tienda.demo.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="categoria")


/**
 *
 * @author HP
 */
public class Categoria implements Serializable{
    //Se recomienda añadir un serialVersionUID
     private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column (unique = true, nullable = false, length = 50)
    private String descripcion;
    
    @Column (length = 1024)
    private String rutaImagen;
    
    @Column (name = "activo")
    private Boolean activo;

    //Realación de uno a muchos con la clase Producto
    @OneToMany(mappedBy = "categoria")
    private List<Producto>productos;
    }
