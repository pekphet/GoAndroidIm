package go.fish.goapp.command;

import client.Client;

import static go.fish.goapp.Constants.P_CALL_USER_LIST;

/**
 * Created by fish on 17-2-20.
 */

public class ChatCommands {
    public static void listUsers() {
        Client.call(P_CALL_USER_LIST, "");
    }

    public static void sendMsg(String msg) {
        Client.sendMsg(msg);
    }

    public static void sendMsgTo(int uid, String msg) {
        Client.sendMsgTo(uid, msg);
    }

}
