package io.github.leomoreiradev.quarkussocial.rest.dto;

import lombok.Data;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ResponseError {

    public static final int UNPROCESSABLE_ENTITY_STATUS = 422;

    private String message;
    private Collection<FieldError> errors;

    public ResponseError(String message, Collection<FieldError> errors) {
        this.message = message;
        this.errors = errors;
    }

    //Criando o obj ResponseError
    public static <T> ResponseError createFromValidation(Set<ConstraintViolation<T>> violations) {

       // Lista de erros =  Collection<FieldError> errors
        List<FieldError> erros = violations
                .stream()
                .map(constraintViolation ->
                        new FieldError(
                                constraintViolation.getPropertyPath().toString(),
                                constraintViolation.getMessage()
                        )
                )
                .collect(Collectors.toList());

        //Message = String message
        String message = "Validation Error";

        //Montando o responseError para a resposta
        var responseError = new ResponseError(message, erros);

        return responseError;
    }

    public Response withStatusCode(int code) {
        return Response.status(code).entity(this).build();
    }
}
