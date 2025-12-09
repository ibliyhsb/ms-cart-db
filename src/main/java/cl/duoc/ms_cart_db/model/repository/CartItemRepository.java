package cl.duoc.ms_cart_db.model.repository;

import cl.duoc.ms_cart_db.model.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    List<CartItem> findByCartIdCart(Long idCart);
    void deleteByCartIdCartAndId(Long idCart, String itemId);
}