package com.bfs.hibernateprojectdemo.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.bfs.hibernateprojectdemo.domain.Product;

@Repository
public class ProductDao extends AbstractHibernateDao<Product>{
	public ProductDao() {
		setClazz(Product.class);
	}
	// Find product by ID
	public Optional<Product> findProductById(Integer productId) {
	    return Optional.ofNullable(findById(productId));
	}


    // Add a new product (for seller)
    public void addProduct(Product product) {
        add(product);
    }

    // Update product details (for seller)
    public void updateProduct(Product product) {
        update(product);
    }
    
    //get all products
    public List<Product> getAllProducts() {
        return this.getAll();
    }
    
    //gell all products in stock 
    public List<Product> getAllInStockProducts(){
    	CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
    	CriteriaQuery<Product> query = builder.createQuery(Product.class);
    	Root<Product> root = query.from(Product.class);
    	query.select(root).where(builder.gt(root.get("stockQuantity"), 0));
    	return getCurrentSession().createQuery(query).getResultList();
    }
    
    public List<Product> getTopProfitableProducts(int limit) {
        String hql = "SELECT oi.product FROM OrderItem oi " +
                     "JOIN oi.order o " +
                     "WHERE o.status = 'COMPLETED' " +
                     "GROUP BY oi.product.id " +
                     "ORDER BY SUM((oi.priceAtPurchase - oi.product.wholesalePrice) * oi.quantity) DESC";

        TypedQuery<Product> query = getCurrentSession().createQuery(hql, Product.class);
        query.setMaxResults(limit);

        return query.getResultList();
    }
    
    
    public List<Product> getTopPopularProducts(int limit) {
        String hql = "SELECT oi.product FROM OrderItem oi " +
                     "JOIN oi.order o " +
                     "WHERE o.status = 'COMPLETED' " +
                     "GROUP BY oi.product.id " +
                     "ORDER BY SUM(oi.quantity) DESC";

        TypedQuery<Product> query = getCurrentSession().createQuery(hql, Product.class);
        query.setMaxResults(limit);

        return query.getResultList();
    }
    
    
    
    
}

