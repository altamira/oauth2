package br.com.altamira.security.oauth2.rest;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.altamira.security.oauth2.controller.AccessTokenController;
import br.com.altamira.data.model.security.AccessToken;

/**
 *
 * @author
 */
@Resource(name = "AUTHZ")
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
    @Path("token")
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
        responseData.put("userId", accessToken.getUser().getId());
        responseData.put("userName", accessToken.getUser().getUser());
        responseData.put("firstName", accessToken.getUser().getFirstName());
        responseData.put("lastName", accessToken.getUser().getLastName());
        responseData.put("email", accessToken.getUser().getEmail());
        responseData.put("theme", accessToken.getUser().getTheme());
        responseData.put("loggedinSince", accessToken.getCreated());

        return createOkResponse(responseData).build();
    }

    @GET
    @Path("permission")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorizeRights(
            @Context HttpServletRequest request,
            @Size(min = 2) @QueryParam("token") String token,
            @Size(min = 2) @QueryParam("resource") String resource,
            @Size(min = 2) @QueryParam("permission") String permission)
            throws URISyntaxException, OAuthSystemException, JsonProcessingException {

        return accessTokenController.checkPermission(token, resource, permission);
    }

    //ALTAMIRA-182: Data API - add validation token for each request in RESTful Data API
    @POST
    @Path("permission")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorizeRights(
            @Context HttpServletRequest request,
            @Size(min = 2) @QueryParam("token") String token,
            @Size(min = 2) @QueryParam("permission") String permission,
            @NotNull HashMap<String, String> map)
            throws URISyntaxException, OAuthSystemException, JsonProcessingException {

        String resource = map.get("resource");

        if (resource == null) {
            HashMap<String, String> message = new HashMap<>();
            message.put("message", "Specify resource name");
            Response response = Response.status(Response.Status.UNAUTHORIZED).entity(message).type(MediaType.APPLICATION_JSON).build();
            return response;
        }

        return accessTokenController.checkPermission(token, resource, permission);
    }
}
