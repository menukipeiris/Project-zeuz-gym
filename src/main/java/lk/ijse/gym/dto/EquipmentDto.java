package lk.ijse.gym.dto;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class EquipmentDto {
    private String equipmentId;
    private String equName;
    private String deliveryDate;
    private String description;
    private double cost;
    private String muscleUsed;
}
