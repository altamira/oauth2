package br.com.altamira.security.oauth2.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 *
 * @author 
 */
@Entity
@Table(name = "SS_ACCESS_TOKEN")
public class AccessToken extends br.com.altamira.security.oauth2.model.Entity {

	/**
	 * Serial number ID
	 */
	private static final long serialVersionUID = -3725014293364656727L;

	@NotNull
	@Size(min = 3)
	@Column(name = "ACCESS_TOKEN")
	private String accessToken = "";

	@NotNull
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
	private Date created = new Date();

	@JsonIgnore
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User user;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
