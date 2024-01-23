package lk.ijse.gym.dto;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class PaymentDto {

    private String paymentId;
    private String memberId;
    private String name;
    private String plan;
    private double amountPaid;
    private String date;
}
