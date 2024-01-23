package lk.ijse.gym.dto.tm;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTm {
    private String trainerId;
    private  String name;
    private  String contactNu;
    private String address;
    private String role;
    private String availability;
}
