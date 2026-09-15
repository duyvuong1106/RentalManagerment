package com.nldv.rentalroom.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> validation(MethodArgumentNotValidException ex){
        Map<String,String> errors=new HashMap<>();
        for(FieldError e: ex.getBindingResult().getFieldErrors()) errors.put(e.getField(), e.getDefaultMessage());
        return response(HttpStatus.BAD_REQUEST,"Dữ liệu không hợp lệ",errors);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,Object>> badRequest(IllegalArgumentException ex){return response(HttpStatus.BAD_REQUEST,ex.getMessage(),null);}
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> internal(Exception ex){return response(HttpStatus.INTERNAL_SERVER_ERROR,"Có lỗi xảy ra trên máy chủ",null);}
    private ResponseEntity<Map<String,Object>> response(HttpStatus status,String message,Object details){
        Map<String,Object> body=new HashMap<>(); body.put("status",status.value()); body.put("message",message); if(details!=null) body.put("details",details); return ResponseEntity.status(status).body(body);
    }
}
