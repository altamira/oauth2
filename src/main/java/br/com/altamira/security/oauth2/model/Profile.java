package br.com.altamira.security.oauth2.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 *
 * @author 
 */
@Entity
@Table(name = "SS_PROFILE")
public class Profile extends Resource {
	
	/**
	 * Serial number ID
	 */
	private static final long serialVersionUID = -3725014293364656727L;

	@NotNull
	@Size(max = 50)
	@Column(name = "NAME")
	private String name = "";
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "profiles")
	private List<User> users = new ArrayList<User>();
	
	/**
	 *
	 */
	public Profile() {

	}

	/**
	 *
	 * @param number
	 * @param customer
	 */
	public Profile(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	

}
