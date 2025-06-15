package cl.duoc.ms_cart_db.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.ms_cart_db.model.dto.CartDTO;
import cl.duoc.ms_cart_db.model.dto.ProductDTO;
import cl.duoc.ms_cart_db.service.CartService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/products")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/getCartById/{idCart}")
    public ResponseEntity<?> getCartById(@PathVariable("idCart") Long idCart) {
        return cartService.getCartById(idCart);
    }
    
    @PostMapping("/insertCart")
    public ResponseEntity<String> insertCart (CartDTO cartDTO){
        return cartService.insertCart(cartDTO);
    }
    
    @PutMapping("/insertProduct/{idCart}")
    public ResponseEntity<?> insertProduct (ProductDTO productDTO, @PathVariable("idCart") Long idCart){
        return cartService.insertProduct(productDTO, idCart);
    }

    @DeleteMapping("/deleteProduct/{idCart}")
    public ResponseEntity<?> deleteProduct(String productName, Long idCart){
        return cartService.deleteProduct(productName, idCart);
    }


}
