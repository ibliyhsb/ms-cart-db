package cl.duoc.ms_cart_db.model.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import cl.duoc.ms_cart_db.model.entities.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

@Query("SELECT ALL FROM Cart WHERE id_cart = :idCart")
List<Cart> findByCartId (@Param("idCart") Long idCart);

@Query("DELETE FROM Cart WHERE id_cart = :idCart AND product_name = :productName")
ResponseEntity<?> deleteProductByName (@Param("idCart") Long idCart, @Param("productName") String productName);
}
