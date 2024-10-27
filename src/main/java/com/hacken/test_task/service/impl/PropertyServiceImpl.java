package com.hacken.test_task.service.impl;

import com.hacken.test_task.repository.PropertyRepository;
import com.hacken.test_task.service.PropertyService;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;

@Slf4j
@Service
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final String lastBlockIdKey;

    public PropertyServiceImpl(PropertyRepository propertyRepository, @Value("${system.last-block-id-key}") String lastBlockIdKey) {
        this.propertyRepository = propertyRepository;
        this.lastBlockIdKey = lastBlockIdKey;
    }

    @Override
    public void saveLastBlock(BigInteger blockId) {
        log.info("Saving last block id: {} ...", blockId);

        var property = propertyRepository.getPropertyByKey(lastBlockIdKey);
        property.setValue(blockId.toString());

        var savedProperty = propertyRepository.save(property);
        log.info("Property saved: {}", savedProperty);
    }

    @Override
    public BigInteger getLastBlock() {
        log.info("Getting last saved block id...");
        var property = propertyRepository.getPropertyByKey(lastBlockIdKey);
        log.info("Get last saved block property: {}", property);

        if (StringUtils.hasLength(property.getValue())) {
            return new BigInteger(property.getValue());
        }

        return null;
    }
}
