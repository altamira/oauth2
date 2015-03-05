package br.com.altamira.security.oauth2.rest;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.mail.MessagingException;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilderException;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.altamira.security.oauth2.controller.UserController;
import br.com.altamira.security.oauth2.model.User;
import java.util.Map;
import javax.ejb.EJBException;
import javax.persistence.NoResultException;

/**
 *
 *
 * @author
 */
@Path("user")
@RequestScoped
public class UserEndPoint extends BaseEndpoint<User> {

    @EJB
    private UserController userController;

    public UserEndPoint() {
        this.type = UserEndPoint.class;
    }

    /**
     *
     * @param startPosition
     * @param maxResult
     * @return
     * @throws IOException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(
            @DefaultValue("0") @QueryParam("start") Integer startPosition,
            @DefaultValue("10") @QueryParam("max") Integer maxResult)
            throws IOException {

        return createOkResponse(
                userController.list(startPosition, maxResult)).build();
    }

    /**
     *
     * @param id
     * @return
     * @throws JsonProcessingException
     */
    @GET
    @Path("/{id:[0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(
            @Min(value = 1, message = ID_VALIDATION) @PathParam(value = "id") long id)
            throws JsonProcessingException, NoResultException {

        return createOkResponse(
                userController.find(id)).build();
    }

    /**
     *
     * @param entity
     * @return
     * @throws IllegalArgumentException
     * @throws UriBuilderException
     * @throws JsonProcessingException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(
            @NotNull(message = ENTITY_VALIDATION) User entity)
            throws IllegalArgumentException, UriBuilderException, JsonProcessingException {

        return createCreatedResponse(userController.create(entity)).build();
    }

    /**
     *
     * @param id
     * @param entity
     * @return
     * @throws JsonProcessingException
     */
    @PUT
    @Path("/{id:[0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @Min(value = 1, message = ID_VALIDATION) @PathParam("id") long id,
            @NotNull(message = ENTITY_VALIDATION) User entity)
            throws JsonProcessingException {

        return createOkResponse(
                userController.update(entity)).build();
    }

    /**
     *
     * @param id
     * @return
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @DELETE
    @Path("/{id:[0-9]*}")
    public Response delete(
            @Min(value = 1, message = ID_VALIDATION) @PathParam("id") long id)
            throws JsonProcessingException {

        userController.remove(id);

        return createNoContentResponse().build();
    }

    /**
     * @param parameters
     * @return Response
     * @throws IllegalArgumentException
     * @throws javax.mail.MessagingException
     * @throws JsonProcessingException
     * @throws ConstraintViolationException
     */
    @POST
    @Path("/forgot-password/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response forgotPassword(@NotNull Map<String, String> parameters)
            throws NoResultException, MessagingException, ConstraintViolationException, JsonProcessingException, IllegalArgumentException {

        User user = null;

        // Create a hash map for response data
        HashMap<String, Serializable> responseData = new HashMap<>();

        try {
            user = userController.forgotPasswordRequest(parameters);
        } catch (EJBException ejb) {
            if (ejb.getCause() instanceof NoResultException) {
                responseData.put("message", "Usuário não encontrado.");
                return Response.status(Response.Status.NOT_FOUND).entity(responseData).type(MediaType.APPLICATION_JSON).build();

            } else if (ejb.getCause() instanceof IllegalArgumentException) {
                responseData.put("message", ejb.getCause().getMessage());
                return Response.status(Response.Status.BAD_REQUEST).entity(responseData).type(MediaType.APPLICATION_JSON).build();

            } else if (ejb.getCause() instanceof MessagingException) {
                responseData.put("message", ejb.getCause().getMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseData).type(MediaType.APPLICATION_JSON).build();
            }
        } catch (Exception e) {
            responseData.put("message", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseData).type(MediaType.APPLICATION_JSON).build();

        }

        responseData.put("message", "Senha enviada para o email " + user.getEmail());
        return createOkResponse(responseData).build();
    }

}
