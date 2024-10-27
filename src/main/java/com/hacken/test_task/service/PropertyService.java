package com.hacken.test_task.service;

import java.math.BigInteger;

public interface PropertyService {
    void saveLastBlock(BigInteger blockId);
    BigInteger getLastBlock();
}
