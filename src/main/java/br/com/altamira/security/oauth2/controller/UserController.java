package br.com.altamira.security.oauth2.controller;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.constraints.Min;


import br.com.altamira.security.oauth2.model.User;


@Stateless
public class UserController extends BaseController<User>{
	
	public UserController() {
		this.type = User.class;
	}
	/**
    *
    */
   @Inject
   protected Logger log;

   /**
    *
    */
   @Inject
   protected EntityManager entityManager;

   /**
    *
    */
   @Inject
   protected Validator validator;
	
	private static final String USERNAME_VALIDATION = "Invalid Username";
	private static final String PASSWORD_VALIDATION = "Invalid password";

	/**
    *
    * @param String
    * @param String
    * @return
    */
   public User findUserByUsernamePassword(String userName, String password)
           throws ConstraintViolationException, NoResultException {
       
       System.out.println(userName);
       System.out.println(password);
		
       CriteriaBuilder cb = entityManager.getCriteriaBuilder();
       System.out.println(userName);
       CriteriaQuery<User> q = cb.createQuery(User.class);
       System.out.println(userName);
       Root<User> entity = q.from(User.class);
       
       System.out.println(userName);
       System.out.println(password);
		
       q.select(cb.construct(User.class,
               entity.get("id"),
               entity.get("name")));

       q.select(entity).where(cb.and(
    		   				  cb.equal(entity.get("user"), userName),
    		   				  cb.equal(entity.get("password"), password)));

       User user = entityManager.createQuery(q).getSingleResult();

       return user;
   }
}
