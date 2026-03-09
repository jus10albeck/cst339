package com.gcu.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gcu.api.dto.ProductDto;

@RestController
@RequestMapping("/service")
public class ProductsRestController 
{

    private final ProductReadDao dao;

    public ProductsRestController(ProductReadDao dao) 
    {
        this.dao = dao;
    }

    /** Return all products */
    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAll() 
    {
        return ResponseEntity.ok(dao.findAll());
    }

    /** Return a desired product */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getOne(@PathVariable Long id) 
    {
        var product = dao.findById(id);
        return (product == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(product);
    }

}
