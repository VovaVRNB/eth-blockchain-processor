package com.hacken.test_task.service;

public interface ProcessorService {
    void start();
    void stop();
    void pause(Integer seconds);
    void resume();
}
