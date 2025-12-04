package cl.duoc.ms_cart_db.model.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cl.duoc.ms_cart_db.model.entities.Cart;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

    // Obtener todos los items de un carrito
    List<Cart> findByIdCart(Long idCart);
    
    // Buscar item específico en el carrito
    Optional<Cart> findByIdCartAndProductIdAndSizeAndPersonalizationMessage(
        Long idCart, 
        Long productId, 
        String size, 
        String personalizationMessage
    );
    
    // Eliminar todos los items de un carrito
    @Transactional
    @Modifying
    void deleteByIdCart(Long idCart);
    
    // Eliminar producto específico de un carrito
    @Transactional
    @Modifying
    void deleteByIdCartAndProductId(Long idCart, Long productId);
    
    // Obtener el último id_cart usado
    @Query("SELECT MAX(c.idCart) FROM Cart c")
    Long findMaxIdCart();

}
