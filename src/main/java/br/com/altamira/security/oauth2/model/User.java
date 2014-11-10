package br.com.altamira.security.oauth2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
*
* @author 
*/
@Entity
@Table(name = "SS_USER")

public class User extends Resource {
	
	/**
	* Serial number ID
	*/
	private static final long serialVersionUID = -3725014293364656727L;
	
	@NotNull
	@Size(min = 3)
	@Column(name = "NAME")
	private String name = "";
	
	@NotNull
	@Size(min = 3)
	@Column(name = "USER")
	private String user = "";
	
	@NotNull
	@Size(min = 3)
	@Column(name = "PASSWORD")
	private String password = "";

    /**
     *
     */
    public User() {
        
    }
	
	/**
    *
    * @param number
    * @param customer
    */
   public User(Long id, String user) {
       this.id = id;
       this.user = user;
   }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}
