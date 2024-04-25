package com.mediasoft.warehouse.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    private final String FILENAME = "data.txt";
    private final BufferedWriter writer;
    public FileUtil() {
        try {
            writer = new BufferedWriter(new FileWriter(FILENAME));
            writer.write("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeDataToFile(String string){
        try {
            writer.append(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void close(){
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
