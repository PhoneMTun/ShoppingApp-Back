package com.bfs.hibernateprojectdemo.dao;

import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.exception.InvalidCredentialsException;

@Repository
public class UserDao extends AbstractHibernateDao<User>{
	public UserDao() {
		setClazz(User.class);
	}
	
	//register new user
	public void registerUser(User user) {
		add(user);
	}
	
	//find user by email
	public Optional<User> findUserByEmail(String email) {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(root.get("email"), email));
        return Optional.ofNullable(getCurrentSession().createQuery(query).uniqueResult());
    }
	// Find user by username
    public Optional<User> findUserByUsername(String username) {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(root.get("username"), username));
        return Optional.ofNullable(getCurrentSession().createQuery(query).uniqueResult());
    }
    
    /// Authenticate user by username and password (Example using HQL)
    public User authenticateUser(String username, String encryptedPassword) throws InvalidCredentialsException {
        String hql = "FROM User WHERE username = :username AND password = :password";
        User user = getCurrentSession()
                .createQuery(hql, User.class)
                .setParameter("username", username)
                .setParameter("password", encryptedPassword)
                .uniqueResult();
        if (user == null) {
            throw new InvalidCredentialsException("Incorrect credentials, please try again.");
        }
        return user;
    }

    public Optional<User> findUserById(Integer userId) {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(root.get("id"), userId));
        return Optional.ofNullable(getCurrentSession().createQuery(query).uniqueResult());
    }

    public Optional<User> findByUsername(String username) {
        return findUserByUsername(username);
    }
    public Optional<User> findByEmailOptional(String email) {
        return findUserByEmail(email);
    }



}

