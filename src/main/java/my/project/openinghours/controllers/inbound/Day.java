package my.project.openinghours.controllers.inbound;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.DateTimeException;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Day {

    MONDAY("monday"),
    TUESDAY("tuesday"),
    WEDNESDAY("wednesday"),
    THURSDAY("thursday"),
    FRIDAY("friday"),
    SATURDAY("saturday"),
    SUNDAY("sunday");


    final String day;

    Day(String day) {
        this.day = day;
    }


    @JsonValue
    public String getDay() {
        return day;
    }

    private static final Day[] ENUMS = Day.values();

    public static Day of(int day) {
        if (day < 1 || day > 7) {
            throw new DateTimeException("Invalid value for Day: " + day);
        }
        return ENUMS[day - 1];
    }

    public int getValue() {
        return ordinal() + 1;
    }
}
