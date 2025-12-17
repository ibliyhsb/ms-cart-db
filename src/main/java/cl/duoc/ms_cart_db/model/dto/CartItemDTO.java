package cl.duoc.ms_cart_db.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value = "productCode")
    private String productCode;

    @JsonProperty(value = "productName")
    private String productName;

    // Acepta tanto "price" como "unitPrice" para compatibilidad
    @JsonProperty(value = "price")
    private Double price;

    @JsonProperty(value = "quantity")
    private Integer quantity;

    @JsonProperty(value = "size")
    private String size;

    @JsonProperty(value = "personalizationMessage")
    private String personalizationMessage;

    @JsonProperty(value = "imageUrl")
    private String imageUrl;

    @JsonProperty(value = "subtotal")
    private Double subtotal;

    // Setter alternativo para aceptar "unitPrice" además de "price"
    @JsonSetter("unitPrice")
    public void setUnitPrice(Double unitPrice) {
        this.price = unitPrice;
    }
}