package com.hacken.test_task.web.rest;

import com.hacken.test_task.service.ProcessorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/block-processor")
public class BlockProcessorController {
    private final ProcessorService processorService;

    public BlockProcessorController(ProcessorService processorService) {
        this.processorService = processorService;
    }

    @GetMapping("/start")
    @Operation(description = "This api is used to start the block processor.")
    public void start() {
        processorService.start();
    }

    @GetMapping("/stop")
    @Operation(description = "This api is used to stop the block processor.")
    public void stop() {
        processorService.stop();
    }

    @GetMapping("/pause")
    @Operation(description = "This api is used to pause the block processor for the specified number of seconds.")
    public void pause(@RequestParam(value = "seconds") Integer seconds) {
        processorService.pause(seconds);
    }

    @GetMapping("/resume")
    @Operation(description = "This api is used to resume the block processor from pause.")
    public void resume() {
        processorService.resume();
    }
}
