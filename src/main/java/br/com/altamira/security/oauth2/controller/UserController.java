package br.com.altamira.security.oauth2.controller;

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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<User> list(
			@Min(value = 0, message = START_PAGE_VALIDATION) int startPage,
			@Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize)
					throws ConstraintViolationException, NoResultException {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> q = cb.createQuery(type);
		Root<User> entity = q.from(type);

		q.select(cb.construct(type,
				entity.get("id"),
				entity.get("user")));

		q.orderBy(cb.desc(entity.get("lastModified")));

		return entityManager.createQuery(q)
				.setFirstResult(startPage * pageSize)
				.setMaxResults(pageSize)
				.getResultList();

	}

	/**
	 *
	 * @param String
	 * @param String
	 * @return
	 */
	public User findUserByUsernamePassword(String userName, String password)
			throws ConstraintViolationException, NoResultException {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<User> q = cb.createQuery(User.class);

		Root<User> entity = q.from(User.class);

		q.select(entity).where(cb.and(
				cb.equal(entity.get("user"), userName),
				cb.equal(entity.get("password"), password)));

		User user = entityManager.createQuery(q).getSingleResult();

		return user;
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	@Override
	public User find(
			@Min(value = 1, message = ID_NOT_NULL_VALIDATION) long id)
					throws ConstraintViolationException, NoResultException {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<User> q = cb.createQuery(User.class);

		Root<User> entity = q.from(User.class);

		q.multiselect(
				entity.get("id"),
				entity.get("user"),
				entity.get("password"),
				entity.get("firstName"), 
				entity.get("lastName")).
					where(cb.equal(entity.get("id"), id));

		User user = entityManager.createQuery(q).getSingleResult();
		return user;
	}

	/**
	 *
	 * @param entity
	 * @return
	 */
	@Override
	public User create(
			@NotNull(message = ENTITY_VALIDATION) User entity)
					throws ConstraintViolationException {

		// Resolve dependencies
		entity.getAccessTokens().stream().forEach((r) -> {
			r.setUser(entity);
		});
		
//		entity.getProfiles().stream().forEach((r) -> {
//			r.getUsers().add(entity);
//		});
		
		return super.create(entity);
	}

	/**
	 *
	 * @param entity
	 * @return
	 */
	@Override
	public User update(
			@NotNull(message = ENTITY_VALIDATION) User entity)
					throws ConstraintViolationException, IllegalArgumentException {

		// Resolve dependencies
		entity.getAccessTokens().stream().forEach((r) -> {
			r.setUser(entity);
		});

		return super.update(entity);
	}
}
