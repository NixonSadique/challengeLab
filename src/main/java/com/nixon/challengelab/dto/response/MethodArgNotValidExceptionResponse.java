package com.nixon.challengelab.dto.response;

import java.util.List;

public record MethodArgNotValidExceptionResponse(
        StandardErrorResponse details,
        List<ValidationErrors> fields
) {
}


