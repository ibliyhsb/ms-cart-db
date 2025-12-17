package cl.duoc.ms_cart_db.model.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    @Id
    @Column(name = "id", length = 100)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cart", nullable = false)
    @JsonBackReference
    private Cart cart;

    @Column(name = "product_code", nullable = false, length = 50)
    private String productCode;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @Column(name = "size", length = 100)
    private String size;

    @Column(name = "personalization_message", columnDefinition = "TEXT")
    private String personalizationMessage;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    public void calculateSubtotal() {
        this.subtotal = this.price * this.quantity;
    }
}