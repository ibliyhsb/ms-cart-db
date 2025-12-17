package cl.duoc.ms_cart_db.controller;

import cl.duoc.ms_cart_db.model.dto.CartDTO;
import cl.duoc.ms_cart_db.model.dto.CartItemDTO;
import cl.duoc.ms_cart_db.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/getCartById/{idCart}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable Long idCart) {
        CartDTO cart = cartService.getCartById(idCart);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/getByCustomerId/{customerId}")
    public ResponseEntity<CartDTO> getByCustomerId(@PathVariable Long customerId) {
        CartDTO cart = cartService.getByCustomerId(customerId);
        if (cart == null) {
            // Crear carrito automáticamente si no existe
            Long cartId = cartService.createCart(customerId);
            cart = cartService.getCartById(cartId);
        }
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/insertCart/{idCustomer}")
    public ResponseEntity<String> insertCart(@PathVariable Long idCustomer) {
        Long cartId = cartService.createCart(idCustomer);
        return ResponseEntity.ok("Carrito creado con ID: " + cartId);
    }

    @PostMapping("/addItem/{idCart}")
    public ResponseEntity<String> addItem(
            @PathVariable Long idCart,
            @RequestBody CartItemDTO itemDTO) {
        cartService.addItem(idCart, itemDTO);
        return ResponseEntity.ok("Item agregado al carrito");
    }

    @PutMapping("/updateQuantity/{idCart}/{itemId}/{quantity}")
    public ResponseEntity<String> updateQuantity(
            @PathVariable Long idCart,
            @PathVariable String itemId,
            @PathVariable Integer quantity) {
        cartService.updateQuantity(idCart, itemId, quantity);
        return ResponseEntity.ok("Cantidad actualizada");
    }

    @DeleteMapping("/removeItem/{idCart}/{itemId}")
    public ResponseEntity<String> removeItem(
            @PathVariable Long idCart,
            @PathVariable String itemId) {
        cartService.removeItem(idCart, itemId);
        return ResponseEntity.ok("Item eliminado del carrito");
    }

    @DeleteMapping("/clear/{idCart}")
    public ResponseEntity<String> clearCart(@PathVariable Long idCart) {
        cartService.clearCart(idCart);
        return ResponseEntity.ok("Carrito vaciado");
    }

    @PostMapping("/applyCoupon/{idCart}/{couponCode}/{discount}")
    public ResponseEntity<String> applyCoupon(
            @PathVariable Long idCart,
            @PathVariable String couponCode,
            @PathVariable Double discount) {
        cartService.applyCoupon(idCart, couponCode, discount);
        return ResponseEntity.ok("Cupón aplicado");
    }
}