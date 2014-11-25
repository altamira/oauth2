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

import br.com.altamira.security.oauth2.model.Profile;

@Stateless
public class ProfileController extends BaseController<Profile> {

	public ProfileController() {
		this.type = Profile.class;
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
	 * @param startPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<Profile> list(
			@Min(value = 0, message = START_PAGE_VALIDATION) int startPage,
			@Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize)
					throws ConstraintViolationException, NoResultException {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Profile> q = cb.createQuery(type);
		Root<Profile> entity = q.from(type);

		q.select(cb.construct(type,
				entity.get("id"),
				entity.get("name")));

		q.orderBy(cb.desc(entity.get("lastModified")));

		return entityManager.createQuery(q)
				.setFirstResult(startPage * pageSize)
				.setMaxResults(pageSize)
				.getResultList();

	}

	/**
	 *
	 * @param id
	 * @return
	 */
	@Override
	public Profile find(
			@Min(value = 1, message = ID_NOT_NULL_VALIDATION) long id)
					throws ConstraintViolationException, NoResultException {

		Profile profile = super.find(id);
		if(profile.getPermission() != null) {
			System.out.println(profile.getPermission().getPermission());
		}
		
		return profile;
	}

	/**
	 *
	 * @param entity
	 * @return
	 */
	@Override
	public Profile create(
			@NotNull(message = ENTITY_VALIDATION) Profile entity)
					throws ConstraintViolationException {

		// Resolve dependencies
		
		return super.create(entity);
	}

	/**
	 *
	 * @param entity
	 * @return
	 */
	@Override
	public Profile update(
			@NotNull(message = ENTITY_VALIDATION) Profile entity)
					throws ConstraintViolationException, IllegalArgumentException {

		// Resolve dependencies
		

		return super.update(entity);
	}


}
