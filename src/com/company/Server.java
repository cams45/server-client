
package com.company;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Server {
    private static int requestCount;
    private static int clientCount;
    private static String threadName;
    private static Socket socket;
    private int port;


    public static void SendToClient(String message, Socket socket) throws IOException {
        OutputStream out = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true);
        writer.println(message);
        writer.close();
    }


    public static String ClientChoice(Socket socket) throws IOException {
        InputStream inpS= socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inpS));
        return br.readLine();
    }


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int port;
        if(args.length >= 1) {
            port = Integer.parseInt(args[0]);}
            else {
                System.out.print("Enter port (1025 - 4998): ");
                    port = input.nextInt();


            while(port < 1025 || port > 4998) {
                System.out.print("Enter a valid port number: ");
                port = input.nextInt(); } }

        try(ServerSocket svSocket = new ServerSocket(port, 25)){
            System.out.println("Listening for clients at port " + port + "...");


            do {
                Socket socket = svSocket.accept();

                // Makes a new thread to handle requests and runs it
                Thread handler = new HandlerThread("processing thread number #" + ++clientCount, socket);
                handler.start();
            } while (true);
        } catch(IOException e) {
            System.out.println("I/O Exception. Exiting...");
            e.printStackTrace();
            System.exit(-1);
        }

        input.close();
        input.close();

    }







    static class HandlerThread extends Thread {
        public String threadName;
        public Socket socket;



        public HandlerThread(String threadName, Socket socket) {
            this.threadName = threadName;
            this.socket = socket;
        }

        @Override
        public void run() {
            int requestNum = requestCount + 1;
            try {
                System.out.println("Handling request #" + ++requestCount + " with " + threadName + "...");
                // Retrieves request value from client
                String line = ClientChoice(socket);
                // Retrieves the command response
                String response = CaseTable(line);
                SendToClient(response,socket);
                System.out.println("-----------------------REQUEST # "+requestNum+"--------------------------------------------------\n");
                System.out.println("\nRequest #" + requestNum + " completed!");
                System.out.println("task complete on server side\n");
                System.out.println("-----------------------REQUEST # "+requestNum+"COMPLETE------------------------------------------\n");
                System.out.println("-----------------------MOVING TO NEXT THREAD-----------------------------------------------------\n");

            }   catch(IOException e) {
                System.out.print("I/O Exception: ");
                e.printStackTrace();
                System.exit(-1); } }



    }


    public static String CaseTable(String line) throws IOException {
        Process processCommand;
        String message = null;

        switch (line) {
            case "1":
                processCommand = Runtime.getRuntime().exec(" date ");
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(processCommand.getInputStream()));
                while ((message = stdInput.readLine()) != null) {
                            System.out.println("command output: "+message);
                            System.out.println("--------------------------------------------------------------------------------------");
                }
                                message = CmdRequest("date").toString();
                break;
            case "2":

                processCommand = Runtime.getRuntime().exec(" uptime -p ");
                    stdInput = new BufferedReader(new InputStreamReader(processCommand.getInputStream()));
                while ((message = stdInput.readLine()) != null) {
                            System.out.println("command output: "+message);
                            System.out.println("-------------------------------------------------------------------------------------");}
                                message = CmdRequest(" uptime -p ").toString();
                break;
            case "3":
                processCommand = Runtime.getRuntime().exec(" free -t -m");
                    stdInput = new BufferedReader(new InputStreamReader(processCommand.getInputStream()));
                while ((message = stdInput.readLine()) != null) {
                            System.out.println("command output: "+message);
                            System.out.println("------------------------------------------------------------------------------------");}
                                message = CmdRequest("free -t -m").toString();
                break;
            case "4":
                processCommand = Runtime.getRuntime().exec(" netstat -at ");
                    stdInput = new BufferedReader(new InputStreamReader(processCommand.getInputStream()));
                while ((message = stdInput.readLine()) != null) {
                            System.out.println("command output: "+message);
                            System.out.println("-----------------------------------------------------------------------------------");}
                                message = CmdRequest(" netstat -at ").toString();
                break;
            case "5":
                processCommand = Runtime.getRuntime().exec(" who ");
                    stdInput = new BufferedReader(new InputStreamReader(processCommand.getInputStream()));
                while ((message = stdInput.readLine()) != null) {
                            System.out.println("command output: "+message);
                            System.out.println("-----------------------------------------------------------------------------------");}
                                message = CmdRequest(" who ").toString();
                break;
            case "6":
                processCommand = Runtime.getRuntime().exec(" ps ");
                    stdInput = new BufferedReader(new InputStreamReader(processCommand.getInputStream()));
                while ((message = stdInput.readLine()) != null) {
                            System.out.println("command output: "+message);
                            System.out.println("---------------------------------------------------------------------------------");}
                                message = CmdRequest("ps").toString();
                break;
            default:
        }
        return message;
    }
    public static StringBuilder CmdRequest(String command) throws IOException {
        BufferedReader cmdResponse;
        Process RunCommand;
        RunCommand = Runtime.getRuntime().exec(command);
        cmdResponse = new BufferedReader(new InputStreamReader(RunCommand.getInputStream()));
        StringBuilder BuildMessage = new StringBuilder();
        for(String CMDmessage = ""; CMDmessage != null; CMDmessage = cmdResponse.readLine())
            BuildMessage.append(CMDmessage).append("\n");
        return BuildMessage;
    }

}//end of server
