package br.com.altamira.security.oauth2.rest;

import java.net.URISyntaxException;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.altamira.security.oauth2.controller.AccessTokenController;
import br.com.altamira.security.oauth2.model.AccessToken;

/**
 *
 * @author 
 */
@Path("/authz")
public class AuthzEndpoint extends BaseEndpoint<AccessToken> {
	
	/**
	 *
	 */
	@EJB
    private AccessTokenController accessTokenController;

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
			@Size(min = 2) @QueryParam("token") String token)
					throws URISyntaxException, OAuthSystemException, JsonProcessingException {
		System.out.println(token);
		//UserController userController = new UserController();
		AccessToken accessToken = accessTokenController.findByToken(token);
		return createOkResponse(accessToken).build();
	}
}
