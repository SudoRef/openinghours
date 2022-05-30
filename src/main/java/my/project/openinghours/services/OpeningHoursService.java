package my.project.openinghours.services;

import my.project.openinghours.model.Status;
import my.project.openinghours.model.UnixTime;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.capitalize;

@Service
public class OpeningHoursService {

    private static final String PATTERN = "hh:mm a";
    private static final String CLOSED = "Closed";
    private static final String UTC = "UTC";

    public Map<String, String> convertToReadableFormat(Map<DayOfWeek, List<UnixTime>> dayListEnumMap) {
        Map<String, String> readableFormatMap = new LinkedHashMap<>();
        for (Map.Entry<DayOfWeek, List<UnixTime>> entry : dayListEnumMap.entrySet()) {
            String key = capitalize(entry.getKey().name().toLowerCase());
            String val;
            if (entry.getValue().isEmpty()) {
                val = CLOSED;
            } else {
                val = buildDaySchedule(dayListEnumMap, entry.getKey());
            }
            readableFormatMap.put(key, val);
        }
        return readableFormatMap;
    }

    public String convertTypeToAmPm(int Type) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return Instant.ofEpochSecond(Type)
                .atZone(ZoneId.of(UTC))
                .format(formatter).toUpperCase();
    }

    private String buildDaySchedule(Map<DayOfWeek, List<UnixTime>> map, DayOfWeek day) {
        List<UnixTime> unixTimesTypes = map.get(day);
        StringBuilder sb = new StringBuilder();
        boolean isOpen = false;
        for (int i = 0; i < unixTimesTypes.size(); i++) {
            UnixTime type = unixTimesTypes.get(i);
            if (i == 0 && Status.CLOSE.equals(type.getType())) {
                continue;
            }
            if (!isOpen && Status.OPEN.equals(type.getType())) {
                isOpen = true;
                sb.append(convertTypeToAmPm(type.getValue())).append(" - ");
            } else if (isOpen && Status.CLOSE.equals(type.getType())) {
                isOpen = false;
                sb.append(convertTypeToAmPm(type.getValue())).append(", ");
            } else {
                throw new IllegalArgumentException("Invalid value for day: " + day);
            }
        }
        if (isOpen) {
            sb.append(addNextDayValue(map, day));
        }
        return sb.toString().replaceAll(", $", "");
    }

    private String addNextDayValue(Map<DayOfWeek, List<UnixTime>> dayListEnumMap, DayOfWeek day) {
        DayOfWeek nextDay = DayOfWeek.of(day.getValue() + 1);
        if (dayListEnumMap.containsKey(nextDay)
                && !dayListEnumMap.get(nextDay).isEmpty()
                && dayListEnumMap.get(nextDay).get(0).getType().equals(Status.CLOSE)) {
            UnixTime unixTimeType = dayListEnumMap.get(nextDay).get(0);
            return convertTypeToAmPm(unixTimeType.getValue());
        } else {
            throw new IllegalArgumentException("Invalid value for day:" + nextDay);
        }
    }

}
