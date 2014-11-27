package br.com.altamira.security.oauth2.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.altamira.security.oauth2.model.AccessToken;
import br.com.altamira.security.oauth2.model.Permission;
import br.com.altamira.security.oauth2.model.Profile;
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
       
       q.select(cb.construct(AccessToken.class,
				entity.get("id"),
				entity.get("accessToken"),
				entity.get("created"),
				entity.get("user")));
       q.select(entity).where(cb.equal(entity.get("accessToken"), token));
       AccessToken accessToken = entityManager.createQuery(q).getSingleResult();
       accessToken.getUser().getProfiles().size();
       accessToken.getUser().getAccessTokens().size();
       return accessToken;
   }

   /**
    *
    * @param String
    * @param String
    * @param String
    * @return Response
    */
   public Response checkPermission(String token, String resource, String permission) 
		   throws ConstraintViolationException, NoResultException  {

	   AccessToken accessToken;
	   HashMap<String, Serializable> responseData = new HashMap<String, Serializable>();

	   try {
		   accessToken = this.findByToken(token);
	   } catch (Exception e) {
		   e.printStackTrace();
		   responseData.put("message", "Invalid Token: " + token);
		   return Response.status(Response.Status.UNAUTHORIZED).entity(responseData).type(MediaType.APPLICATION_JSON).build();
	   }
	   User user = accessToken.getUser();
	   List<Profile> profiles = user.getProfiles();
	   for (Profile p: profiles) {  
		   if ( p.getPermission().getResourceName().equals(resource) ) {
			   if (p.getPermission().getPermission().contains(permission) ) {
				   responseData.put("message", "Authorized");
				   return Response.status(Response.Status.OK).entity(responseData).type(MediaType.APPLICATION_JSON).build();
			   }
		   }
	   }
	   responseData.put("message", "Unauthorized resource or permission");
	   return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity(responseData).type(MediaType.APPLICATION_JSON).build();
   }
}
