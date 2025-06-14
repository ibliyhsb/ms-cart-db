package cl.duoc.ms_cart_db.model.dto;

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

public class ProductDTO {

    @JsonProperty(value = "product_id")
        private Long idProduct;

    @JsonProperty(value = "product_name")
        private String productName;

    @JsonProperty(value = "price")
        private int price;

    @JsonProperty(value = "stock")
        private int stock;
}
