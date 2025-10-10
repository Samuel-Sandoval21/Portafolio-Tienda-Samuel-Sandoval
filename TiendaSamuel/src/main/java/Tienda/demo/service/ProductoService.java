/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tienda.demo.service;
import Tienda.demo.domain.Producto;
import Tienda.demo.repository.ProductoRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author HP
 */

@Service
public class ProductoService {
     @Autowired
    private ProductoRepository productoRepository;

    // Lista todos los productos, filtrando solo activos si se desea
    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean activos) {
        var lista = productoRepository.findAll();
        if (activos) {
            lista.removeIf(p -> !p.isActivo());
        }
        return lista;
    }

    // Obtiene un producto por su id
    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }

    // Guarda un producto con o sin imagen
    @Transactional
public void save(Producto producto, MultipartFile imagenFile) {
    try {
        if (imagenFile != null && !imagenFile.isEmpty()) {
            // Guarda solo el nombre del archivo (la ruta), no el contenido binario
            producto.setRutaImagen(imagenFile.getOriginalFilename());
        }
        productoRepository.save(producto);
    } catch (Exception e) {
        throw new RuntimeException("Error al guardar el producto", e);
    }
}

    // Elimina un producto por su id
    @Transactional
    public void delete(Integer idProducto) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        productoRepository.delete(producto);
    }
}

