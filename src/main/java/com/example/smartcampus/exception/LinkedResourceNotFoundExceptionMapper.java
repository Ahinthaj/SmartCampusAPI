/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.smartcampus.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {

        ErrorMessage errorMessage = new ErrorMessage(
                exception.getMessage(),
                422,
                "https://smartcampus.api/docs/errors#invalid-linked-resource"
        );

        return Response.status(422) // or Response.Status.BAD_REQUEST (400)
                .entity(errorMessage)
                .build();
    }
}
