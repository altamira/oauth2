package br.com.altamira.security.oauth2.rest;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.HashMap;

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

    	AccessToken accessToken = accessTokenController.findByToken(token);
    	
    	// Create a hash map
    	HashMap<String, Serializable> responseData = new HashMap<String, Serializable>();
    	// Put elements to the map
    	responseData.put("access_token", accessToken.getAccessToken());
    	responseData.put("user_name", accessToken.getUser().getUser());
    	responseData.put("first_name", accessToken.getUser().getFirstName());
    	responseData.put("last_name", accessToken.getUser().getLastName());
    	responseData.put("loggedin_since", accessToken.getCreated());
    	
    	return createOkResponse(responseData).build();
    }
}
