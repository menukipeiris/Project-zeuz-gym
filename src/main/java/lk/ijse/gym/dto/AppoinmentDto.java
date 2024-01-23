package lk.ijse.gym.dto;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class AppoinmentDto {
    private String appoinmentId;
    private String date;
    private String memberId;
    private String time;
    private String description;
}
