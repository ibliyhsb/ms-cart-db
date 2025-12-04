package cl.duoc.ms_cart_db.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class CartDTO {
    
    @JsonProperty(value = "id_cart")
    private Long idCart;

    @JsonProperty(value = "id_customer")
    private String idCustomer;

    @JsonProperty(value = "items")
    private List<CartItemDTO> items;

    @JsonProperty(value = "total")
    private int total;

}
