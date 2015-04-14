package br.com.altamira.security.oauth2.rest;

import br.com.altamira.security.oauth2.controller.AccessTokenController;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import javax.ejb.EJB;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class PermissionInterceptor implements ContainerRequestFilter {

    /**
     *
     */
    @EJB
    private AccessTokenController accessTokenController;

    /**
     *
     */
    private static final String PERMISSION_GET = "READ";

    /**
     *
     */
    private static final String PERMISSION_POST = "CREATE";

    /**
     *
     */
    private static final String PERMISSION_PUT = "UPDATE";

    /**
     *
     */
    private static final String PERMISSION_DELETE = "DELETE";

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String token;

        if (requestContext.getUriInfo().getQueryParameters().get("token") != null) {
            token = requestContext.getUriInfo().getQueryParameters().get("token").get(0);
        } else {
            token = "00000000000000000000000000000000";
        }
        System.out.println(token);

        Method method = resourceInfo.getResourceMethod();

        StringBuilder resource = new StringBuilder();

        // Retrieve resource from Resource Annontation
        resource.append(resourceInfo.getResourceClass().getAnnotation(Resource.class).name());

        // Retrieve class' @Path value
        if (resource.length() == 0) {
            if (resourceInfo.getResourceClass().isAnnotationPresent(Path.class)) {
                resource.append(resourceInfo.getResourceClass().getAnnotation(Path.class).value());
            }

            // Retrieve method's @Path value
            if (method.isAnnotationPresent(Path.class)) {
                String methodPath = method.getAnnotation(Path.class).value();

                if (methodPath.startsWith("/")) {
                    resource.append(methodPath);
                } else {
                    resource.append("/").append(methodPath);
                }
            }
        }

        // To opt out for Endpoint class-name as resource, use below:
        if (resource.length() == 0) {
            resource.append(resourceInfo.getResourceClass().getName());
        }

        if (resource.toString().equals("AUTHZ") || resource.toString().equals("TOKEN")) {
            return;
        }
        
        // Retrieve permission type
        String permission = null;

        if (method.isAnnotationPresent(Permission.class)) {
            permission = method.getAnnotation(Permission.class).name();
        } else if (method.isAnnotationPresent(GET.class)) {
            permission = PERMISSION_GET;
        } else if (method.isAnnotationPresent(POST.class)) {
            permission = PERMISSION_POST;
        } else if (method.isAnnotationPresent(PUT.class)) {
            permission = PERMISSION_PUT;
        } else if (method.isAnnotationPresent(DELETE.class)) {
            permission = PERMISSION_DELETE;
        }
        
        Response authResponse = accessTokenController.checkPermission(token, resource.toString(), permission);

        if (authResponse.getStatus() != 200) {
            String message = authResponse.readEntity(HashMap.class).get("message").toString();
            requestContext.abortWith(Response.status(authResponse.getStatus()).entity(message).build());
        }

        /*} else {
         Response response = Response.status(Response.Status.UNAUTHORIZED).entity("Token required").build();
         requestContext.abortWith(response);
         }*/
    }

}
