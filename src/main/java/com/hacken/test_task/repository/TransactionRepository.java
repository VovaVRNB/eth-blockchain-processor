package com.hacken.test_task.repository;

import com.hacken.test_task.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("select distinct t.blockNumber from Transaction as t")
    Set<BigInteger> getBlocksNumbers();

    Transaction getTransactionByHash(String hash);
    List<Transaction> getTransactionsByBlockNumber(BigInteger blockNumber);
    List<Transaction> getTransactionsByFromAddress(String address);
    List<Transaction> getTransactionsByToAddress(String address);
}
