package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardResponse<T> {
    private String responseCode;          // The response code (e.g., "E1005")
    private String responseDescription;   // The response description (e.g., "Invalid method argument.")
    private T response;                   // Generic type to hold any object (e.g., data or error details)
    private boolean isOkay;               // Indicates success (true) or failure (false)
}