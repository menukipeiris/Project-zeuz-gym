package lk.ijse.gym.dto;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class MemberDto {
    private String memberId;
    private String name;
    private String gender;
    private int cont;
    private String address;
    private String dateJoin;
    private String plan;
    private double planPrice;




}
