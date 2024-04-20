package com.mjrafg.springsecurestarterkit.utils;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperUtil {

    private final ModelMapper modelMapper;

    public ModelMapperUtil() {
        this.modelMapper = new ModelMapper();
        // Skip null values
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.getConfiguration().setFieldAccessLevel(AccessLevel.PRIVATE);
        modelMapper.getConfiguration().setMethodAccessLevel(AccessLevel.PRIVATE);
    }

    public <S, D> D map(S source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }
}
