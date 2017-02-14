package go.fish.goapp.utils;

import go.fish.goapp.service.SocketService;
import goandroid.Goandroid;

/**
 * Created by fish on 17-2-14.
 */

public class Chatter {
    public static void send(String data) {
        if (SocketService.sIsLinked) {
            Goandroid.send(data);
        } else {
            SocketService.HToast("未连接到服务器");
        }
    }

    public static void freshUserList() {
        if (SocketService.sIsLinked) {
            Goandroid.cmdUserList();
        } else {
            SocketService.HToast("未连接到服务器");
        }
    }

    public static void Register(String lg, String pwd, String name) {
        if (SocketService.sIsLinked) {
        } else {
            SocketService.HToast("未连接到服务器");
        }
    }

    public static void Login(String lg, String pwd) {
        if (SocketService.sIsLinked) {
        } else {
            SocketService.HToast("未连接到服务器");
        }
    }

    public static void ChangeUser(String name) {
        if (SocketService.sIsLinked) {
            Goandroid.changeSendUser(name);
        } else {
            SocketService.HToast("未连接到服务器");
        }
    }

    public static void ClearChatUser() {
        if (SocketService.sIsLinked) {
            Goandroid.clearSendUser();
        } else {
            SocketService.HToast("未连接到服务器");
        }
    }

    public static void ChangeName(String name) {
        if (SocketService.sIsLinked) {
            Goandroid.changeName(name);
        } else {
            SocketService.HToast("未连接到服务器");
        }
    }

}
