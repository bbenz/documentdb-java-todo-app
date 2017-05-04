package com.microsoft.azure.appservice.sql.sample.model;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class TodoItem {
    private String category;
    private boolean complete;
    private String id;
    private String name;
}
