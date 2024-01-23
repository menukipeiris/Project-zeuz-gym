package lk.ijse.gym.dto.tm;
import javafx.scene.control.Button;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class SupplimentTm {
    private String code;
    private String description;
    private double unitPrice;
    private int qtyOnHand;
    private Button btn;


}
