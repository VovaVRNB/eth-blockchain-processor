package com.hacken.test_task.service;

import com.hacken.test_task.domain.Property;
import com.hacken.test_task.repository.PropertyRepository;
import com.hacken.test_task.service.impl.PropertyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PropertyServiceTest {
    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    private final String lastBlockIdKey = "lastBlockIdKey";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        propertyService = new PropertyServiceImpl(propertyRepository, lastBlockIdKey);
    }

    @Test
    void testSaveLastBlock() {
        BigInteger blockId = new BigInteger("12345");
        Property property = new Property();
        property.setKey(lastBlockIdKey);

        when(propertyRepository.getPropertyByKey(lastBlockIdKey)).thenReturn(property);
        when(propertyRepository.save(property)).thenReturn(property);

        propertyService.saveLastBlock(blockId);

        assertEquals(blockId.toString(), property.getValue());
        verify(propertyRepository).getPropertyByKey(lastBlockIdKey);
        verify(propertyRepository).save(property);
    }

    @Test
    void testGetLastBlockWhenValueExists() {
        String value = "12345";
        Property property = new Property();
        property.setKey(lastBlockIdKey);
        property.setValue(value);

        when(propertyRepository.getPropertyByKey(lastBlockIdKey)).thenReturn(property);

        BigInteger result = propertyService.getLastBlock();

        assertNotNull(result);
        assertEquals(new BigInteger(value), result);
        verify(propertyRepository).getPropertyByKey(lastBlockIdKey);
    }

    @Test
    void testGetLastBlockWhenValueIsEmpty() {
        Property property = new Property();
        property.setKey(lastBlockIdKey);
        property.setValue("");

        when(propertyRepository.getPropertyByKey(lastBlockIdKey)).thenReturn(property);

        BigInteger result = propertyService.getLastBlock();

        assertNull(result);
        verify(propertyRepository).getPropertyByKey(lastBlockIdKey);
    }
}
