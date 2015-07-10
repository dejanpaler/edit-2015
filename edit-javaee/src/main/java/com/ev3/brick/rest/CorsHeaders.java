package com.ev3.brick.rest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

@Provider
public class CorsHeaders implements ContainerResponseFilter {

    public static final String ACCESS_CONTROL_ALLOW_HEADERS_KEY = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS_VALUE = "origin,content-type,accept";

    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS_KEY = "Access-Control-Allow-Credentials";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS_VALUE = "true";

    public static final String ACCESS_CONTROL_ALLOW_METHODS_KEY = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOW_METHODS_VALUE = "GET, POST, PUT, DELETE, OPTIONS, HEAD";

    public static final String ACCESS_CONTROL_MAX_AGE_KEY = "Access-Control-Max-Age";
    public static final String ACCESS_CONTROL_MAX_AGE_VALUE = String
            .valueOf(TimeUnit.DAYS.toSeconds(3));

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN_KEY = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN_VALUE = "*";

    @Override
    public void filter(final ContainerRequestContext requestContext,
            final ContainerResponseContext responseContext) throws IOException {

        final MultivaluedMap<String, Object> headers = responseContext
                .getHeaders();
        headers.add(ACCESS_CONTROL_ALLOW_ORIGIN_KEY,
                ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        headers.add(ACCESS_CONTROL_ALLOW_HEADERS_KEY,
                ACCESS_CONTROL_ALLOW_HEADERS_VALUE);
        headers.add(ACCESS_CONTROL_ALLOW_CREDENTIALS_KEY,
                ACCESS_CONTROL_ALLOW_CREDENTIALS_VALUE);
        headers.add(ACCESS_CONTROL_ALLOW_METHODS_KEY,
                ACCESS_CONTROL_ALLOW_METHODS_VALUE);
        headers.add(ACCESS_CONTROL_MAX_AGE_KEY, ACCESS_CONTROL_MAX_AGE_VALUE);
    }
}