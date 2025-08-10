package com.improvess.shared.examples;

import com.improvess.shared.connector.Connector;

public class CheckConnector {

    static {
        System.loadLibrary("java_shared_memory_lib");
    }

    public static void main(String[] args) {
        System.out.println("Checking Connector:");
        Connector connector = new Connector();
        connector.say_hello();
        int times = 0;
        if (args.length > 0) {
            times = Integer.parseInt(args[0]);
        }
        connector.say_hello_again(times);

        System.out.println("Count: " + connector.get_count());

        System.out.println("End");

    }
    
}
