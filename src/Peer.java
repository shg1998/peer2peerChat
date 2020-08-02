import javax.json.Json;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;

public class Peer {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter UserName And Port for this peer:\n");
        String[] setupValues = br.readLine().split(" ");
        ServerThreads serverThreads = new ServerThreads(setupValues[1]);
        serverThreads.start();
        new Peer().UpdateListenToPeers(br, setupValues[0], serverThreads);
    }

    public void UpdateListenToPeers(BufferedReader bufferedReader, String username, ServerThreads serverThreads) throws Exception {
        System.out.println("enter hostName:Port#");
        System.out.println("Peers to recive Message From(s to skip)");
        String input = bufferedReader.readLine();
        String[] inputValue = input.split(" ");
        if (!input.equals("s")) for (int i = 0; i < inputValue.length; i++) {
            String[] Address = inputValue[i].split(":");
            Socket socket = null;
            try {
                socket = new Socket(Address[0], Integer.valueOf(Address[1]));
                new PeerThreads(socket).start();
            } catch (Exception e) {
                if (socket != null) {
                    socket.close();
                } else
                    System.out.println("Invalid Input.Skip for the next round!");
            }

        }
        Communicate(bufferedReader, username, serverThreads);
    }

    public void Communicate(BufferedReader bufferedReader, String userName, ServerThreads serverThreads) {
        try {
            System.out.println("You can now communicate(e for exit- c for change)");
            boolean flag = true;
            while (flag) {
                String message = bufferedReader.readLine();
                if (message.equals("e")) {
                    flag = false;
                    break;
                } else if (message.equals("c")) {
                    UpdateListenToPeers(bufferedReader, userName, serverThreads);
                } else {
                    StringWriter stringWriter = new StringWriter();
                    Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder()
                            .add("username", userName)
                            .add("message",message)
                            .build()
                    );
                    serverThreads.sendMessage(stringWriter.toString());
                }
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
