package br.com.altamira.security.oauth2.rest;

import java.io.IOException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;
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

import br.com.altamira.security.oauth2.controller.ProfileController;
import br.com.altamira.security.oauth2.model.Profile;

/**
*
*
* @author 
*/
@Path("profile")
@RequestScoped
public class ProfileEndPoint extends BaseEndpoint<Profile> {
	

	@EJB
	private ProfileController profileController;

	public ProfileEndPoint() {
		this.type = ProfileEndPoint.class;
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
				profileController.list(startPosition, maxResult)).build();
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
				profileController.find(id)).build();
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
			@NotNull(message = ENTITY_VALIDATION) Profile entity)
					throws IllegalArgumentException, UriBuilderException, JsonProcessingException {

		return createCreatedResponse(profileController.create(entity)).build();
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
			@NotNull(message = ENTITY_VALIDATION) Profile entity)
					throws JsonProcessingException {

		return createOkResponse(
				profileController.update(entity)).build();
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

		profileController.remove(id);

		return createNoContentResponse().build();
	}

}
