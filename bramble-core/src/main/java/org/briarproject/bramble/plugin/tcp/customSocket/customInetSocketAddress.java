package org.briarproject.bramble.plugin.tcp.customSocket;

import java.net.InetSocketAddress;

/**
 * Overwrite the current implementation of the InetSocketAddress
 * to add our own ClientID, a unique String of the current client
 */

public class customInetSocketAddress extends InetSocketAddress {

    private String ClientID;
    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }
    public customInetSocketAddress(int i) {
        super(i);
    }


}
