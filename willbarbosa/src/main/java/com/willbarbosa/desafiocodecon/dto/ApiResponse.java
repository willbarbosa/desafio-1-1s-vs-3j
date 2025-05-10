package com.willbarbosa.desafiocodecon.dto;

import java.time.Instant;

public record ApiResponse<T>(Instant timestamp, long execution_time_ms, T data) {
    public ApiResponse(long execution_time_ms, T data) {
        this(Instant.now(), execution_time_ms, data);
    }
}