/**
 * Author: Luiz Carlos d'Oleron
 * doleron@gmail.com
 * 
 * DISCLAIMER: This software is provided “AS IS”, without warranty of any kind, express or implied, 
 * including but not limited to the warranties of merchantability, fitness for a particular purpose, 
 * and noninfringement. In no event shall the author or copyright holders be liable for any claim, 
 * damages, or other liability, whether in an action of contract, 
 * tort, or otherwise, arising from, out of, or in connection with the software or the use or other dealings in the software.
 * 
 */

package com.improvess.shared.examples;

import com.improvess.shared.connector.Connector;

import java.nio.ByteBuffer;

public class CheckConnectorRead {

    static {
        System.loadLibrary("java_shared_memory_lib");
    }

    public static void main(String[] args) {

        System.out.println("Checking Connector read:");

        Connector connector = new Connector();

        int shm_id = connector.initialize_shared_buffer(0xd7a6, 100);

        ByteBuffer buffer = connector.get_shared_buffer(shm_id);

        if (buffer != null) {
            
            for (int i = 0; i < 8; ++i) {
                System.out.println((int)buffer.get(i));
            }
            
        } else {
            System.err.println("Buffer is null");
        }

        if (!connector.release_shared_buffer(shm_id)) {
            System.err.println("Failed to release shared buffer");
        }

        System.out.println("End");

    }
    
}
