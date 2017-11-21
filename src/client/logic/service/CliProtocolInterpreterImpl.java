package client.logic.service;


import static server.logic.Constants.*;

public class CliProtocolInterpreterImpl implements IProtocolInterpreter {
    String username;
    public CliProtocolInterpreterImpl() {
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String input(String data) {
        if (data.length() >= 4) {
            String header = data.substring(0, 4);
            switch (header) {
                case J_OK: {
                    return joinConfirm();
                }
                case JERR: {
                    return joinError();

                }
                case LIST: {
                    return listClients(data);
                }
                case DATA: {
                    return dataIn(data);
                }
            }
        }
        return "";
    }

    @Override
    public String output(String data) {
        if (data.charAt(0) == '!') {
            if (data.startsWith("!play"))
                return gameChallengeOut(data);
            else if (data.startsWith("!accept"))
                return gameChallengeOut(data);
            else if (data.startsWith("!decline"))
                return gameDeclineOut(data);
            else if (data.startsWith("!quit"))
                return quit();
        }
        return dataOut(data);
    }

    @Override
    public String dataIn(String data) {
        return data.substring(5, data.length());
    }

    @Override
    public String dataOut(String data) {
        return "DATA " + username + ": " + data;
    }

    @Override
    public String listClients(String data) {
        String out = "Clients in lobby: ";
        String[] clients = data.split(" ");
        for (int i = 1; i < clients.length; i++) {
            out += (clients[i] + " ");
        }
        return out;
    }

    @Override
    public String joinConfirm() {
        return "Successfully connected";
    }

    @Override
    public String joinError() {
        System.out.println("Username not available, aborting");
        System.out.println("Error, closing connection");
        System.exit(0);
        return "";
    }

    @Override
    public String gameChallengeIn(String data) {
        String challenger = data.split(" ")[1];
        return challenger + " has challenged you!\nType \"!accept " + challenger + "\" to play";
    }

    @Override
    public String gameChallengeOut(String data) {
        if (data.split(" ").length>1)
            return GCHL + " " + username + " " + data.split(" ")[1];
        return "Something is not quite right";
    }

    @Override
    public String gameAcceptIn(String data) {
            return data.split(" ")[2] + " has accepted your challenge";
    }

    @Override
    public String gameAcceptOut(String data) {
        if (data.split(" ").length>1)
            return GACK + " " + data.split(" ")[1] + " " + username;
        return "Something is not quite right";
    }

    @Override
    public String gameDeclineIn(String data) {
        return data.split(" ")[2] + " has declined your challenge :(";
    }

    @Override
    public String gameDeclineOut(String data) {
        if (data.split(" ").length>1)
            return GNAK + " " + data.split(" ")[1] + " " + username;
        return "Something is not quite right";
    }


    @Override
    public String quit() {
        return (QUIT + " " + username);
    }
}
