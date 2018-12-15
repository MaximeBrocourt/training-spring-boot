package com.ecommerce.microcommerce.web.exceptions;

import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Api(description = "API pour gérer les exceptions des produits gratuit")
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ProduitGratuitException extends RuntimeException {

    public ProduitGratuitException(String s) {
        super(s);
    }
}