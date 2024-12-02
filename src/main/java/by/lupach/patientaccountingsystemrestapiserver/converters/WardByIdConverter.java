package by.lupach.patientaccountingsystemrestapiserver.converters;

import by.lupach.patientaccountingsystemrestapiserver.entities.Ward;
import by.lupach.patientaccountingsystemrestapiserver.services.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WardByIdConverter implements Converter<String, Ward> {

    @Autowired
    WardService wardService;

    @Override
    public Ward convert(String source) {
        return wardService.getById(Integer.parseInt(source)).get();
    }
}
