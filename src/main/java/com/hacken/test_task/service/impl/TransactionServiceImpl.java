package com.hacken.test_task.service.impl;

import com.hacken.test_task.domain.Transaction;
import com.hacken.test_task.dto.TransactionDTO;
import com.hacken.test_task.repository.TransactionRepository;
import com.hacken.test_task.service.PropertyService;
import com.hacken.test_task.service.TransactionService;
import com.hacken.test_task.service.utils.TransactionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final PropertyService propertyService;
    private final Web3j web3j;


    public TransactionServiceImpl(TransactionRepository transactionRepository, PropertyService propertyService, Web3j web3j) {
        this.transactionRepository = transactionRepository;
        this.propertyService = propertyService;
        this.web3j = web3j;
    }

    @Override
    @Transactional
    public void processBlock() {
        var blockNumber = getStartingBlockNumber();

        if (blockNumber == null) {
            return;
        }

        try {
            var block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), true)
                    .send()
                    .getBlock();

            processAndSaveTransactions(block);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<BigInteger> getBlocksIds() {
        log.info("Retrieving all blocks numbers...");
        var numbers = transactionRepository.getBlocksNumbers().stream()
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));

        log.info("Retrieved {} numbers.", numbers.size());
        return numbers;
    }

    @Override
    public List<TransactionDTO> getTransactions(BigInteger blockNumber) {
        log.info("Retrieving transactions by block number...");
        var transactions = transactionRepository.getTransactionsByBlockNumber(blockNumber).stream()
                .map(TransactionUtil::mapToTransactionDTO)
                .sorted(Comparator.comparing(TransactionDTO::getBlockNumber))
                .toList();

        log.info("Retrieved {} transactions.", transactions.size());
        return transactions;
    }

    @Override
    public List<TransactionDTO> getTransactionsFrom(String address) {
        log.info("Retrieving transactions by from address...");
        var transactions = transactionRepository.getTransactionsByFromAddress(address).stream()
                .map(TransactionUtil::mapToTransactionDTO)
                .sorted(Comparator.comparing(TransactionDTO::getBlockNumber))
                .toList();

        log.info("Retrieved {} transactions.", transactions.size());
        return transactions;
    }

    @Override
    public List<TransactionDTO> getTransactionsTo(String address) {
        log.info("Retrieving transactions by to address...");
        var transactions = transactionRepository.getTransactionsByToAddress(address).stream()
                .map(TransactionUtil::mapToTransactionDTO)
                .sorted(Comparator.comparing(TransactionDTO::getBlockNumber))
                .toList();

        log.info("Retrieved {} transactions.", transactions.size());
        return transactions;
    }

    @Override
    public TransactionDTO getTransaction(String hash) {
        log.info("Retrieving transaction by hash...");
        var transaction = transactionRepository.getTransactionByHash(hash);

        log.info("Retrieved transaction {}.", transaction);
        return Objects.nonNull(transaction) ? TransactionUtil.mapToTransactionDTO(transaction) : null;
    }

    private BigInteger getStartingBlockNumber() {
        var lastBlockNumber = propertyService.getLastBlock();
        var lastBlockFromNetwork = getLastBlockFromNetwork();

        if (lastBlockNumber == null) {
            return lastBlockFromNetwork;
        }
        if (lastBlockNumber.compareTo(lastBlockFromNetwork) == 0) {
            log.info("Block number {} was already processed.", lastBlockNumber);
            return null;
        }

        return lastBlockNumber.add(BigInteger.ONE);
    }

    private BigInteger getLastBlockFromNetwork() {
        log.info("Getting last block number from network...");
        try {
            var lastBlockNumber = web3j.ethBlockNumber().send().getBlockNumber();
            log.info("Last block number from network: {}", lastBlockNumber);

            return lastBlockNumber;
        } catch (IOException e) {
            throw new RuntimeException("Failed to get the latest block from network.", e);
        }
    }

    private void processAndSaveTransactions(EthBlock.Block block) {
        log.info("Processing transactions for block number {} ...", block.getNumber());
        List<Transaction> transactions = block.getTransactions().stream()
                .map(tx -> (EthBlock.TransactionObject) tx.get())
                .map(TransactionUtil::mapToTransaction)
                .toList();

        log.info("Saving all {} transactions from block {} ...", transactions.size(), block.getNumber());
        transactionRepository.saveAll(transactions);
        propertyService.saveLastBlock(block.getNumber());
    }

}