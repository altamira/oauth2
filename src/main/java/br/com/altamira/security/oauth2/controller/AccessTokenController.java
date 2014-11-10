package br.com.altamira.security.oauth2.controller;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
		entity.setAccess_token(token);
		entity.setUser(user);
		return super.create(entity);

	}
}
