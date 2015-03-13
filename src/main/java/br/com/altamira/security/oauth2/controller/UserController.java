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
import br.com.altamira.security.oauth2.model.User_;
import br.com.altamira.security.oauth2.util.Common;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeUtility;

@Stateless
public class UserController extends BaseController<User> {

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
        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> entity = q.from(User.class);

        q.select(cb.construct(User.class,
                entity.get(User_.id),
                entity.get(User_.user)));

        //q.orderBy(cb.desc(entity.get("lastModified")));
        return entityManager.createQuery(q)
                .setFirstResult(startPage * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

    }

    /**
     *
     * @param userName
     * @param password
     * @return
     */
    public User findByUsernamePassword(String userName, String password)
            throws ConstraintViolationException, NoResultException {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> q = cb.createQuery(User.class);

        Root<User> entity = q.from(User.class);

        q.select(entity).where(cb.and(
                cb.equal(entity.get(User_.user), userName),
                cb.equal(entity.get(User_.password), password)));

        User user = entityManager.createQuery(q).getSingleResult();

        return user;
    }

    /**
     *
     * @param userName
     * @param password
     * @return
     */
    public User findByEmailPassword(String userName, String password)
            throws ConstraintViolationException, NoResultException {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> q = cb.createQuery(User.class);

        Root<User> entity = q.from(User.class);

        q.select(entity).where(cb.and(
                cb.equal(entity.get(User_.email), userName),
                cb.equal(entity.get(User_.password), password)));

        User user = entityManager.createQuery(q).getSingleResult();

        return user;
    }
    
    /**
     *
     * @param userName
     * @return
     */
    public User findByUsername(String userName)
            throws ConstraintViolationException, NoResultException {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> q = cb.createQuery(User.class);

        Root<User> entity = q.from(User.class);

        q.select(entity).where(cb.equal(entity.get(User_.user), userName));

        User user = entityManager.createQuery(q).getSingleResult();

        return user;
    }

    /**
     *
     * @param email
     * @return
     */
    public User findByEmail(String email)
            throws ConstraintViolationException, NoResultException {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> q = cb.createQuery(User.class);

        Root<User> entity = q.from(User.class);

        q.select(entity).where(cb.equal(entity.get(User_.email), email));

        User user = entityManager.createQuery(q).getSingleResult();

        return user;
    }

    /**
     *
     * @param user
     * @return
     */
    public User find(String user)
            throws ConstraintViolationException, NoResultException {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> root = cb.createQuery(User.class);

        Root<User> entity = root.from(User.class);

        root.select(entity).where(cb.or(
                cb.equal(entity.get(User_.email), user.toLowerCase()),
                cb.equal(entity.get(User_.user), user.toLowerCase())));

        return entityManager.createQuery(root).getSingleResult();
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

    public User forgotPasswordRequest(Map<String, String> parameters) throws MessagingException {
        User user;

        if (parameters.containsKey("user") && !parameters.get("user").isEmpty()) {
            user = this.find(parameters.get("user"));
        } else if (parameters.containsKey("email") && !parameters.get("email").isEmpty()) {
            user = this.findByEmail(parameters.get("email"));
        } else if (parameters.containsKey("username") && !parameters.get("username").isEmpty()) {
            user = this.findByUsername(parameters.get("username"));
        } else {
            throw new IllegalArgumentException("User name or email should be supplied.");
        }

        // Lazy load of tokens
        user.getAccessTokens().size();
        user.getProfiles().size();
        System.out.println(user.getEmail());
        this.sendForgotPasswordEmail(user);
        return user;
    }

    private void sendForgotPasswordEmail(User user) throws MessagingException {

        String smtpUser = Common.SMTP_USER;
        String smtpPassword = Common.SMTP_PASSWORD;

        String emailText = "<html><head></head><body>Olá <b>" + user.getFirstName() + "</b>,<br>"
                + "<p>Em resposta a sua solicitação, segue o reenvio da senha de acesso ao sistema da Altamira:</p>"
                + "<table>"
                + "<tr><td>Seu nome de usuário:</td><td><b>" + user.getUser() + "</b></td></tr>"
                + "<tr><td>Sua senha:</td><td><b>" + user.getPassword() + "</b></td></tr>"
                + "</table></body></html>";

        // Properties for Amazon SES
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.host", Common.SMTP_HOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        //props.put("mail.smtp.starttls.required", "true");
        //props.put("mail.smtp.socketFactory.port", "587");
        //props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //props.put("mail.smtp.ssl.trust", Common.SMTP_HOST);
        //props.put("mail.smtp.ssl.enable", "true");
        //props.put("mail.smtp.auth", Common.SMTP_AUTH);
        //props.put("mail.smtp.auth", "true");
        //Transport transport = session.getTransport();

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(smtpUser, smtpPassword);
                    }
                });

        Transport transport = null;

        try {
            transport = session.getTransport();

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Common.FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail()));
            message.setSubject("Altamira - Reenvio de senha");
            message.setContent(emailText, "text/html; charset=iso-8859-1");

            transport.connect(Common.SMTP_HOST, Common.SMTP_USER, Common.SMTP_PASSWORD);

            message.saveChanges();

            transport.sendMessage(message, message.getAllRecipients());
            
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    //throw new EmailException(e);
                }
            }
        }
    }

}
