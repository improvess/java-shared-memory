package com.improvess.shared.connector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

        URL url = LoadLib.class.getResource("/libjava_shared_memory_lib.so");
        String absoluteDiskPath = url.getPath();

        File source = new File(absoluteDiskPath);

        if (!source.exists()) {
            throw new IllegalArgumentException(absoluteDiskPath + " not found");
        }
        if (!source.isFile()) {
            throw new IllegalArgumentException(absoluteDiskPath + " is not a file");
        }

        Random random = new Random();

        File destFolder = new File(tempDir, Integer.toString(random.nextInt(65356)));

        destFolder.mkdir();

        File dest = new File(destFolder, "/libjava_shared_memory_lib.so");

        try (
                InputStream in = new BufferedInputStream(
                        new FileInputStream(source));
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
