package com.bfs.hibernateprojectdemo.domain;

import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    @Column(nullable = false, length = 255)
    private String description;

    @Positive(message = "Wholesale price must be positive")
    @Column(name = "wholesale_price", nullable = false)
    private double wholesalePrice;

    @Positive(message = "Retail price must be positive")
    @Column(name = "retail_price", nullable = false)
    private double retailPrice;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @NotBlank(message = "Image URL is required")
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid URL starting with http or https")
    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
