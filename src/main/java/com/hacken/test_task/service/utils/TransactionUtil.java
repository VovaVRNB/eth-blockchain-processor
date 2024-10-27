package com.hacken.test_task.service.utils;

import com.hacken.test_task.domain.Transaction;
import com.hacken.test_task.dto.TransactionDTO;
import org.web3j.protocol.core.methods.response.EthBlock;

public class TransactionUtil {

    private TransactionUtil() {
    }

    public static Transaction mapToTransaction(EthBlock.TransactionObject transactionObject) {
        return Transaction.builder()
                .hash(transactionObject.getHash())
                .blockHash(transactionObject.getBlockHash())
                .blockNumber(transactionObject.getBlockNumber())
                .fromAddress(transactionObject.getFrom())
                .toAddress(transactionObject.getTo())
                .value(transactionObject.getValue())
                .gas(transactionObject.getGas())
                .gasPrice(transactionObject.getGasPrice())
                .nonce(transactionObject.getNonce())
                .input(transactionObject.getInput())
                .transactionIndex(transactionObject.getTransactionIndex())
                .creates(transactionObject.getCreates())
                .raw(transactionObject.getRaw())
                .r(transactionObject.getR())
                .s(transactionObject.getS())
                .v(transactionObject.getV())
                .build();
    }

    public static TransactionDTO mapToTransactionDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .hash(transaction.getHash())
                .blockHash(transaction.getBlockHash())
                .blockNumber(transaction.getBlockNumber())
                .fromAddress(transaction.getFromAddress())
                .toAddress(transaction.getToAddress())
                .value(transaction.getValue())
                .gas(transaction.getGas())
                .gasPrice(transaction.getGasPrice())
                .nonce(transaction.getNonce())
                .input(transaction.getInput())
                .creates(transaction.getCreates())
                .raw(transaction.getRaw())
                .build();
    }
}
