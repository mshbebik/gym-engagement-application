package com.gym.engagement.factory;

import org.modelmapper.ModelMapper;

public final class ModelMapperFactory {
    private ModelMapperFactory() {}

    public static ModelMapper create() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return mapper;
    }
}
