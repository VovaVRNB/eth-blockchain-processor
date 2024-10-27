package com.hacken.test_task.service;

import com.hacken.test_task.dto.TransactionDTO;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public interface TransactionService {
    void processBlock();
    Set<BigInteger> getBlocksIds();
    List<TransactionDTO> getTransactions(BigInteger blockNumber);
    List<TransactionDTO> getTransactionsFrom(String address);
    List<TransactionDTO> getTransactionsTo(String address);
    TransactionDTO getTransaction(String hash);
}
