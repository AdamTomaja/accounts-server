package com.cydercode.dto.stats;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsResponse {

  private long totalAccounts;
  private long verifiedAccounts;
  private long unverifiedAccounts;
  private long lockedAccounts;
}
