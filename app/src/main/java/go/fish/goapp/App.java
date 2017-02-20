package go.fish.goapp;

import android.app.Application;
import android.content.Intent;

import go.fish.goapp.service.SocketService;

/**
 * Created by fish on 17-2-20.
 */

public class App extends Application {
    public static String sName;


    @Override
    public void onCreate() {
        super.onCreate();
        initSocket();
    }

    private void initSocket() {
        Intent intent = new Intent(this, SocketService.class);
        startService(intent);
    }

}
