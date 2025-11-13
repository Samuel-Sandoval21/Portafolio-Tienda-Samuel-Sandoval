package Tienda.demo.service;

import Tienda.demo.domain.Categoria;
import Tienda.demo.repository.CategoriaRepository;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) {
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Categoria> getCategoria(Long idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }

    @Transactional
    public void save(Categoria categoria, MultipartFile imagenFile) {
        categoria = categoriaRepository.save(categoria);
        if (!imagenFile.isEmpty()) {            
            try {
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile, "categoria",
                        categoria.getIdCategoria().intValue());
                categoria.setRutaImagen(rutaImagen);
                categoriaRepository.save(categoria);
            } catch (IOException e) {
                // Log del error
                System.err.println("Error al subir imagen: " + e.getMessage());
            }
        }
    }

    @Transactional
    public void delete(Long idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new IllegalArgumentException("La categoría con ID " + idCategoria + " no existe.");
        }
        try {
            categoriaRepository.deleteById(idCategoria);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar la categoria. Tiene datos asociados.", e);
        }
    }
}