package br.com.altamira.security.oauth2.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.altamira.security.oauth2.controller.AccessTokenController;
import br.com.altamira.security.oauth2.controller.UserController;
import br.com.altamira.security.oauth2.model.User;
import br.com.altamira.security.oauth2.util.Database;

/**
 *
 * @author 
 */
@Path("/authz")
public class AuthzEndpoint extends BaseEndpoint<User> {
	
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
    public AuthzEndpoint() {
    	this.type = AuthzEndpoint.class;
    }

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response authorize(
			@Context HttpServletRequest request,
			@Size(min = 2) @QueryParam("username") String userName,
			@Size(min = 2) @QueryParam("password") String password)
					throws URISyntaxException, OAuthSystemException, JsonProcessingException {
		System.out.println(userName);
		System.out.println(password);
		//UserController userController = new UserController();
		User user = userController.findUserByUsernamePassword(userName, password);

		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
		final String accessToken = oauthIssuerImpl.accessToken();
		System.out.println(accessToken);
		return createOkResponse(accessTokenController.create(user, accessToken)).build();
	}
}
