package com.oracle.interview.resources;

import com.oracle.interview.auth.SimpleDatabaseAuthenticator;
import com.oracle.interview.db.entity.User;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.*;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Optional;


/**
 * Quick and dirty PoC for basic auth.
 * is based on Basic auth
 */
@Path("/auth")
@Api(value = "LoginController")
public class LoginController {


    public LoginController() {

    }


    @ApiOperation(value = "Simple Login, tht uses basic auth", response = Iterable.class, tags = "LoginController")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED"),
            @ApiResponse(code = 500, message = "In case of error")
    })
    @GET
    @PermitAll
    public Response simpleBasicLogin(@ApiParam(name = "user", hidden = true) @Auth User user) {
        return Response.ok().entity(user.getUsername()).build();
    }


    @ApiOperation(value = "Get logged username", response = Iterable.class, tags = "LoginController")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED"),
            @ApiResponse(code = 500, message = "In case of error")
    })
    @GET
    @Path("/username")
    public Response loggedUserName(@ApiParam(name = "user", hidden = true) @Auth Optional<User> user) {
        return Response.ok().entity(user.orElse(SimpleDatabaseAuthenticator.GUEST_USER).getUsername()).build();
    }

    @ApiOperation(value = "Logout", response = Iterable.class, tags = "LoginController")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED"),
    })
    @GET
    @Path("/logout")
    public Response logout(@ApiParam(name = "user", hidden = true) @Auth User user) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }


}
