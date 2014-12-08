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
    
    @Path("token")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorize(
    		@Context HttpServletRequest request,
    		@Size(min = 2) @QueryParam("token") String token)
    				throws URISyntaxException, OAuthSystemException, JsonProcessingException {

    	AccessToken accessToken;
    	// Create a hash map for response data
    	HashMap<String, Serializable> responseData = new HashMap<String, Serializable>();
    	
    	try {
    		accessToken = accessTokenController.findByToken(token);
    	} catch (Exception e) {
    		e.printStackTrace();
    		responseData.put("message", "Invalid Token: " + token);
    		return Response.status(Response.Status.UNAUTHORIZED).entity(responseData).type(MediaType.APPLICATION_JSON).build();
    	}
    	
    	// Put elements to the map
    	responseData.put("accessToken", accessToken.getAccessToken());
    	responseData.put("userName", accessToken.getUser().getUser());
    	responseData.put("firstName", accessToken.getUser().getFirstName());
    	responseData.put("lastName", accessToken.getUser().getLastName());
        responseData.put("email", accessToken.getUser().getEmail());
        responseData.put("theme", accessToken.getUser().getTheme());
    	responseData.put("loggedinSince", accessToken.getCreated());
    	
    	return createOkResponse(responseData).build();
    }
    
    @Path("permission")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorizeRights(
    		@Context HttpServletRequest request,
    		@Size(min = 2) @QueryParam("token") String token,
    		@Size(min = 2) @QueryParam("resource") String resource,
    		@Size(min = 2) @QueryParam("permission") String permission)
    				throws URISyntaxException, OAuthSystemException, JsonProcessingException {

    	return accessTokenController.checkPermission(token, resource, permission);
    }
}
