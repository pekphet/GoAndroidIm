package go.fish.goapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import go.fish.goapp.Constants;
import go.fish.goapp.entity.CommandAction;
import go.fish.goapp.entity.CommandData;
import go.fish.goapp.utils.EventPoster;
import goandroid.Goandroid;

/**
 * Created by fish on 17-2-14.
 */

public class SocketService extends Service implements Constants {
    public static EventPoster<CommandData> gPoster = new EventPoster<>();
    public static Handler   gHandler      = new Handler(Looper.getMainLooper());
    public static boolean   sIsLinked     = false;

    private String          innerMsg;
    private static Context  mContext;

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
            if (Goandroid.conn()) {
                HToast("连接服务器失败");
                return;
            }
            sIsLinked = true;
            HToast("连接服务器成功");
            while (true) {
                innerMsg = Goandroid.receiver();
                if (SIGN_CMD_CLOSE.equals(innerMsg)){
                    gHandler.post(()->doTCPClose());
                    break;
                }
                gHandler.post(() -> {
                    if (innerMsg.startsWith(SIGN_CMD_RESULT)) {
                        gPoster.post(new CommandData(innerMsg.split(SIGN_CMD_SPLIT_M)[1]));
                    }else {
                        gPoster.post(new CommandData(CommandAction.Chat, innerMsg));
                    }
                });
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (msg.startsWith("Command Result:")) {
//                                doCmd(msg.split(":")[1]);
//                            } else {
//                                addTextView(msg);
//                            }
//                        }
//                    });
            }
        sIsLinked = false;
        });
        msgThread.start();
    }

    private void doTCPClose() {
        //TODO  close dev
    }

    public static void HToast(String msg) {
        gHandler.post(() -> Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show());
    }
}
