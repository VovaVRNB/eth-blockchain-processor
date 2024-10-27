package com.hacken.test_task.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Entity
@Table(name = "transactions")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "hash")
    private String hash;

    @Column(name = "block_hash")
    private String blockHash;

    @Column(name = "block_number")
    private BigInteger blockNumber;

    @Column(name = "from_address")
    private String fromAddress;

    @Column(name = "to_address")
    private String toAddress;

    @Column(name = "value")
    private BigInteger value;

    @Column(name = "gas")
    private BigInteger gas;

    @Column(name = "gas_price")
    private BigInteger gasPrice;

    @Column(name = "nonce")
    private BigInteger nonce;

    @Column(name = "input")
    private String input;

    @Column(name = "transaction_index")
    private BigInteger transactionIndex;

    @Column(name = "creates")
    private String creates;

    @Column(name = "raw")
    private String raw;

    @Column(name = "r")
    private String r;

    @Column(name = "s")
    private String s;

    @Column(name = "v")
    private Long v;

}
