package com.improvess.shared.connector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Random;

public class LoadLib {

    public static void load() throws IOException {
        String tmpDirsLocation = System.getProperty("java.io.tmpdir");
        if (tmpDirsLocation == null) {
            throw new IllegalArgumentException("system env java.io.tmpdir not set");
        }
        File tempDir = new File(tmpDirsLocation);
        if (!tempDir.isDirectory()) {
            throw new IllegalArgumentException(tmpDirsLocation + " is not a folder");
        }

        Random random = new Random();

        File destFolder = new File(tempDir, Integer.toString(random.nextInt(65356)));

        destFolder.mkdir();

        File dest = new File(destFolder, "/libjava_shared_memory_lib.so");

        try (
                InputStream in = LoadLib.class.getResourceAsStream("/libjava_shared_memory_lib.so");
                OutputStream out = new BufferedOutputStream(
                        new FileOutputStream(dest))) {

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        }

        System.load(dest.getAbsolutePath());

    }

}
