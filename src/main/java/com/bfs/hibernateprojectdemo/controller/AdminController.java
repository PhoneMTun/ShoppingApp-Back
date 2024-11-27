package com.bfs.hibernateprojectdemo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bfs.hibernateprojectdemo.domain.Order;
import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.dto.DataResponse;
import com.bfs.hibernateprojectdemo.exception.ValidationException;
import com.bfs.hibernateprojectdemo.service.OrderService;
import com.bfs.hibernateprojectdemo.service.ProductService;
@RestController
@RequestMapping("/admin")
public class AdminController {
	private ProductService productService;
	private OrderService orderService;
	
	public AdminController(ProductService productService, OrderService orderService) {
		this.productService = productService;
		this.orderService = orderService;
	}
	
	
	
	
	
	@PostMapping("/product")
	public DataResponse createProduct(@Valid @RequestBody Product product, BindingResult result) {
	    if (result.hasErrors()) {
	        throw new ValidationException(result); // Throw an exception
	    }
	    Product createdProduct = productService.createProduct(product);
	    return DataResponse.builder()
	            .success(true)
	            .message("Product created successfully")
	            .data(createdProduct)
	            .build();
	}

	@PatchMapping("/product/{id}")
	public DataResponse updateProduct(@PathVariable Integer id, @Valid @RequestBody Product updatedProduct, BindingResult result) {
	    if (result.hasErrors()) {
	        throw new ValidationException(result); // Throw an exception
	    }
	    Product product = productService.updateProduct(id, updatedProduct);
	    return DataResponse.builder()
	            .success(true)
	            .message("Product updated successfully")
	            .data(product)
	            .build();
	}

    
    @PatchMapping("/orders/{id}/complete")
    public DataResponse completeOrder(@PathVariable int id) {
        Order order = orderService.completeOrder(id);
        return DataResponse.builder()
                .success(true)
                .message("Order completed successfully")
                .data(order)
                .build();
    }
    @GetMapping("/profit/{limit}")
    public DataResponse getTopProfitableProducts(@PathVariable int limit) {
        List<Product> products = productService.getTopProfitableProducts(limit);
        return DataResponse.builder()
                .success(true)
                .message("Top profitable products retrieved successfully")
                .data(products)
                .build();
    }

    @GetMapping("/popular/{limit}")
    public DataResponse getTopPopularProducts(@PathVariable int limit) {
        List<Product> products = productService.getTopPopularProducts(limit);
        return DataResponse.builder()
                .success(true)
                .message("Top popular products retrieved successfully")
                .data(products)
                .build();
    }

}
