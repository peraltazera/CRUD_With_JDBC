package com.vpereira.service;

import com.vpereira.core.domain.Default;
import com.vpereira.repository.DefaultRepository;
import com.vpereira.repository.generic.IGenericRepository;
import com.vpereira.service.generic.GenericService;

public class DefaultService extends GenericService<Default, Long> implements IDefaultService{
    public DefaultService(DefaultRepository defaultRepository) {
        super(defaultRepository);
    }
}
