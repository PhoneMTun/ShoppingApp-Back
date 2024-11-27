package com.bfs.hibernateprojectdemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bfs.hibernateprojectdemo.dao.ProductDao;
import com.bfs.hibernateprojectdemo.dao.UserDao;
import com.bfs.hibernateprojectdemo.dao.WatchlistDao;
import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.domain.Watchlist;
import com.bfs.hibernateprojectdemo.exception.ResourceNotFoundException;

@Service
public class WatchlistService {

    private final WatchlistDao watchlistDao;
    private final ProductDao productDao;
    private final UserDao userDao;

    @Autowired
    public WatchlistService(WatchlistDao watchlistDao, ProductDao productDao, UserDao userDao) {
        this.watchlistDao = watchlistDao;
        this.productDao = productDao;
        this.userDao=userDao;
    }


    @Transactional(readOnly = true)
    public List<Product> getAllWatchlistProducts(Integer userId) {
        return watchlistDao.getAllWatchlistProductsByUserId(userId);
    }


    @Transactional
    public void addProductToWatchlist(Integer userId, Integer productId) {
        Product product = productDao.findProductById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        User user = userDao.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        if(product.getStockQuantity()==0) {
        	throw new IllegalArgumentException("Product not in stock to add to watchlist");
        }

        // Check if the product is already in the user's watchlist
        Optional<Watchlist> existingWatchlistEntry = watchlistDao.findByUserIdAndProductId(userId, productId);
        if (existingWatchlistEntry.isPresent()) {
            throw new IllegalArgumentException("Product is already in the watchlist");
        }

        // Create a new watchlist entry if it doesn't already exist
        Watchlist watchlistEntry = new Watchlist();
        watchlistEntry.setUser(user);
        watchlistEntry.setProduct(product);
        watchlistDao.add(watchlistEntry);
    }



    @Transactional
    public void removeProductFromWatchlist(Integer userId, Integer productId) {
        
    	watchlistDao.removeProductFromWatchlist(userId, productId);
    }
}
