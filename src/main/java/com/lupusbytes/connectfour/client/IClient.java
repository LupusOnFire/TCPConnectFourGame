package com.lupusbytes.connectfour.client;

public interface IClient {
    void setRemoteServer();
    void setUsername(String username);
    boolean joinServer();
    void clientInput();
    void displayData(String data);
}
