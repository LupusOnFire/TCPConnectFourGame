package server.logic.repository;

import server.logic.GameInstance;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameInstanceRepository {
    List<GameInstance> instanceList;

    public GameInstanceRepository() {
        this.instanceList =  new ArrayList<>();
    }

    public GameInstance getGameInstanceBySocket(Socket socket){
        for (GameInstance instance : instanceList) {
            if (instance.getPlayer1().getSocket() == socket) {
                return instance;
            } else if (instance.getPlayer2().getSocket() == socket) {
                return instance;
            }
        }
        return null;
    }
}
