package com.bfs.hibernateprojectdemo.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.bfs.hibernateprojectdemo.domain.Order;
import com.bfs.hibernateprojectdemo.domain.Product;

@Repository
public class OrderDao extends AbstractHibernateDao<Order> {

		public OrderDao() {
			setClazz(Order.class);
		}
		
		//find order by id
		public Order findOrderById(int orderId) {
			return findById(orderId);
		}
		
		//create a new order
		public void createOrder(Order order) {
			add(order);
		}
		
		//update order status
		public void updateOrderStatus(Order order) {
			update(order);
		}
		
		//find all orders using Critria
		public List<Order> findAllOrders() {
			CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
	        CriteriaQuery<Order> query = builder.createQuery(Order.class);
	        Root<Order> root = query.from(Order.class);
	        query.select(root);
	        return getCurrentSession().createQuery(query).getResultList(); 
	}
		public List<Order> findOrdersByUserId(int userId) {
		    CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
		    CriteriaQuery<Order> query = builder.createQuery(Order.class);
		    Root<Order> root = query.from(Order.class);
		    query.select(root).where(builder.equal(root.get("user").get("id"), userId));
		    return getCurrentSession().createQuery(query).getResultList();
		}

		
		public List<Product> getMostFrequentlyPurchasedProducts(Integer userId, int limit) {
	        String hql = "SELECT oi.product FROM OrderItem oi " +
	                     "JOIN oi.order o " +
	                     "WHERE o.user.id = :userId AND o.status != 'CANCELED' " +
	                     "GROUP BY oi.product.id " +
	                     "ORDER BY COUNT(oi.product.id) DESC, oi.product.id ASC";

	        TypedQuery<Product> query = getCurrentSession().createQuery(hql, Product.class);
	        query.setParameter("userId", userId);
	        query.setMaxResults(limit);

	        return query.getResultList();
	    }

	    
		public List<Product> getMostRecentlyPurchasedProducts(Integer userId, int limit) {
		    String hql = "SELECT oi.product FROM OrderItem oi " +
		                 "JOIN oi.order o " +
		                 "WHERE o.user.id = :userId AND o.status != 'CANCELED' " +
		                 "GROUP BY oi.product.id " +
		                 "ORDER BY MAX(o.orderTime) DESC";

		    TypedQuery<Product> query = getCurrentSession().createQuery(hql, Product.class);
		    query.setParameter("userId", userId);
		    query.setMaxResults(limit);

		    return query.getResultList();
		}



		
}
