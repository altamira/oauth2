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


}
