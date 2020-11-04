package BattleShip;

import java.io.Serializable;
import java.util.function.Consumer;
//Server class that inherits from NetworkConnection class
public class Server extends NetworkConnection {

    private int port;

    Server(int port, Consumer<Serializable> onReceiveCallBack){
        super(onReceiveCallBack);
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return true;
    }

    @Override
    protected String getIP() {
        return null;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
