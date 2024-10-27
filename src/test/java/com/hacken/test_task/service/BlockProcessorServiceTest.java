package com.hacken.test_task.service;

import com.hacken.test_task.exception.SystemException;
import com.hacken.test_task.service.impl.BlockProcessorServiceImpl;
import com.hacken.test_task.service.utils.TaskStateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BlockProcessorServiceTest {

    @Mock
    private TaskStateUtil taskStateUtil;

    @Mock
    private ScheduledExecutorService schedulerMock;

    @InjectMocks
    private BlockProcessorServiceImpl blockProcessorService;

    private final int delay = 5;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(blockProcessorService, "delay", delay);
        ReflectionTestUtils.setField(blockProcessorService, "scheduler", schedulerMock);
    }

    @Test
    void testStartWhenAlreadyRunning() {
        when(taskStateUtil.isRunning()).thenReturn(true);

        SystemException exception = assertThrows(SystemException.class, () -> blockProcessorService.start());
        assertEquals("Block Processor was already started.", exception.getMessage());

        verify(taskStateUtil, never()).start();
        verify(schedulerMock, never()).scheduleAtFixedRate(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class));
    }

    @Test
    void testStartWhenNotRunning() {
        when(taskStateUtil.isRunning()).thenReturn(false);

        blockProcessorService.start();

        verify(taskStateUtil).start();
        verify(schedulerMock).scheduleAtFixedRate(any(Runnable.class), eq(0L), eq((long) delay), eq(TimeUnit.SECONDS));
    }

    @Test
    void testStopWhenAlreadyStopped() {
        when(taskStateUtil.isRunning()).thenReturn(false);

        SystemException exception = assertThrows(SystemException.class, () -> blockProcessorService.stop());
        assertEquals("Block Processor was already stopped.", exception.getMessage());

        verify(taskStateUtil, never()).stop();
        verify(schedulerMock, never()).shutdownNow();
    }

    @Test
    void testStopWhenRunning() {
        when(taskStateUtil.isRunning()).thenReturn(true);

        blockProcessorService.stop();

        verify(taskStateUtil).stop();
        verify(schedulerMock).shutdownNow();
    }

    @Test
    void testPauseWhenAlreadyPaused() {
        when(taskStateUtil.isPaused()).thenReturn(true);

        SystemException exception = assertThrows(SystemException.class, () -> blockProcessorService.pause(5));
        assertEquals("Block Processor was already paused.", exception.getMessage());

        verify(taskStateUtil, never()).pause();
        verify(schedulerMock, never()).schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
    }

    @Test
    void testPauseWhenNotRunning() {
        when(taskStateUtil.isPaused()).thenReturn(false);
        when(taskStateUtil.isRunning()).thenReturn(false);

        SystemException exception = assertThrows(SystemException.class, () -> blockProcessorService.pause(5));
        assertEquals("Block Processor was not started yet.", exception.getMessage());

        verify(taskStateUtil, never()).pause();
        verify(schedulerMock, never()).schedule(any(Runnable.class), anyLong(), any(TimeUnit.class));
    }

    @Test
    void testPauseWhenRunning() {
        when(taskStateUtil.isPaused()).thenReturn(false);
        when(taskStateUtil.isRunning()).thenReturn(true);

        int pauseSeconds = 5;
        blockProcessorService.pause(pauseSeconds);

        verify(taskStateUtil).pause();
        verify(schedulerMock).schedule(any(Runnable.class), eq((long) pauseSeconds), eq(TimeUnit.SECONDS));
    }

    @Test
    void testResumeWhenNotPaused() {
        when(taskStateUtil.isPaused()).thenReturn(false);

        SystemException exception = assertThrows(SystemException.class, () -> blockProcessorService.resume());
        assertEquals("Block Processor was not paused.", exception.getMessage());

        verify(taskStateUtil, never()).resume();
    }

    @Test
    void testResumeWhenPaused() {
        when(taskStateUtil.isPaused()).thenReturn(true);

        blockProcessorService.resume();

        verify(taskStateUtil).resume();
    }
}
