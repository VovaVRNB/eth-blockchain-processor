package com.hacken.test_task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
public class TransactionDTO {
    private String hash;
    private String blockHash;
    private BigInteger blockNumber;
    private String fromAddress;
    private String toAddress;
    private BigInteger value;
    private BigInteger gas;
    private BigInteger gasPrice;
    private BigInteger nonce;
    private String input;
    private String creates;
    private String raw;
}
