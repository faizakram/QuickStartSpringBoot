package com.app;

import lombok.Data;

import java.io.ByteArrayOutputStream;
@Data
public class Download {
    private String name;
    private byte[] bytes;
}
