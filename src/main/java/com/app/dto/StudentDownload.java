package com.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDownload {
    @JsonProperty("Regno")
    private String regNo;
    @JsonProperty("Certificate No")
    private String certificateNo;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Guardian Name")
    private String guardianName;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("Mobile")
    private String mobile;
}