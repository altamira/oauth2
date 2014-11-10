
package br.com.altamira.security.oauth2.rest;

import java.net.URISyntaxException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.altamira.security.oauth2.controller.AccessTokenController;
import br.com.altamira.security.oauth2.controller.UserController;
import br.com.altamira.security.oauth2.model.User;



/**
 *
 *
 *
 */
@Path("/token")
public class TokenEndpoint extends BaseEndpoint<User>{


	/**
	 *
	 */
	@Inject
	protected AccessTokenController accessTokenController;

	@EJB
	private UserController userController;

	/**
	 *
	 */
	public TokenEndpoint() {
		this.type = TokenEndpoint.class;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getToken( @NotNull(message = ENTITY_VALIDATION) User entity)
					throws URISyntaxException, OAuthSystemException, JsonProcessingException {
		System.out.println(entity.getName());
		System.out.println(entity.getPassword());
		//UserController userController = new UserController();
		User user = userController.findUserByUsernamePassword(entity.getName(), entity.getPassword());

		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
		final String accessToken = oauthIssuerImpl.accessToken();
		System.out.println(accessToken);
		return createOkResponse(accessTokenController.create(user, accessToken)).build();
	}

}