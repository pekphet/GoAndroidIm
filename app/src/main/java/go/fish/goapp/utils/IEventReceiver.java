package go.fish.goapp.utils;

/**
 * Created by fish on 16-6-24.
 */
public interface IEventReceiver<T> {
    void onReceived(T t);
    void onReceivedError(String msg);
}
