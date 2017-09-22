import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created  on 17.08.17.
 */
public class JGroupsTest extends ReceiverAdapter {

    private JChannel channel;
    private String user_name = System.getProperty("user.name", "n/a");

    private void start() throws Exception {
        channel = new JChannel("custom_tcp.xml");
        channel.setReceiver(this);
        channel.connect("jasper-sync");
        eventLoop();
        channel.close();
    }

    private void eventLoop() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.print("> ");
                System.out.flush();
                String line = in.readLine().toLowerCase();
                if (line.startsWith("quit") || line.startsWith("exit")) {
                    break;
                }
                line = "[ " + user_name + " ] " + line;
                Message msg = new Message(null, null, line);
                channel.send(msg);
            } catch (Exception ignored) {
            }
        }
    }

    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    public void receive(Message msg) {
        System.out.println(msg.getSrc() + ": " + msg.getObject());
    }

    public static void main(String[] args) throws Exception {
        new JGroupsTest().start();
    }

}
