package my.project.openinghours.controllers.inbound;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Type {
    TypeEnum type;
    Integer value;
}
