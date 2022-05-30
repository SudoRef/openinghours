package my.project.openinghours;


import my.project.openinghours.services.OpeningHoursService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.util.*;

import my.project.openinghours.model.UnixTime;

import static my.project.openinghours.model.Status.CLOSE;
import static my.project.openinghours.model.Status.OPEN;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OpeningHoursServiceTests {

    @Autowired
    OpeningHoursService service;
    EnumMap<DayOfWeek, List<UnixTime>> inboundEnumMap;
    Map<String, String> outboundMap;

    @BeforeEach
    void createEnumMap() {
        inboundEnumMap = new EnumMap<>(DayOfWeek.class);
        inboundEnumMap.put(DayOfWeek.MONDAY, new ArrayList<>());
        inboundEnumMap.put(DayOfWeek.TUESDAY, List.of(createType(OPEN, 36000), createType(CLOSE, 64800)));
        inboundEnumMap.put(DayOfWeek.WEDNESDAY, new ArrayList<>());
        inboundEnumMap.put(DayOfWeek.THURSDAY, List.of(createType(OPEN, 37800), createType(CLOSE, 64800)));
        inboundEnumMap.put(DayOfWeek.FRIDAY, List.of(createType(OPEN, 36000)));
        inboundEnumMap.put(DayOfWeek.SATURDAY, List.of(createType(CLOSE, 3600), createType(OPEN, 32400), createType(CLOSE, 39600), createType(OPEN, 57600), createType(CLOSE, 82800)));
        inboundEnumMap.put(DayOfWeek.SUNDAY, List.of(createType(OPEN, 43200), createType(CLOSE, 75600)));

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

        EnumMap<DayOfWeek, List<UnixTime>> brokenMap = inboundEnumMap.clone();
        brokenMap.remove(DayOfWeek.SATURDAY);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.convertToReadableFormat(brokenMap);
        });

        String expectedMessage = "Invalid value for day:";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testConvertTypeToAmPm() {
        int Type1 = 0;
        int Type2 = 43200;
        int Type3 = 64800;

        assertEquals("12:00 AM", service.convertTypeToAmPm(Type1));
        assertEquals("12:00 PM", service.convertTypeToAmPm(Type2));
        assertEquals("06:00 PM", service.convertTypeToAmPm(Type3));
    }

    private UnixTime createType(my.project.openinghours.model.Status status, int value){
        UnixTime unixTime = new UnixTime();
        unixTime.setType(status);
        unixTime.setValue(value);
        return unixTime;
    }

}
