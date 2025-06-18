package cl.duoc.ms_cart_db.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.ms_cart_db.model.dto.CartDTO;
import cl.duoc.ms_cart_db.service.CartService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/Cart")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/getCartById/{idCart}")
    public CartDTO getCartById(@PathVariable("idCart") Long idCart) {
        return cartService.getCartById(idCart);
    }
    
    @PostMapping("/insertCart")
    public ResponseEntity<?> insertCart (@RequestBody CartDTO cartDTO){
        return cartService.insertCart(cartDTO);
    }
    
    @PostMapping("/insertProduct/{price}/{productName}/{idCart}")
    public ResponseEntity<?> insertProduct (@PathVariable("price") int price, @PathVariable("productName") String productName, @PathVariable("idCart") Long idCart){
        return cartService.insertProduct(price, productName, idCart);
    }

    @DeleteMapping("/deleteProduct/{productName}/{idCart}")
      public ResponseEntity<String> deleteProduct(@PathVariable("productName")String productName, @PathVariable("idCart") Long idCart){
          return cartService.deleteProduct(productName, idCart);
      }


}
