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
import br.com.altamira.security.oauth2.model.AccessToken;
import br.com.altamira.security.oauth2.model.User;

@Stateless
public class AccessTokenController extends BaseController<AccessToken>{

	public AccessTokenController() {
		this.type = AccessToken.class;
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

	/**
	 *
	 * @param Long
	 * @param String
	 * @return
	 */
	public AccessToken create (User user, String token)
			throws ConstraintViolationException, NoResultException {

		AccessToken entity = new AccessToken();
		entity.setAccessToken(token);
		entity.setUser(user);
		return super.create(entity);

	}
	
	/**
    *
    * @param String
    * @return
    */
   public AccessToken findByToken(String token) 
           throws ConstraintViolationException, NoResultException  {

       CriteriaBuilder cb = entityManager.getCriteriaBuilder();
       CriteriaQuery<AccessToken> q = cb.createQuery(AccessToken.class);
       Root<AccessToken> entity = q.from(AccessToken.class);
       q.select(entity).where(cb.equal(entity.get("accessToken"), token));
       AccessToken accessToken = entityManager.createQuery(q).getSingleResult();
       
    // Lazy load of user
       if (accessToken.getUser() != null) {
    	   System.out.println("test");
       }
       return accessToken;
   }
}
