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
import cl.duoc.ms_cart_db.model.repository.CartRepository;
import cl.duoc.ms_cart_db.service.CartService;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

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
        
        // Create mock cart data simulating database records
        List<Cart> cartData = new ArrayList<>();
        
        // Initial record with null product (created when cart is created)
        Cart initialCart = new Cart();
        initialCart.setId(1L);
        initialCart.setIdCart(cartId);
        initialCart.setIdCustomer(cartId);
        initialCart.setProduct(null);
        initialCart.setPrice(0);
        cartData.add(initialCart);
        
        // First product added
        Cart cart1 = new Cart();
        cart1.setId(2L);
        cart1.setIdCart(cartId);
        cart1.setIdCustomer(cartId);
        cart1.setProduct("Product1");
        cart1.setPrice(100);
        cartData.add(cart1);
        
        // Second product added
        Cart cart2 = new Cart();
        cart2.setId(3L);
        cart2.setIdCart(cartId);
        cart2.setIdCustomer(cartId);
        cart2.setProduct("Product2");
        cart2.setPrice(200);
        cartData.add(cart2);
        
        // Third product added
        Cart cart3 = new Cart();
        cart3.setId(4L);
        cart3.setIdCart(cartId);
        cart3.setIdCustomer(cartId);
        cart3.setProduct("Product3");
        cart3.setPrice(300);
        cartData.add(cart3);
        
        // Mock repository responses
        when(cartRepository.findByIdCart(cartId)).thenReturn(Optional.of(initialCart));
        when(cartRepository.findAllByCartId(cartId)).thenReturn(cartData);
        
        // Act
        CartDTO result = cartService.getCartById(cartId);
        
        // Assert
        assertNotNull(result, "CartDTO should not be null");
        assertEquals(cartId, result.getIdCart(), "Cart ID should match");
        assertEquals(cartId, result.getIdCustomer(), "Customer ID should match");
        
        // Verify all three products are included (not missing the first product)
        assertEquals(3, result.getProducts().size(), "Should have 3 products");
        assertTrue(result.getProducts().contains("Product1"), "Should contain Product1");
        assertTrue(result.getProducts().contains("Product2"), "Should contain Product2");
        assertTrue(result.getProducts().contains("Product3"), "Should contain Product3");
        
        // Verify total price is correct
        assertEquals(600, result.getTotal(), "Total should be 600 (100+200+300)");
    }

    @Test
    void testGetCartById_ShouldFilterNullProducts() {
        // Arrange
        Long cartId = 2L;
        
        List<Cart> cartData = new ArrayList<>();
        
        // Initial record with null product
        Cart initialCart = new Cart();
        initialCart.setId(1L);
        initialCart.setIdCart(cartId);
        initialCart.setIdCustomer(cartId);
        initialCart.setProduct(null);
        initialCart.setPrice(0);
        cartData.add(initialCart);
        
        // Product with empty string (should also be filtered)
        Cart emptyCart = new Cart();
        emptyCart.setId(2L);
        emptyCart.setIdCart(cartId);
        emptyCart.setIdCustomer(cartId);
        emptyCart.setProduct("");
        emptyCart.setPrice(0);
        cartData.add(emptyCart);
        
        // Valid product
        Cart cart1 = new Cart();
        cart1.setId(3L);
        cart1.setIdCart(cartId);
        cart1.setIdCustomer(cartId);
        cart1.setProduct("ValidProduct");
        cart1.setPrice(150);
        cartData.add(cart1);
        
        when(cartRepository.findByIdCart(cartId)).thenReturn(Optional.of(initialCart));
        when(cartRepository.findAllByCartId(cartId)).thenReturn(cartData);
        
        // Act
        CartDTO result = cartService.getCartById(cartId);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getProducts().size(), "Should have only 1 valid product");
        assertTrue(result.getProducts().contains("ValidProduct"), "Should contain ValidProduct");
        assertEquals(150, result.getTotal(), "Total should be 150");
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
    void testGetCartById_EmptyCart_OnlyInitialRecord() {
        // Arrange
        Long cartId = 3L;
        
        List<Cart> cartData = new ArrayList<>();
        
        // Only initial record with null product
        Cart initialCart = new Cart();
        initialCart.setId(1L);
        initialCart.setIdCart(cartId);
        initialCart.setIdCustomer(cartId);
        initialCart.setProduct(null);
        initialCart.setPrice(0);
        cartData.add(initialCart);
        
        when(cartRepository.findByIdCart(cartId)).thenReturn(Optional.of(initialCart));
        when(cartRepository.findAllByCartId(cartId)).thenReturn(cartData);
        
        // Act
        CartDTO result = cartService.getCartById(cartId);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.getProducts().size(), "Should have no products");
        assertEquals(0, result.getTotal(), "Total should be 0");
    }
}
