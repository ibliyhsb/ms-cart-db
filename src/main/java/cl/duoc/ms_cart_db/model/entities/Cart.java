package cl.duoc.ms_cart_db.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString

public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "id_cart")
    private Long idCart;
    
    @Column(name = "id_customer")
    private String idCustomer;
    
    @Column(name = "product_id")
    private Long productId;
    
    @Column(name = "product_name")
    private String productName;
    
    @Column(name = "price")
    private int price;
    
    @Column(name = "quantity")
    private int quantity;
    
    @Column(name = "size")
    private String size;
    
    @Column(name = "personalization_message", columnDefinition = "TEXT")
    private String personalizationMessage;

}
