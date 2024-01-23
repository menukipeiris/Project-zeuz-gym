package lk.ijse.gym.dto;

import lk.ijse.gym.dto.tm.CartTm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class PlaceOrderDto {
    private String orderId;
    private LocalDate date;
    private String memberId;
    private List<CartTm> cartTmList = new ArrayList<>();
}
