package my.project.openinghours.utils;

import my.project.openinghours.controllers.inbound.Day;
import my.project.openinghours.controllers.inbound.Type;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.EnumMap;
import java.util.List;


@Component
public class MapValidator implements Validator {

    public static final int MIN_TIME_VALUE = 0;
    public static final int MAX_TIME_VALUE = 86399;

    @Override
    public boolean supports(Class<?> clazz) {
        return EnumMap.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final EnumMap<Day, List<Type>> enumMap = (EnumMap<Day, List<Type>>) target;
        enumMap.values().forEach(types -> getErrors(errors, types));
    }

    private void getErrors(Errors errors, List<Type> types) {
        for (int i = 0; i < types.size(); i++) {
            Type type = types.get(i);
            if (type.getValue() == null || type.getValue() < MIN_TIME_VALUE || type.getValue() > MAX_TIME_VALUE) {
                errors.reject("0", "Invalid type format. Type cannot be null, negative or bigger than " + MAX_TIME_VALUE);
                break;
            }
            if (i < types.size() - 1) {
                if (type.getValue() >= types.get(i + 1).getValue()) {
                    errors.reject("0", "Invalid type format. Previous value cannot be later than next one");
                    break;
                }
            }
        }
    }
}
