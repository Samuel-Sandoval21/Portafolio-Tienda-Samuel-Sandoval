/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tienda.demo.controller;

import Tienda.demo.service.CategoriaService;
import Tienda.demo.service.ProductoService;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {
   
    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public IndexController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/")
    public String cargarPaginaInicio(Model model) {
        var lista = productoService.getProductos(true);
        model.addAttribute("productos", lista);
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "index"; // ← Sin barra inicial
    }

    @GetMapping("/consultas/{idCategoria}")
    public String listado(@PathVariable("idCategoria") Long idCategoria, Model model) { // ← CORREGIDO: Long
        model.addAttribute("idCategoriaActual", idCategoria);
        var categoriaOptional = categoriaService.getCategoria(idCategoria);
        if (categoriaOptional.isEmpty()) {
            model.addAttribute("productos", java.util.Collections.emptyList());
        } else {
            var categoria = categoriaOptional.get();
            var productos = categoria.getProductos();
            model.addAttribute("productos", productos);

            var categorias = categoriaService.getCategorias(true);
            model.addAttribute("categorias", categorias);
        }
        return "index"; // ← Sin barra inicial
    }
}