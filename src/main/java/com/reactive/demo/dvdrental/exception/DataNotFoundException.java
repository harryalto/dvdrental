package com.reactive.demo.dvdrental.exception;


public class DataNotFoundException extends NotFoundException {


    public DataNotFoundException(Integer id) {
        super(String.format("Item [%d] is not found", id));
    }

}