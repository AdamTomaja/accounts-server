package com.cydercode.dto.query;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryAccountResponse {

    private String username;
    private String email;
    private boolean emailVerified;
    private Instant createdAt;
    private Instant updatedAt;
}
