package BattleShip;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;
//Class used for handling socket connection and socket data
public abstract class NetworkConnection {

    public ConnectionThread connThread = new ConnectionThread();
    private Consumer<Serializable> onReceiveCallBack;

    public NetworkConnection(Consumer<Serializable> onReceiveCallBack){
        this.onReceiveCallBack = onReceiveCallBack;
        connThread.setDaemon(true);
    }

    void startConnection() throws Exception{
        connThread.start();
    }

    void send(Serializable data) throws Exception{
        connThread.out.writeObject(data);
    }

    protected abstract boolean isServer();
    protected abstract String getIP();
    protected abstract int getPort();

    public class ConnectionThread extends Thread {
        public Socket socket;
        public ObjectOutputStream out;
        public ObjectInputStream in;

        @Override
        public void run(){
            try (ServerSocket server = isServer() ? new ServerSocket(getPort()) : null;
                 Socket socket = isServer() ? server.accept(): new Socket(getIP(), getPort());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                this.socket = socket;
                this.out = out;
                this.in = in;
                socket.setTcpNoDelay(true);

                while (true){
                    Serializable data = (Serializable) in.readObject();
                    onReceiveCallBack.accept(data);
                }
            } catch (Exception e) {
                onReceiveCallBack.accept("Connection Closed");
            }
        }
    }
}
