package com.hacken.test_task.web.rest;

import com.hacken.test_task.dto.TransactionDTO;
import com.hacken.test_task.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/blocks")
    @Operation(description = "This api is used to retrieving blocks numbers from db.")
    public Set<BigInteger> getBlocksIds() {
        return transactionService.getBlocksIds();
    }

    @GetMapping("/block/{blockNumber}")
    @Operation(description = "This api is used to retrieving transactions by block number from db.")
    public List<TransactionDTO> getTransactionsByBlockNumber(@PathVariable(value = "blockNumber") BigInteger blockNumber) {
        return transactionService.getTransactions(blockNumber);
    }

    @GetMapping("/transaction/{hash}")
    @Operation(description = "This api is used to retrieving transaction by hash from db.")
    public TransactionDTO getTransactionByHash(@PathVariable(value = "hash") String hash) {
        return transactionService.getTransaction(hash);
    }

    @GetMapping("/from/{from}")
    @Operation(description = "This api is used to retrieving transactions by sender's address from db.")
    public List<TransactionDTO> getTransactionsByFromAddress(@PathVariable(value = "from") String from) {
        return transactionService.getTransactionsFrom(from);
    }

    @GetMapping("/to/{to}")
    @Operation(description = "This api is used to retrieving transactions by recipient address from db.")
    public List<TransactionDTO> getTransactionsByToAddress(@PathVariable(value = "to") String to) {
        return transactionService.getTransactionsTo(to);
    }
}
