package si.f5.hatosaba.uhcffa.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ArenaSocket {

    public static void sendMessage(String message) {
        if (message == null)
            return;
        if (message.isEmpty())
            return;

        try {
            Socket socket = new Socket(InetAddress.getByName("uhcmeetup").getHostAddress(), 22222);
            RemoteLobby rl = new RemoteLobby(socket);
            if (rl.out != null) {
                rl.sendMessage(message);
            }
        } catch (IOException iOException) {
           System.out.println("えらー");
        }

    }

    private static class RemoteLobby {
        private Socket socket;

        private PrintWriter out;

        private Scanner in;

        private boolean compute = true;

        private RemoteLobby(Socket socket) {
            this.socket = socket;
            try {
                this.out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException ignored) {
                this.out = null;
                return;
            }
            try {
                this.in = new Scanner(socket.getInputStream());
            } catch (IOException ignored) {
                return;
            }
        }

        private boolean sendMessage(String message) {
            if (this.socket == null) {
                disable();
                return false;
            }
            if (!this.socket.isConnected()) {
                disable();
                return false;
            }
            if (this.out == null) {
                disable();
                return false;
            }
            if (this.in == null) {
                disable();
                return false;
            };
            if (this.out.checkError()) {
                disable();
                return false;
            }
            this.out.println(message);
            return true;
        }

        private void disable() {
            this.compute = false;

            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.in = null;
            this.out = null;
        }
    }

}
