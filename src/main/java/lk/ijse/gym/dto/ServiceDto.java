package lk.ijse.gym.dto;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class ServiceDto {

    private String serviceId;
    private String trainerId;
    private String trainerName;
    private String classType;
    private String time;
    private String date;

}
