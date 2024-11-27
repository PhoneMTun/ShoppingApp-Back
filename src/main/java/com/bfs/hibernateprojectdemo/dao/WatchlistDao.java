package com.bfs.hibernateprojectdemo.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.bfs.hibernateprojectdemo.domain.Product;
import com.bfs.hibernateprojectdemo.domain.Watchlist;

@Repository
public class WatchlistDao extends AbstractHibernateDao<Watchlist>{
	
	public WatchlistDao() {
		setClazz(Watchlist.class);
	}
	
	public List<Product> getAllWatchlistProductsByUserId(Integer userId){
		CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Product> query = builder.createQuery(Product.class);
		Root<Watchlist>root = query.from(Watchlist.class);
		Join<Watchlist, Product> productJoin = root.join("product");
        query.select(productJoin).where(builder.equal(root.get("user").get("id"), userId));
        return getCurrentSession().createQuery(query).getResultList();
    }
	
	public Optional<Watchlist> findWatchlistByUserAndProduct(Integer userId, Integer productId) {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Watchlist> query = builder.createQuery(Watchlist.class);
        Root<Watchlist> root = query.from(Watchlist.class);
        query.select(root).where(
                builder.equal(root.get("user").get("id"), userId),
                builder.equal(root.get("product").get("id"), productId)
        );
        return getCurrentSession().createQuery(query).uniqueResultOptional();
    }

	public void removeProductFromWatchlist(Integer userId, Integer productId) {
	    String hql = "DELETE FROM Watchlist w WHERE w.user.id = :userId AND w.product.id = :productId";
	    getCurrentSession().createQuery(hql)
	        .setParameter("userId", userId)
	        .setParameter("productId", productId)
	        .executeUpdate();
	}

	public Optional<Watchlist> findByUserIdAndProductId(Integer userId, Integer productId) {
	    String hql = "FROM Watchlist w WHERE w.user.id = :userId AND w.product.id = :productId";
	    return getCurrentSession().createQuery(hql, Watchlist.class)
	            .setParameter("userId", userId)
	            .setParameter("productId", productId)
	            .uniqueResultOptional();
	}





	

}
