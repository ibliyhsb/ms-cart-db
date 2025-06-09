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

            Cart newCart = translateDtoToEntity(cartDTO);
            cartRepository.save(newCart);

            return ResponseEntity.ok("Cart created.");
        }
    


    public ResponseEntity<?> getCartById (Long idCart){

        Optional<Cart> OpCart = cartRepository.findById(idCart);
        if(!OpCart.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no cart with this ID");
        }
    
        else {

        List<Cart> data = cartRepository.findByCartId(idCart);
        ArrayList<Long> products = new ArrayList<>();
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

        return ResponseEntity.ok("ID Cart: "
                                 + cartDTO.getIdCart()
                                 + "\n"
                                 + "ID Customer: "
                                 + cartDTO.getIdCustomer()
                                 + "\n"
                                 + "Products: "
                                 + "\n"
                                 + cartDTO.getProducts()
                                 + "\n"
                                 + "Total: "
                                 + cartDTO.getTotal()); }

    }

}
