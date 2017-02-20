package go.fish.goapp.command;

import client.Client;

import static go.fish.goapp.Constants.P_CALL_LOGIN;
import static go.fish.goapp.Constants.P_CALL_REG;

/**
 * Created by fish on 17-2-20.
 */

public class LoginCommands {
    public static void register(String acc, String pwd, String nick) {
        Client.call(P_CALL_REG, String.format("%s,%s,%s", nick, acc, pwd));
    }

    public static void login(String acc, String pwd) {
        Client.call(P_CALL_LOGIN, String.format("%s,%s", acc, pwd));
    }
}
