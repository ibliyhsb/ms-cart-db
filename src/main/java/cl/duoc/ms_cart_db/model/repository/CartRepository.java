package cl.duoc.ms_cart_db.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cl.duoc.ms_cart_db.model.entities.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByIdCustomer(Long idCustomer);

@Query("SELECT c FROM Cart c WHERE c.idCart = :idCart")
List<Cart> findAllByCartId(@Param("idCart") Long idCart);

@Query("SELECT idCart FROM Cart WHERE idCart = :idCart")
Optional<Cart> findByIdCart (@Param("idCart") Long idCart);

// Metodo legacy - obsoleto, usar CartItemRepository.deleteById() en su lugar
// Mantenerlo comentado para evitar errores de query
// @Transactional
// @Modifying
// @Query("DELETE FROM CartItem ci WHERE ci.productName = :productName AND ci.cart.idCart = :idCart")
// void deleteProductByName(@Param("productName") String productName, @Param("idCart") Long idCart);

}
