package br.com.altamira.security.oauth2.controller;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
import br.com.altamira.security.oauth2.util.Common;


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

		User user = super.find(id);

		// Lazy load of tokens
		user.getAccessTokens().size();
		user.getProfiles().size();
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

	public User getUserFromUsername(String userName) throws MessagingException {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> q = cb.createQuery(User.class);
		Root<User> entity = q.from(User.class);

		q.select(entity).where(cb.equal(entity.get("user"), userName));
		User user = entityManager.createQuery(q).getSingleResult();
		// Lazy load of tokens
		user.getAccessTokens().size();
		user.getProfiles().size();
		System.out.println(user.getEmail());
		this.sendForgotPasswordEmail(user.getEmail(), user.getPassword());
		return user;
	}

	private void sendForgotPasswordEmail (String toEmail, String password) throws MessagingException{

		String smtpUser = Common.SMTP_USER;
		String smtpPassword = Common.SMTP_PASSWORD;

		String emailText = "Hey," + "\n\n" +
				"Your password is: " + password + "\n\n" +
				"Thanks";

		Properties props = new Properties();
		props.put("mail.smtp.host", Common.SMTP_HOST);
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", Common.SMTP_AUTH);
		props.put("mail.smtp.port", Common.SMTP_PORT);

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(smtpUser, smtpPassword);
			}
		});


		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(Common.FROM_EMAIL));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(toEmail));
		message.setSubject("Forgot Password Request");
		message.setText(emailText);

		Transport.send(message);
	}
}
