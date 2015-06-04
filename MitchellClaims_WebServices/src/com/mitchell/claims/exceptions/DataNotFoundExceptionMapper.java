package com.mitchell.claims.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.mitchell.claims.model.ErrorMessage;

@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

	@Override
	public Response toResponse(DataNotFoundException ex) {
		ErrorMessage em = new ErrorMessage(ex.getMessage());
		return Response.status(Status.NOT_FOUND)
				.entity(em).build();
	}
	
	

}
