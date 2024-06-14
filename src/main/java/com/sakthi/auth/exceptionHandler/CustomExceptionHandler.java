package com.sakthi.auth.exceptionHandler;

import com.sakthi.auth.customException.BannedUserException;
import com.sakthi.auth.customException.UserNotActivatedException;
import com.sakthi.auth.model.error.CustomErrorListModel;
import com.sakthi.auth.model.error.CustomErrorModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorListModel> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.add(error.getDefaultMessage());

        });
        CustomErrorListModel customErrorModel= CustomErrorListModel.builder().errors(errors).build();
        return new ResponseEntity<>(customErrorModel
                ,HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomErrorModel> handleBadCredentialsException(BadCredentialsException ex){
        CustomErrorModel customErrorModel= CustomErrorModel.builder().error("Invalid Credentials").build();
        return new ResponseEntity<>(customErrorModel
                ,HttpStatus.NOT_ACCEPTABLE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<CustomErrorModel> handleUsernameNotFoundException(UsernameNotFoundException ex){
        CustomErrorModel customErrorModel= CustomErrorModel.builder().error(ex.getMessage()).build();
        return new ResponseEntity<>(customErrorModel
                ,HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(UserNotActivatedException.class)
    public ResponseEntity<CustomErrorModel> handleUserNotActivatedException(UserNotActivatedException ex){
        CustomErrorModel customErrorModel= CustomErrorModel.builder().error(ex.getMessage()).build();
        return new ResponseEntity<>(customErrorModel
                ,HttpStatus.NOT_ACCEPTABLE);
    }
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(BannedUserException.class)
    public ResponseEntity<CustomErrorModel> handleBannedUserException(BannedUserException ex){
        CustomErrorModel customErrorModel= CustomErrorModel.builder().error(ex.getMessage()).build();
        return new ResponseEntity<>(customErrorModel
                ,HttpStatus.FORBIDDEN);
    }


}
