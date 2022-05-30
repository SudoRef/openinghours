package my.project.openinghours.controllers;


import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import lombok.AllArgsConstructor;
import my.project.openinghours.model.UnixTime;
import my.project.openinghours.services.OpeningHoursService;
import my.project.openinghours.utils.MapValidator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class OpeningHoursController implements my.project.openinghours.api.OpeningHoursApi {

    private MapValidator mapValidator;
    private OpeningHoursService service;

    @Override
    public ResponseEntity<Map<String, String>> postUnixTimeAndGetOpeningHours(Map<String, List<UnixTime>> requestBody) {
        EnumMap<DayOfWeek, List<UnixTime>> enumSchedules = convertRequestBodyToEnumMap(requestBody);
        validateInboundMap(enumSchedules);
        return ResponseEntity.ok().body(service.convertToReadableFormat(enumSchedules));
    }

    private void validateInboundMap(Map<DayOfWeek, List<UnixTime>> enumSchedules) {
        final Errors errors = new MapBindingResult(new HashMap<>(), "dayListEnumMap");
        mapValidator.validate(enumSchedules, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getAllErrors().get(0).getDefaultMessage());
        }
    }

    private EnumMap<DayOfWeek, List<UnixTime>> convertRequestBodyToEnumMap(Map<String, List<UnixTime>> requestBody) {
        EnumMap<DayOfWeek, List<UnixTime>> enumMap = new EnumMap<>(DayOfWeek.class);
        requestBody.forEach((key, value) -> enumMap.put(DayOfWeek.valueOf(key.toUpperCase()), value));
        return enumMap;
    }

}
