package cl.duoc.ms_cart_db.service;

import cl.duoc.ms_cart_db.model.dto.CartDTO;
import cl.duoc.ms_cart_db.model.dto.CartItemDTO;
import cl.duoc.ms_cart_db.model.entities.Cart;
import cl.duoc.ms_cart_db.model.entities.CartItem;
import cl.duoc.ms_cart_db.model.repository.CartRepository;
import cl.duoc.ms_cart_db.model.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartDTO getCartById(Long idCart) {
        Cart cart = cartRepository.findById(idCart)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        return convertToDTO(cart);
    }

    public CartDTO getByCustomerId(Long customerId) {
        Cart cart = cartRepository.findByIdCustomer(customerId).orElse(null);
        return cart != null ? convertToDTO(cart) : null;
    }

    @Transactional
    public Long createCart(Long idCustomer) {
        // Verificar si ya existe un carrito para este customer
        if (cartRepository.findByIdCustomer(idCustomer).isPresent()) {
            throw new RuntimeException("El customer ya tiene un carrito");
        }

        Cart cart = new Cart();
        cart.setIdCustomer(idCustomer);
        cart.setSubtotal(0.0);
        cart.setDiscount(0.0);
        cart.setTotal(0.0);
        
        Cart savedCart = cartRepository.save(cart);
        return savedCart.getIdCart();
    }

    @Transactional
    public void addItem(Long idCart, CartItemDTO itemDTO) {
        Cart cart = cartRepository.findById(idCart)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        CartItem item = new CartItem();
        item.setId(itemDTO.getId());
        item.setCart(cart);
        item.setProductCode(itemDTO.getProductCode());
        item.setProductName(itemDTO.getProductName());
        item.setPrice(itemDTO.getPrice());
        item.setQuantity(itemDTO.getQuantity());
        item.setSize(itemDTO.getSize());
        item.setPersonalizationMessage(itemDTO.getPersonalizationMessage());
        item.setImageUrl(itemDTO.getImageUrl());
        item.calculateSubtotal();

        cart.getItems().add(item);
        cart.calculateTotals();
        
        cartRepository.save(cart);
    }

    @Transactional
    public void updateQuantity(Long idCart, String itemId, Integer quantity) {
        Cart cart = cartRepository.findById(idCart)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        CartItem item = cart.getItems().stream()
            .filter(i -> i.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        item.setQuantity(quantity);
        item.calculateSubtotal();
        
        cart.calculateTotals();
        cartRepository.save(cart);
    }

    @Transactional
    public void removeItem(Long idCart, String itemId) {
        Cart cart = cartRepository.findById(idCart)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        cart.calculateTotals();
        
        cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long idCart) {
        Cart cart = cartRepository.findById(idCart)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        cart.getItems().clear();
        cart.setSubtotal(0.0);
        cart.setDiscount(0.0);
        cart.setTotal(0.0);
        cart.setAppliedCoupon(null);
        
        cartRepository.save(cart);
    }

    @Transactional
    public void applyCoupon(Long idCart, String couponCode, Double discount) {
        Cart cart = cartRepository.findById(idCart)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        cart.setAppliedCoupon(couponCode);
        cart.setDiscount(discount);
        cart.calculateTotals();
        
        cartRepository.save(cart);
    }

    // Conversión de Entity a DTO
    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setIdCart(cart.getIdCart());
        dto.setIdCustomer(cart.getIdCustomer());
        dto.setSubtotal(cart.getSubtotal());
        dto.setDiscount(cart.getDiscount());
        dto.setTotal(cart.getTotal());
        dto.setAppliedCoupon(cart.getAppliedCoupon());
        
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
            .map(this::convertItemToDTO)
            .collect(Collectors.toList());
        dto.setItems(itemDTOs);
        
        return dto;
    }

    private CartItemDTO convertItemToDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.getId());
        dto.setProductCode(item.getProductCode());
        dto.setProductName(item.getProductName());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        dto.setSize(item.getSize());
        dto.setPersonalizationMessage(item.getPersonalizationMessage());
        dto.setImageUrl(item.getImageUrl());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}