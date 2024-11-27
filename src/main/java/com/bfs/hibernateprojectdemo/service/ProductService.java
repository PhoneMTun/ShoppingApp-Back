package com.bfs.hibernateprojectdemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bfs.hibernateprojectdemo.dao.OrderDao;
import com.bfs.hibernateprojectdemo.dao.ProductDao;
import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.exception.ResourceNotFoundException;

@Service
@Transactional
public class ProductService {

    private final ProductDao productDao;
    private final OrderDao orderDao;

    @Autowired
    public ProductService(ProductDao productDao, OrderDao orderDao) {
        this.productDao = productDao;
        this.orderDao=orderDao;
    }

    public List<Product> getAllAvailableProducts() {
        return productDao.getAllInStockProducts();
    }


    public Product getProductById(int id) {
    	productDao.findProductById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    	return productDao.findById(id);
    }

    @Transactional
    public Product createProduct(Product product) {
        productDao.addProduct(product);
        return product;
    }
    
    
    @Transactional(readOnly = true)
    public List<Product> getMostFrequentlyPurchasedProducts(Integer userId, int limit) {
        return orderDao.getMostFrequentlyPurchasedProducts(userId, limit);
    }

    @Transactional(readOnly = true)
    public List<Product> getMostRecentlyPurchasedProducts(Integer userId, int limit) {
        return orderDao.getMostRecentlyPurchasedProducts(userId, limit);
    }
    
    @Transactional
    public Product updateProduct(Integer productId, Product updatedProduct) {
        Product existingProduct = productDao.findProductById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setWholesalePrice(updatedProduct.getWholesalePrice());
        existingProduct.setRetailPrice(updatedProduct.getRetailPrice());
        existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());

        productDao.updateProduct(existingProduct);
        return existingProduct;
    }
    
    public List<Product> getTopProfitableProducts(int limit) {
        return productDao.getTopProfitableProducts(limit);
    }

    public List<Product> getTopPopularProducts(int limit) {
        return productDao.getTopPopularProducts(limit);
    }

	public List<Product> getAllProducts() {
		return productDao.getAll();
	}
    
    
}
