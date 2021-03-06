/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.security.oauth2.rest;

//import br.com.altamira.security.oauth2.model.Entity;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import java.lang.reflect.ParameterizedType;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

import br.com.altamira.data.model.Entity;
import br.com.altamira.security.oauth2.util.NullValueSerializer;

/**
 *
 * Base rest services
 *
 * @param <T>
 */
public abstract class BaseEndpoint<T extends Entity> /* implements Endpoint<T> */ {

    /**
     *
     */
    public static final String NOT_FOUND = "Entity not found.";

    /**
     *
     */
    public static final String START_PAGE_VALIDATION = "Invalid start page number, must be greater than 0.";

    /**
     *
     */
    public static final String PAGE_SIZE_VALIDATION = "Invalid page size, must be greater than 0.";

    /**
     *
     */
    public static final String ID_VALIDATION = "Invalid id, must be greater than zero.";

    /**
     *
     */
    public static final String ENTITY_VALIDATION = "Entity can't be null.";

    /**
     *
     */
    public static final String ID_NULL_VALIDATION = "Entity id must be null or zero.";

    /**
     *
     */
    public static final String ID_NOT_NULL_VALIDATION = "Entity id can't be null or zero.";

    /**
     *
     */
    @Inject
    protected Logger log;

    /**
     *
     */
    @Context
    protected HttpHeaders headers;

    /**
     *
     */
    protected Class<? extends BaseEndpoint<T>> type;

    /**
     *
     * @param entity
     * @return
     * @throws JsonProcessingException
     */
    protected Response.ResponseBuilder createOkResponse(Object entity)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new Hibernate4Module());
        mapper.getSerializerProvider().setNullValueSerializer(
                new NullValueSerializer());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        // ObjectWriter writer =
        // mapper.writerWithView(JSonViews.EntityView.class);

        ResponseBuilder responseBuilder;

        // if (type.isInstance(entity)) {
        // responseBuilder = Response.ok(
        // UriBuilder.fromResource(type)
        // .path(String.valueOf(((T) entity).getId())));
        // } else {
        // responseBuilder = Response.ok(UriBuilder.fromResource(type));
        // }
        responseBuilder = Response.ok();

        responseBuilder.entity(mapper.writeValueAsString(entity));

        return responseBuilder;
    }

    /**
     *
     * @param entity
     * @return
     * @throws JsonProcessingException
     */
    protected Response.ResponseBuilder createCreatedResponse(T entity)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new Hibernate4Module());
        mapper.getSerializerProvider().setNullValueSerializer(
                new NullValueSerializer());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        ResponseBuilder responseBuilder = Response.created(
                UriBuilder.fromPath("/")
                //UriBuilder.fromMethod(type, "create")
                .path(String.valueOf(entity.getId())).build())
                //                UriBuilder.fromResource(type)
                //                .path(String.valueOf(entity.getId())).build())
                .entity(mapper.writeValueAsString(entity));

        return responseBuilder;
    }

    /**
     *
     * @return @throws JsonProcessingException
     */
    protected Response.ResponseBuilder createNoContentResponse()
            throws JsonProcessingException {

        ResponseBuilder responseBuilder = Response.noContent();

        return responseBuilder;
    }

    /**
     *
     * @return
     */
    protected Class<?> getTypeClass() {
        Class<?> clazz = (Class<?>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        return clazz;
    }
}
