package com.company;
import java.io.*;
import java.net.*;
import java.time.Duration;
import java.time.Instant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.System.exit;


class Client {
    private static long turnTime;
    private static String message;
    private String Message;

    static class CreateThread extends Thread {
        private final int port;
        private final String address;
        private final String line;
        private final String threadName;




        public CreateThread(String port, String address, String line, String threadName) {
            this.line = line;
            this.threadName = threadName;
            this.address = address;
            this.port = Integer.parseInt(port);
        }


        @Override
        public void run() {
        try(Socket socket = new Socket(address, port)) {
                Instant start = Instant.now();
                Client.RequestToServer(line, socket);
                String response = Client.Response(socket);
                Instant finish = Instant.now();
        long time = Duration.between(start, finish).toMillis();
                System.out.print(threadName + " output:\n" + "\n" + threadName + " finished in " + time + " ms\n\n");
              Client.TAT(time);
      } catch(IOException e) {
                System.out.println("I/O Exception. Exiting...");
                    e.printStackTrace();
                    exit(-1);
            }}}



public static void populateThreads(String address,String port){
    Scanner input;
    for( ;; ) {
        input = new Scanner(System.in);
        int clinet_Requests;
        System.out.println("hello welcome to the unf server:" + address);
        System.out.println("please enter one of the fallowing commands ");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("1-date,2-up,3-memory,4-netstat,5-list,6-running,7-to quit or ctrl+c");
        System.out.println("------------------------------------------------------------------------------");
        String line = input.next();
        if(line.contains("7")){
            System.out.println("user choice ....7-(quitting.....)");
            exit(0); }



        while (Integer.parseInt(line) < 1 || Integer.parseInt(line) > 6) {
            System.out.print("Invalid option. Please try again: ");
            line = input.next(); }
        System.out.println(" Option " + line + " selected!");
        System.out.print("Number of client requests (1, 5, 10, 15, 20, 25, 100): ");
        clinet_Requests = input.nextInt();



        while (!Arrays.asList(1, 5, 10, 15, 20, 25, 100).contains(clinet_Requests)) {
            System.out.print("Invalid request number. Please try again: ");
            System.out.println(clinet_Requests);
            clinet_Requests = input.nextInt(); }
        ArrayList<Thread> ProcesstThread = new ArrayList<>();


        for (int i = 1; i <= clinet_Requests; i++) {
            Thread reqThread = new CreateThread(port, address, line, "Client #" + i);
            reqThread.start();
            ProcesstThread.add(reqThread); }


        for (Thread thread : ProcesstThread) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace(); } }
        // Prints turn-around total and avg after final thread completes
        System.out.printf("\nTotal Turn-around Time: %d ms" + "\nAverage Turn-around Time: %d ms\n",
                turnTime, turnTime / clinet_Requests); }


}







    public static void main(String[] args) {
                Scanner input = new Scanner(System.in);
                System.out.println("hello please enter a hostname: CNT4505D.ccec.unf.edu");
                String address;
        int port;
        if(args.length >= 2) {
                address = args[0];
                port = Integer.parseInt(args[1]);


        } else
            if(args.length == 1) {
                address = args[0];
                System.out.print("Enter port (1025 - 4998): ");
                port = input.nextInt();


        } else {
                System.out.print("Enter address: ");
                address = input.nextLine();
                System.out.print("Enter port (1025 - 4998): ");
                port = input.nextInt();

            }


        while(port < 1 || port > 4998) {
                System.out.print("Enter a valid port number: ");
                port = input.nextInt(); }

        populateThreads(address, String.valueOf(port));

    }


    public static void TAT(long time) { turnTime += time; }



    public static void RequestToServer(String message, Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true);
        writer.println(message);
        if(message.equals("1")){
            System.out.println("user choice ...."+message+"-(Date)");
        } else
            if (message.equals("2")){
                System.out.println("user choice ...."+message+"-(uptime)");
            }else
                if(message.equals("3")){

                    System.out.println("user choice ...."+message+"-(memory)");
                }else
                    if(message.equals("5")){

                        System.out.println("user choice ...."+message+"-(-netstat)");
                    }else
                        if(message.equals("6")){

                            System.out.println("user choice ...."+message+"-(List)");
                        }



    }




    public static String Response(Socket socket) throws IOException {
        // read from socket
        BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while ((message = rd.readLine()) != null) {
            System.out.println(message);
        }
        return "";
    }}



