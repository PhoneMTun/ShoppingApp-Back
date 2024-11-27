package com.bfs.hibernateprojectdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bfs.hibernateprojectdemo.dto.DataResponse;
import com.bfs.hibernateprojectdemo.service.WatchlistService;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {
    private final WatchlistService watchlistService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping("/products/all")
    public DataResponse getAllWatchlistItems(@RequestParam Integer userId) {
        return DataResponse.builder()
                .success(true)
                .message("Watchlist items fetched successfully")
                .data(watchlistService.getAllWatchlistProducts(userId))
                .build();
    }

    @PostMapping("/product/{productId}")
    public DataResponse addProductToWatchlist(@RequestParam Integer userId, @PathVariable Integer productId) {
        watchlistService.addProductToWatchlist(userId, productId);
        return DataResponse.builder()
                .success(true)
                .message("Product added to watchlist successfully")
                .build();
    }

    @DeleteMapping("/product/{productId}")
    public DataResponse removeProductFromWatchlist(@RequestParam Integer userId, @PathVariable Integer productId) {
        watchlistService.removeProductFromWatchlist(userId, productId);
        return DataResponse.builder()
                .success(true)
                .message("Product removed from watchlist successfully")
                .build();
    }
    
}
