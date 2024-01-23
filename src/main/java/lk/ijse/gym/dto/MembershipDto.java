package lk.ijse.gym.dto;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class MembershipDto {
    private String membershipId;
    private String memberId;
    private String type;
    private double amount;
    private String date;
    private double signUpFee;
}
