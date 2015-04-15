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

import br.com.altamira.data.model.security.Permission;

@Stateless
public class PermissionController extends BaseController<Permission> {

	public PermissionController() {
		this.type = Permission.class;
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
	public List<Permission> list(
			@Min(value = 0, message = START_PAGE_VALIDATION) int startPage,
			@Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize)
					throws ConstraintViolationException, NoResultException {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Permission> q = cb.createQuery(type);
		Root<Permission> entity = q.from(type);

		q.select(cb.construct(type,
				entity.get("id"),
				entity.get("resourceName"),
				entity.get("permission")));

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
	public Permission find(
			@Min(value = 1, message = ID_NOT_NULL_VALIDATION) long id)
					throws ConstraintViolationException, NoResultException {

		Permission permission = super.find(id);
		//permission.getProfiles().size();
		return permission;
	}

	/**
	 *
	 * @param entity
	 * @return
	 */
	@Override
	public Permission create(
			@NotNull(message = ENTITY_VALIDATION) Permission entity)
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
	public Permission update(
			@NotNull(message = ENTITY_VALIDATION) Permission entity)
					throws ConstraintViolationException, IllegalArgumentException {

		// Resolve dependencies


		return super.update(entity);
	}

}
