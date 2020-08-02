import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

public class ServerThreadThreads extends Thread {
    private ServerThreads serverThreads;
    private Socket socket;
    private PrintWriter printWriter;

    public ServerThreadThreads(Socket socket, ServerThreads serverThreads) {
        this.serverThreads = serverThreads;
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
            while (true) serverThreads.sendMessage(bufferedReader.readLine());
        } catch (Exception e) {
            serverThreads.getServerThreadThreads().remove(this);
        }
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }
}
