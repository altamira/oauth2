package br.com.altamira.security.oauth2.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
	@Size(min = 1)
	@Column(name = "FIRST_NAME")
	private String firstName = "";
	
	@NotNull
	@Size(min = 1)
	@Column(name = "LAST_NAME")
	private String lastName = "";
	
	@NotNull
	@Size(min = 3)
	@Column(name = "USER")
	private String user = "";
	
	@NotNull
	@Size(min = 3)
	@Column(name = "PASSWORD")
	private String password = "";
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<AccessToken> accessTokens;

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

	public List<AccessToken> getAccessTokens() {
		return accessTokens;
	}

	public void setAccessTokens(List<AccessToken> accessTokens) {
		this.accessTokens = accessTokens;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


}
