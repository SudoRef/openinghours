package my.project.openinghours.controllers.inbound;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TypeEnum {
    OPEN("open"),
    CLOSE("close");

    private final String alias;

    TypeEnum(String alias) {
        this.alias = alias;
    }

    @JsonValue
    public String getAlias() {
        return alias;
    }
}
