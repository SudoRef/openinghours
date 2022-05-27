package my.project.openinghours;

import my.project.openinghours.controllers.inbound.Day;
import my.project.openinghours.controllers.inbound.Type;
import my.project.openinghours.services.OpeningHoursService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static my.project.openinghours.controllers.inbound.TypeEnum.CLOSE;
import static my.project.openinghours.controllers.inbound.TypeEnum.OPEN;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OpeningHoursServiceTests {

    @Autowired
    OpeningHoursService service;
    EnumMap<Day, List<Type>> inboundEnumMap;
    Map<String, String> outboundMap;

    @BeforeEach
    void createEnumMap() {
        inboundEnumMap = new EnumMap<>(Day.class);
        inboundEnumMap.put(Day.MONDAY, new ArrayList<>());
        inboundEnumMap.put(Day.TUESDAY, List.of(new Type(OPEN, 36000), new Type(CLOSE, 64800)));
        inboundEnumMap.put(Day.WEDNESDAY, new ArrayList<>());
        inboundEnumMap.put(Day.THURSDAY, List.of(new Type(OPEN, 37800), new Type(CLOSE, 64800)));
        inboundEnumMap.put(Day.FRIDAY, List.of(new Type(OPEN, 36000)));
        inboundEnumMap.put(Day.SATURDAY, List.of(new Type(CLOSE, 3600), new Type(OPEN, 32400), new Type(CLOSE, 39600), new Type(OPEN, 57600), new Type(CLOSE, 82800)));
        inboundEnumMap.put(Day.SUNDAY, List.of(new Type(OPEN, 43200), new Type(CLOSE, 75600)));

        outboundMap = new HashMap<>();
        outboundMap.put("Monday", "Closed");
        outboundMap.put("Tuesday", "10:00 AM - 06:00 PM");
        outboundMap.put("Wednesday", "Closed");
        outboundMap.put("Thursday", "10:30 AM - 06:00 PM");
        outboundMap.put("Friday", "10:00 AM - 01:00 AM");
        outboundMap.put("Saturday", "09:00 AM - 11:00 AM, 04:00 PM - 11:00 PM");
        outboundMap.put("Sunday", "12:00 PM - 09:00 PM");
    }

    @Test
    void testConvertToReadableFormat_andGetOk() {
        Map<String, String> map = service.convertToReadableFormat(inboundEnumMap);
        assertEquals(outboundMap, map);
    }

    @Test
    void testConvertToReadableFormat_andGetInvalidArgument() {

        EnumMap<Day, List<Type>> brokenMap = inboundEnumMap.clone();
        brokenMap.remove(Day.SATURDAY);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.convertToReadableFormat(brokenMap);
        });

        String expectedMessage = "Invalid value for day:";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testConvertUnixTimeToAmPm() {
        int unixTime1 = 0;
        int unixTime2 = 43200;
        int unixTime3 = 64800;

        assertEquals("12:00 AM", service.convertUnixTimeToAmPm(unixTime1));
        assertEquals("12:00 PM", service.convertUnixTimeToAmPm(unixTime2));
        assertEquals("06:00 PM", service.convertUnixTimeToAmPm(unixTime3));
    }

}
