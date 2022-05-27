package my.project.openinghours.services;

import my.project.openinghours.controllers.inbound.Day;
import my.project.openinghours.controllers.inbound.Type;
import my.project.openinghours.controllers.inbound.TypeEnum;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.capitalize;

@Service
public class OpeningHoursService {

    private static final String PATTERN = "hh:mm a";
    private static final String CLOSED = "Closed";
    private static final String UTC = "UTC";

    public Map<String, String> convertToReadableFormat(EnumMap<Day, List<Type>> dayListEnumMap) {
        Map<String, String> readableMap = new LinkedHashMap<>();
        for (Map.Entry<Day, List<Type>> entry : dayListEnumMap.entrySet()) {
            String key = capitalize(entry.getKey().getDay());
            String val;
            if (entry.getValue().isEmpty()) {
                val = CLOSED;
            } else {
                val = buildDaySchedule(dayListEnumMap, entry.getKey());
            }
            readableMap.put(key, val);
        }
        return readableMap;
    }

    public String convertUnixTimeToAmPm(int unixTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return Instant.ofEpochSecond(unixTime)
                .atZone(ZoneId.of(UTC))
                .format(formatter);
    }

    private String buildDaySchedule(EnumMap<Day, List<Type>> map, Day day) {
        List<Type> types = map.get(day);
        StringBuilder sb = new StringBuilder();
        boolean isOpen = false;
        for (int i = 0; i < types.size(); i++) {
            Type type = types.get(i);
            if (i == 0 && TypeEnum.CLOSE.equals(type.getType())) {
                continue;
            }
            if (!isOpen && TypeEnum.OPEN.equals(type.getType())) {
                isOpen = true;
                sb.append(convertUnixTimeToAmPm(type.getValue())).append(" - ");
            } else if (isOpen && TypeEnum.CLOSE.equals(type.getType())) {
                isOpen = false;
                sb.append(convertUnixTimeToAmPm(type.getValue())).append(", ");
            } else {
                throw new IllegalArgumentException("Invalid value for day: " + day);
            }
        }
        if (isOpen) {
            sb.append(addNextDayValue(map, day));
        }
        return sb.toString().replaceAll(", $", "");
    }

    private String addNextDayValue(EnumMap<Day, List<Type>> map, Day day) {
        Day nextDay = Day.of(day.getValue() + 1);
        if (map.containsKey(nextDay)
                && !map.get(nextDay).isEmpty()
                && map.get(nextDay).get(0).getType().equals(TypeEnum.CLOSE)) {
            Type type = map.get(nextDay).get(0);
            return convertUnixTimeToAmPm(type.getValue());
        } else {
            throw new IllegalArgumentException("Invalid value for day:" + nextDay);
        }
    }

}
