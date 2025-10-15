package com.lldubercab.model.cab;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class CabNotificationResponse {
    private int cabId;
    private boolean response;
}
