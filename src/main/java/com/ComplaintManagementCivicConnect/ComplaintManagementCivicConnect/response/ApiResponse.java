package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Every endpoint returns this shape:
 * {
 *   "success"  : true/false,
 *   "message"  : "...",
 *   "data"     : { ... },      ← null on errors
 *   "timestamp": "..."
 * }
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean       success;
    private final String        message;
    private final T             data;
    private final LocalDateTime timestamp;

    // ── Success ───────────────────────────────────────────────────────────────

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true).message(message).data(data)
                .timestamp(LocalDateTime.now()).build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return success("Success", data);
    }

    // ── Error ─────────────────────────────────────────────────────────────────

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false).message(message)
                .timestamp(LocalDateTime.now()).build();
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
                .success(false).message(message).data(data)
                .timestamp(LocalDateTime.now()).build();
    }
}
