package com.improvess.shared.examples;

import com.improvess.shared.connector.Connector;

import java.nio.ByteBuffer;

public class CheckConnectorWrite {

    static {
        System.loadLibrary("java_shared_memory_lib");
    }

    public static void main(String[] args) {
        System.out.println("Checking Connector Write:");
        Connector connector = new Connector();

        int shm_id = connector.initialize_shared_buffer(0xd7a6, 100);

        ByteBuffer buffer = connector.get_shared_buffer(shm_id);

        if (buffer != null) {
            
            for (int i = 0; i < 8; ++i) {
                buffer.put(i, (byte)i);
            }
            
        } else {
            System.err.println("Buffer is null");
        }

        System.out.println("End");

    }
    
}
