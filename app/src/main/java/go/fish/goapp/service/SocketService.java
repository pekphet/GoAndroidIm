package go.fish.goapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import client.Client;
import go.fish.goapp.Constants;
import go.fish.goapp.entity.CommandData;
import go.fish.goapp.utils.EventPoster;

/**
 * Created by fish on 17-2-14.
 */

public class SocketService extends Service implements Constants {
    public static EventPoster<CommandData> gPoster = new EventPoster<>();
    public static Handler gHandler = new Handler(Looper.getMainLooper());
    public static boolean isConnecting = false;

    private String innerMsg;
    private static Context mContext;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        startSocketThread();
    }

    private void startSocketThread() {

        Thread msgThread = new Thread(() -> {
            HToast("连接服务器中。。。");
            if (Client.conn()) {
                HToast("连接服务器失败");
                return;
            }
            isConnecting = true;
            HToast("连接服务器成功");
            while (isConnecting) {
                innerMsg = Client.receive();
                if (innerMsg.contains(P_SP)) {
                    gHandler.post(() -> gPoster.post(new CommandData(innerMsg)));
                } else if (innerMsg.contains(P_RS_ERR)) {
                    gHandler.post(() -> gPoster.postError(innerMsg));
                }
            }
        });
        msgThread.start();
    }

    private void doTCPClose() {
        //TODO  close dev
        isConnecting = false;
    }

    public static void HToast(String msg) {
        gHandler.post(() -> Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show());
    }
}
