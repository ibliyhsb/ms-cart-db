package cl.duoc.ms_cart_db.model.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cart")
    private Long idCart;

    @Column(name = "id_customer", nullable = false, unique = true)
    private Long idCustomer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<CartItem> items = new ArrayList<>();

    @Column(name = "subtotal")
    private Double subtotal = 0.0;

    @Column(name = "discount")
    private Double discount = 0.0;

    @Column(name = "total")
    private Double total = 0.0;

    @Column(name = "applied_coupon")
    private String appliedCoupon;

    public void calculateTotals() {
        this.subtotal = items.stream()
            .mapToDouble(CartItem::getSubtotal)
            .sum();
        this.total = this.subtotal - (this.discount != null ? this.discount : 0.0);
    }
}