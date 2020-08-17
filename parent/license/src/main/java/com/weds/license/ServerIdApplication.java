package com.weds.license;

import com.weds.core.license.ServerIDGenerator;

import java.io.File;
import java.io.PrintWriter;

public class ServerIdApplication {
    private static final String PRODUCT_NAME = "WEDS";
    private static final String SERVER_PATH = "server.txt";


    public static void main(String[] args) throws Exception {
        String[] ids = ServerIDGenerator.make(PRODUCT_NAME);
        File file = new File(SERVER_PATH);
        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter pw = new PrintWriter(file);
        pw.println(ids[0]);
        pw.close();
        System.out.println("机器码\n\r" + ids[0]);
    }
}
