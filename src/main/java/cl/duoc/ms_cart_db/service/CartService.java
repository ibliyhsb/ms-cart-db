package cl.duoc.ms_cart_db.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import cl.duoc.ms_cart_db.model.dto.CartDTO;
import cl.duoc.ms_cart_db.model.entities.Cart;
import cl.duoc.ms_cart_db.model.repository.CartRepository;


@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    public Cart translateDtoToEntity(CartDTO cartDTO){

        Cart cart = new Cart();
        cart.setIdCart(cartDTO.getIdCart());
        cart.setIdCustomer(cartDTO.getIdCustomer());
        cart.setProduct(null);
        cart.setPrice(0);

        return cart;
    }

    public ResponseEntity<String> insertCart (CartDTO cartDTO){

        Optional<Cart> cartOp = cartRepository.findById(cartDTO.getIdCart());

        if(cartOp.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This customer's cart already exist");
        }
            Cart newCart = translateDtoToEntity(cartDTO);
            cartRepository.save(newCart);

            return ResponseEntity.ok("Cart created.");
        }
    


    public CartDTO getCartById (Long idCart){

    Optional<Cart> cartOp = cartRepository.findByIdCart(idCart);
    List<Cart> data = cartRepository.findAllByCartId(idCart);

        if(!cartOp.isPresent()){
            return null; 
        }
    
        else {
        ArrayList<String> products = new ArrayList<>();
        CartDTO cartDTO = new CartDTO();
        int total = 0;
        Long idCustomer = null;

        for (Cart c : data){
            products.add(c.getProduct());
            total += c.getPrice();
            idCustomer = c.getIdCustomer();
        }

        cartDTO.setIdCart(idCart);
        cartDTO.setIdCustomer(idCustomer);
        cartDTO.setProducts(products);
        cartDTO.setTotal(total);

        return cartDTO;
    }

    }

    public ResponseEntity<?> insertProduct (int price, String productName, Long idCart){

        Optional<Cart> cartOp = cartRepository.findById(idCart);

        if(!cartOp.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no cart with this ID.");
        }

        else {

            Cart cart = new Cart();

            cart.setIdCustomer(idCart);
            cart.setIdCart(idCart);
            cart.setPrice(price);
            cart.setProduct(productName);
            cartRepository.save(cart);

            return ResponseEntity.ok("Product added to cart.");

        }
    }

    public ResponseEntity<String> deleteProduct(String productName, Long idCart){

        Optional<Cart> cartOp = cartRepository.findById(idCart);

        if(!cartOp.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no cart with this ID.");
        }

        else {
            if(getCartById(idCart).getProducts().contains(productName)){
            cartRepository.deleteProductByName(productName, idCart);
            return ResponseEntity.ok("Product deleted successfully");
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The product doesn't exist in the cart.");
            }

        }
    }
}
