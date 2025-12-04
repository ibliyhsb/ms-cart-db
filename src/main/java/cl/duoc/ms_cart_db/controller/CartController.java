package cl.duoc.ms_cart_db.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.ms_cart_db.model.dto.CartDTO;
import cl.duoc.ms_cart_db.model.dto.CartItemDTO;
import cl.duoc.ms_cart_db.service.CartService;



@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/getCartById/{idCart}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable Long idCart) {
        return ResponseEntity.ok(cartService.getCartById(idCart));
    }
    
    @PostMapping("/createCart/{idCustomer}")
    public ResponseEntity<CartDTO> createCart(@PathVariable String idCustomer) {
        return ResponseEntity.ok(cartService.createCart(idCustomer));
    }
    
    @PostMapping("/addProduct/{idCart}")
    public ResponseEntity<CartDTO> addProduct(
        @PathVariable Long idCart,
        @RequestBody CartItemDTO item
    ) {
        return ResponseEntity.ok(cartService.addProduct(idCart, item));
    }
    
    @PutMapping("/updateQuantity/{idCart}/{productId}")
    public ResponseEntity<CartDTO> updateQuantity(
        @PathVariable Long idCart,
        @PathVariable Long productId,
        @RequestBody Map<String, Integer> body
    ) {
        return ResponseEntity.ok(cartService.updateQuantity(idCart, productId, body.get("quantity")));
    }
    
    @DeleteMapping("/deleteProduct/{idCart}/{productId}")
    public ResponseEntity<CartDTO> deleteProduct(
        @PathVariable Long idCart,
        @PathVariable Long productId
    ) {
        return ResponseEntity.ok(cartService.deleteProduct(idCart, productId));
    }
}
