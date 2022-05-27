package my.project.openinghours.controllers;


import lombok.AllArgsConstructor;
import my.project.openinghours.controllers.inbound.Day;
import my.project.openinghours.controllers.inbound.Type;
import my.project.openinghours.services.OpeningHoursService;
import my.project.openinghours.utils.MapValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@AllArgsConstructor
public class OpeningHoursController {

    private MapValidator mapValidator;
    private OpeningHoursService service;


    @GetMapping("/opening-hours")
    public ResponseEntity<Map<String, String>> getOpeningHours(@RequestBody EnumMap<Day, List<Type>> dayListEnumMap) {
        validateMap(dayListEnumMap);
        return ResponseEntity.ok().body(service.convertToReadableFormat(dayListEnumMap));
    }

    private void validateMap(EnumMap<Day, List<Type>> dayListEnumMap) {
        final Errors errors = new MapBindingResult(new HashMap<>(), "dayListEnumMap");
        mapValidator.validate(dayListEnumMap, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.getAllErrors().get(0).getDefaultMessage());
        }
    }

}
