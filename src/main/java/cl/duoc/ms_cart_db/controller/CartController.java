package cl.duoc.ms_cart_db.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.ms_cart_db.service.CartService;

@RestController
public class CartController {

    @Autowired
    CartService cartService;
}
