package com.hacken.test_task.service;

import com.hacken.test_task.domain.Transaction;
import com.hacken.test_task.dto.TransactionDTO;
import com.hacken.test_task.repository.TransactionRepository;
import com.hacken.test_task.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PropertyService propertyService;

    @Mock
    private Web3j web3j;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessBlock() throws Exception {
        BigInteger blockNumber = BigInteger.ONE;

        Request requestMock = mock(Request.class);
        EthBlock ethBlockMock = mock(EthBlock.class);
        EthBlock.Block blockMock = mock(EthBlock.Block.class);

        when(web3j.ethGetBlockByNumber(any(DefaultBlockParameter.class), eq(true))).thenReturn(requestMock);
        when(requestMock.send()).thenReturn(ethBlockMock);
        when(ethBlockMock.getBlock()).thenReturn(blockMock);
        when(blockMock.getNumber()).thenReturn(blockNumber);

        Request blockNumberRequestMock = mock(Request.class);
        EthBlockNumber ethBlockNumberMock = mock(EthBlockNumber.class);

        when(web3j.ethBlockNumber()).thenReturn(blockNumberRequestMock);
        when(blockNumberRequestMock.send()).thenReturn(ethBlockNumberMock);
        when(ethBlockNumberMock.getBlockNumber()).thenReturn(blockNumber);

        transactionService.processBlock();

        verify(transactionRepository).saveAll(anyList());
        verify(propertyService).saveLastBlock(blockNumber);
    }

    @Test
    void testGetBlocksIds() {
        when(transactionRepository.getBlocksNumbers()).thenReturn(Set.of(BigInteger.valueOf(1), BigInteger.valueOf(2)));

        Set<BigInteger> result = transactionService.getBlocksIds();

        assertEquals(Set.of(BigInteger.valueOf(1), BigInteger.valueOf(2)), result);
    }

    @Test
    void testGetTransactions() {
        BigInteger blockNumber = BigInteger.ONE;
        when(transactionRepository.getTransactionsByBlockNumber(blockNumber)).thenReturn(Collections.emptyList());

        List<TransactionDTO> result = transactionService.getTransactions(blockNumber);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transactionRepository).getTransactionsByBlockNumber(blockNumber);
    }

    @Test
    void testGetTransactionsFrom() {
        String address = "0x123";
        when(transactionRepository.getTransactionsByFromAddress(address)).thenReturn(Collections.emptyList());

        List<TransactionDTO> result = transactionService.getTransactionsFrom(address);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transactionRepository).getTransactionsByFromAddress(address);
    }

    @Test
    void testGetTransactionsTo() {
        String address = "0x123";
        when(transactionRepository.getTransactionsByToAddress(address)).thenReturn(Collections.emptyList());

        List<TransactionDTO> result = transactionService.getTransactionsTo(address);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transactionRepository).getTransactionsByToAddress(address);
    }

    @Test
    void testGetTransaction() {
        String hash = "0xabc";
        when(transactionRepository.getTransactionByHash(hash)).thenReturn(new Transaction());

        TransactionDTO result = transactionService.getTransaction(hash);

        assertNotNull(result);
        verify(transactionRepository).getTransactionByHash(hash);
    }

    @Test
    void testGetTransactionWhenNotFound() {
        String hash = "0xabc";
        when(transactionRepository.getTransactionByHash(hash)).thenReturn(null);

        TransactionDTO result = transactionService.getTransaction(hash);

        assertNull(result);
        verify(transactionRepository).getTransactionByHash(hash);
    }
}
