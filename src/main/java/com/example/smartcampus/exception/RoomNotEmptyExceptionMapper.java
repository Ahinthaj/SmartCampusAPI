/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.smartcampus.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException exception) {

        ErrorMessage errorMessage = new ErrorMessage(
                exception.getMessage(),
                409,
                "https://smartcampus.api/docs/errors#room-not-empty"
        );

        return Response.status(Response.Status.CONFLICT)
                .entity(errorMessage)
                .build();
    }
}
