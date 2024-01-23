package lk.ijse.gym.dto;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class WorkoutDto {
    private String planId;
    private String warmUp;
    private String training;
    private String cardio;
    private String warmDown;
}
