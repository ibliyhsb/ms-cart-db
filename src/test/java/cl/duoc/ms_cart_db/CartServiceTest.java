package cl.duoc.ms_cart_db;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.ms_cart_db.model.dto.CartDTO;
import cl.duoc.ms_cart_db.model.entities.Cart;
import cl.duoc.ms_cart_db.model.entities.CartItem;
import cl.duoc.ms_cart_db.model.repository.CartRepository;
import cl.duoc.ms_cart_db.model.repository.CartItemRepository;
import cl.duoc.ms_cart_db.service.CartService;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    
    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        // Nothing specific needed here, Mockito annotations handle the setup
    }

    @Test
    void testGetCartById_ShouldIncludeAllProducts() {
        // Arrange
        Long cartId = 1L;
        
        // Create cart with items
        Cart cart = new Cart();
        cart.setIdCart(cartId);
        cart.setIdCustomer(cartId);
        
        List<CartItem> items = new ArrayList<>();
        
        // First product
        CartItem item1 = new CartItem();
        item1.setId("item1");
        item1.setCart(cart);
        item1.setProductCode("P001");
        item1.setProductName("Product1");
        item1.setPrice(100.0);
        item1.setQuantity(1);
        item1.setSubtotal(100.0);
        items.add(item1);
        
        // Second product
        CartItem item2 = new CartItem();
        item2.setId("item2");
        item2.setCart(cart);
        item2.setProductCode("P002");
        item2.setProductName("Product2");
        item2.setPrice(200.0);
        item2.setQuantity(1);
        item2.setSubtotal(200.0);
        items.add(item2);
        
        // Third product
        CartItem item3 = new CartItem();
        item3.setId("item3");
        item3.setCart(cart);
        item3.setProductCode("P003");
        item3.setProductName("Product3");
        item3.setPrice(300.0);
        item3.setQuantity(1);
        item3.setSubtotal(300.0);
        items.add(item3);
        
        cart.setItems(items);
        cart.setSubtotal(600.0);
        cart.setTotal(600.0);
        
        // Mock repository response
        when(cartRepository.findByIdCart(cartId)).thenReturn(Optional.of(cart));
        
        // Act
        CartDTO result = cartService.getCartById(cartId);
        
        // Assert
        assertNotNull(result, "CartDTO should not be null");
        assertEquals(cartId, result.getIdCart(), "Cart ID should match");
        assertEquals(cartId, result.getIdCustomer(), "Customer ID should match");
        
        // Verify all three products are included
        assertEquals(3, result.getItems().size(), "Should have 3 items");
        assertTrue(result.getItems().stream().anyMatch(i -> i.getProductName().equals("Product1")), "Should contain Product1");
        assertTrue(result.getItems().stream().anyMatch(i -> i.getProductName().equals("Product2")), "Should contain Product2");
        assertTrue(result.getItems().stream().anyMatch(i -> i.getProductName().equals("Product3")), "Should contain Product3");
        
        // Verify total price is correct
        assertEquals(600.0, result.getTotal(), "Total should be 600.0 (100+200+300)");
    }

    @Test
    void testGetCartById_WithValidProduct() {
        // Arrange
        Long cartId = 2L;
        
        Cart cart = new Cart();
        cart.setIdCart(cartId);
        cart.setIdCustomer(cartId);
        
        List<CartItem> items = new ArrayList<>();
        
        // Valid product
        CartItem item1 = new CartItem();
        item1.setId("item1");
        item1.setCart(cart);
        item1.setProductCode("P001");
        item1.setProductName("ValidProduct");
        item1.setPrice(150.0);
        item1.setQuantity(1);
        item1.setSubtotal(150.0);
        items.add(item1);
        
        cart.setItems(items);
        cart.setSubtotal(150.0);
        cart.setTotal(150.0);
        
        when(cartRepository.findByIdCart(cartId)).thenReturn(Optional.of(cart));
        
        // Act
        CartDTO result = cartService.getCartById(cartId);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getItems().size(), "Should have only 1 valid product");
        assertTrue(result.getItems().stream().anyMatch(i -> i.getProductName().equals("ValidProduct")), "Should contain ValidProduct");
        assertEquals(150.0, result.getTotal(), "Total should be 150.0");
    }

    @Test
    void testGetCartById_ReturnsNull_WhenCartNotFound() {
        // Arrange
        Long cartId = 999L;
        when(cartRepository.findByIdCart(cartId)).thenReturn(Optional.empty());
        
        // Act
        CartDTO result = cartService.getCartById(cartId);
        
        // Assert
        assertNull(result, "Should return null when cart is not found");
    }

    @Test
    void testGetCartById_EmptyCart() {
        // Arrange
        Long cartId = 3L;
        
        Cart cart = new Cart();
        cart.setIdCart(cartId);
        cart.setIdCustomer(cartId);
        cart.setItems(new ArrayList<>());
        cart.setSubtotal(0.0);
        cart.setTotal(0.0);
        
        when(cartRepository.findByIdCart(cartId)).thenReturn(Optional.of(cart));
        
        // Act
        CartDTO result = cartService.getCartById(cartId);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.getItems().size(), "Should have no items");
        assertEquals(0.0, result.getTotal(), "Total should be 0.0");
    }
}
