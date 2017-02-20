package go.fish.goapp.utils;

import go.fish.goapp.entity.CommandData;
import go.fish.goapp.interfaces.TCPCallback;

/**
 * Created by fish on 17-2-17.
 */

public class EventHelper {
    public static IEventReceiver<CommandData> createEventReceiver(TCPCallback callback) {
        IEventReceiver<CommandData> eRcv = new IEventReceiver<CommandData>() {
            @Override
            public void onReceived(CommandData cmdData) {
                callback.doCmd(cmdData);
            }
            @Override
            public void onReceivedError(String msg) {
                callback.doErr(msg.split(":")[1]);
            }
        };
        return eRcv;
    }
}
