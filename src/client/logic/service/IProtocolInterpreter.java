package client.logic.service;

public interface IProtocolInterpreter {
    void setUsername(String username);
    String input(String data);
    String output(String data);
    String dataIn(String data);
    String dataOut(String data);
    String listClients(String data);
    String joinConfirm();
    String joinError();
    String gameChallengeIn(String data);
    String gameChallengeOut(String data);
    String gameAcceptIn(String data);
    String gameAcceptOut(String data);
    String gameDeclineIn(String data);
    String gameDeclineOut(String data);
    String quit();

}
