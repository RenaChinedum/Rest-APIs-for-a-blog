package com.task9.task9.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ExceptionResponse {
    private LocalDate errorTime;
    private String errorMessage;
    private String errorPath;
    private Integer errorStatusCode;
}
