package com.bfs.hibernateprojectdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.dto.DataResponse;
import com.bfs.hibernateprojectdemo.exception.ResourceNotFoundException;
import com.bfs.hibernateprojectdemo.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/allInStock")
    public DataResponse getAllInStockProducts() {
        List<Product> products = productService.getAllAvailableProducts();
        return DataResponse.builder()
                .success(true)
                .message("In-stock products retrieved successfully")
                .data(products)
                .build();
    }
    @GetMapping("/all")
    public DataResponse getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return DataResponse.builder()
                .success(true)
                .message("All Products retrieved successfully")
                .data(products)
                .build();
    }
    
    

    @GetMapping("/{id}")
    public DataResponse getProductDetailById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        return DataResponse.builder()
                .success(true)
                .message("Product details retrieved successfully")
                .data(product)
                .build();
    }
    @GetMapping("/frequent/{limit}")
    public DataResponse getMostFrequentlyPurchasedProducts(@RequestParam Integer userId, @PathVariable int limit) {
        List<Product> products = productService.getMostFrequentlyPurchasedProducts(userId, limit);
        return DataResponse.builder()
                .success(true)
                .message("Fetched most frequently purchased products")
                .data(products)
                .build();
    }

    // Endpoint to get the most recently purchased products
    @GetMapping("/recent/{limit}")
    public DataResponse getMostRecentlyPurchasedProducts(@RequestParam Integer userId, @PathVariable int limit) {
        List<Product> products = productService.getMostRecentlyPurchasedProducts(userId, limit);
        return DataResponse.builder()
                .success(true)
                .message("Fetched most recently purchased products")
                .data(products)
                .build();
    }
    


    
}

