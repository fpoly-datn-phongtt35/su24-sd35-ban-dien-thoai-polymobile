package com.poly.polystore.core.client.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeSPCartRequest {
    private int newID;
    private int oldID;
}
