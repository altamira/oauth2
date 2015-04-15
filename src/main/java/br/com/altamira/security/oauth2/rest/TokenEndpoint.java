package br.com.altamira.security.oauth2.rest;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.NoResultException;
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
import br.com.altamira.data.model.security.User;

/**
 *
 *
 *
 */
@Resource(name = "TOKEN")
@Path("/token")
public class TokenEndpoint extends BaseEndpoint<User> {

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
    @Permission(name = "REQUEST_TOKEN")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getToken(@NotNull(message = ENTITY_VALIDATION) User entity)
            throws URISyntaxException, OAuthSystemException, JsonProcessingException {

        User user = null;
        // hash map for response string
        HashMap<String, Serializable> responseData = new HashMap<>();

        //responseData.put("accessToken", accessToken.getAccessToken());
        try {
            user = userController.findByUsernamePassword(entity.getUser(), entity.getPassword());

        } catch (NoResultException e) {
            e.printStackTrace();
            responseData.put("message", "Invalid Username or Password");
            return Response.status(Response.Status.UNAUTHORIZED).entity(responseData).type(MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            e.printStackTrace();
            responseData.put("message", "Invalid Username or Password");
            return Response.status(Response.Status.UNAUTHORIZED).entity(responseData).type(MediaType.APPLICATION_JSON).build();
        }
        if (user == null) {
            //responseData.put("accessToken", accessToken.getAccessToken());
            try {
                user = userController.findByEmailPassword(entity.getUser(), entity.getPassword());

            } catch (NoResultException e) {
                e.printStackTrace();
                responseData.put("message", "Invalid Email or Password");
                return Response.status(Response.Status.UNAUTHORIZED).entity(responseData).type(MediaType.APPLICATION_JSON).build();

            } catch (Exception e) {
                e.printStackTrace();
                responseData.put("message", "Internal Server Error");
                return Response.status(Response.Status.UNAUTHORIZED).entity(responseData).type(MediaType.APPLICATION_JSON).build();
            }
        }

        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        final String accessToken = oauthIssuerImpl.accessToken();

        return createOkResponse(accessTokenController.create(user, accessToken)).build();
    }

}
