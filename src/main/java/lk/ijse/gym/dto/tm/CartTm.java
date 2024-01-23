package lk.ijse.gym.dto.tm;

import javafx.scene.control.Button;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class CartTm {
    private String code;
    private String description;
    private int qty;
    private double unitPrice;
    private double tot;
    private Button btn;
}
