package cl.duoc.ms_cart_db.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.duoc.ms_cart_db.model.dto.CartDTO;
import cl.duoc.ms_cart_db.model.dto.CartItemDTO;
import cl.duoc.ms_cart_db.model.entities.Cart;
import cl.duoc.ms_cart_db.model.repository.CartRepository;


@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    // A) Crear carrito nuevo
    @Transactional
    public CartDTO createCart(String idCustomer) {
        // Generar nuevo idCart (siguiente ID disponible)
        Long maxIdCart = cartRepository.findMaxIdCart();
        Long newIdCart = (maxIdCart == null) ? 1L : maxIdCart + 1;
        
        CartDTO cartDTO = new CartDTO();
        cartDTO.setIdCart(newIdCart);
        cartDTO.setIdCustomer(idCustomer);
        cartDTO.setItems(new ArrayList<>());
        cartDTO.setTotal(0);
        
        return cartDTO;
    }
    
    // B) Agregar producto al carrito
    @Transactional
    public CartDTO addProduct(Long idCart, CartItemDTO item) {
        // Buscar si ya existe el mismo item
        Optional<Cart> existingItem = cartRepository
            .findByIdCartAndProductIdAndSizeAndPersonalizationMessage(
                idCart,
                item.getProductId(),
                item.getSize(),
                item.getPersonalizationMessage()
            );
        
        if (existingItem.isPresent()) {
            // Si existe, incrementar cantidad
            Cart cart = existingItem.get();
            cart.setQuantity(cart.getQuantity() + item.getQuantity());
            cartRepository.save(cart);
        } else {
            // Si no existe, crear nuevo registro
            Cart newCart = new Cart();
            newCart.setIdCart(idCart);
            newCart.setIdCustomer(item.getIdCustomer());
            newCart.setProductId(item.getProductId());
            newCart.setProductName(item.getProductName());
            newCart.setPrice(item.getPrice());
            newCart.setQuantity(item.getQuantity());
            newCart.setSize(item.getSize());
            newCart.setPersonalizationMessage(item.getPersonalizationMessage());
            cartRepository.save(newCart);
        }
        
        // Retornar carrito completo actualizado
        return getCartById(idCart);
    }
    
    // C) Actualizar cantidad de un producto
    @Transactional
    public CartDTO updateQuantity(Long idCart, Long productId, int quantity) {
        List<Cart> items = cartRepository.findByIdCart(idCart);
        
        for (Cart item : items) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
                cartRepository.save(item);
                break;
            }
        }
        
        return getCartById(idCart);
    }
    
    // D) Eliminar producto del carrito
    @Transactional
    public CartDTO deleteProduct(Long idCart, Long productId) {
        cartRepository.deleteByIdCartAndProductId(idCart, productId);
        return getCartById(idCart);
    }
    
    // E) Obtener carrito por ID
    public CartDTO getCartById(Long idCart) {
        List<Cart> cartItems = cartRepository.findByIdCart(idCart);
        
        CartDTO cartDTO = new CartDTO();
        cartDTO.setIdCart(idCart);
        
        if (!cartItems.isEmpty()) {
            cartDTO.setIdCustomer(cartItems.get(0).getIdCustomer());
            
            // Convertir entities a DTOs
            List<CartItemDTO> items = cartItems.stream().map(cart -> {
                CartItemDTO item = new CartItemDTO();
                item.setProductId(cart.getProductId());
                item.setProductName(cart.getProductName());
                item.setPrice(cart.getPrice());
                item.setQuantity(cart.getQuantity());
                item.setSize(cart.getSize());
                item.setPersonalizationMessage(cart.getPersonalizationMessage());
                return item;
            }).collect(Collectors.toList());
            
            cartDTO.setItems(items);
            
            // Calcular total
            int total = items.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
            cartDTO.setTotal(total);
        } else {
            cartDTO.setItems(new ArrayList<>());
            cartDTO.setTotal(0);
        }
        
        return cartDTO;
    }
}
