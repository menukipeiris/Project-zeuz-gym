package lk.ijse.gym.dto;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class SupplimentDto {

    private String code;
    private String description;
    private double unitPrice;
    private int qtyOnHand;

}
