package com.hacken.test_task.service.impl;

import com.hacken.test_task.exception.SystemException;
import com.hacken.test_task.service.ProcessorService;
import com.hacken.test_task.service.TransactionService;
import com.hacken.test_task.service.utils.TaskStateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class BlockProcessorServiceImpl implements ProcessorService {

    private final TaskStateUtil taskStateUtil;
    private final TransactionService transactionService;
    private final Integer delay;

    private ScheduledExecutorService scheduler;

    public BlockProcessorServiceImpl(TaskStateUtil taskStateUtil, TransactionService transactionService, @Value("${system.delay}") Integer delay) {
        this.taskStateUtil = taskStateUtil;
        this.transactionService = transactionService;
        this.delay = delay;
    }

    @Override
    public void start() {
        if (taskStateUtil.isRunning()) {
            throw new SystemException("Block Processor was already started.");
        }
        log.info("Starting Block Processor...");

        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
        }

        if (!taskStateUtil.isRunning()) {
            taskStateUtil.start();
            scheduler.scheduleAtFixedRate(this::processBlocks, 0, delay, TimeUnit.SECONDS);

            log.info("Block Processor was started.");
        }
    }

    @Override
    public void stop() {
        if (!taskStateUtil.isRunning()) {
            throw new SystemException("Block Processor was already stopped.");
        }

        log.info("Stopping Block Processor...");

        taskStateUtil.stop();
        scheduler.shutdownNow();

        log.info("Block Processor was stopped.");
    }

    @Override
    public void pause(Integer seconds) {
        if (taskStateUtil.isPaused()) {
            throw new SystemException("Block Processor was already paused.");
        }

        if (!taskStateUtil.isRunning()) {
            throw new SystemException("Block Processor was not started yet.");
        }

        if (taskStateUtil.isRunning()) {
            log.info("Pausing Block Processor for {} seconds...", seconds);

            taskStateUtil.pause();
            scheduler.schedule(this::resume, seconds, TimeUnit.SECONDS);

            log.info("Block Processor was paused for {} seconds.", seconds);
        }
    }

    @Override
    public void resume() {
        if (!taskStateUtil.isPaused()) {
          throw new SystemException("Block Processor was not paused.")  ;
        }

        log.info("Resuming Block Processor...");
        taskStateUtil.resume();
        log.info("Block Processor was resumed.");
    }

    private void processBlocks() {
        if (taskStateUtil.isRunning() && !taskStateUtil.isPaused()) {
            transactionService.processBlock();
        }
    }
}
