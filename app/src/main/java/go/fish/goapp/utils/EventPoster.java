package go.fish.goapp.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by fish on 16-6-24.
 */
public class EventPoster<T> {
    private Set<IEventReceiver<T>> mRcvs = new HashSet<>();

    public void registered(IEventReceiver rcv) {
        mRcvs.add(rcv);
    }

    public void unregistered(IEventReceiver rcv) {
        mRcvs.remove(rcv);
    }

    public void clean() {
        mRcvs.clear();
    }

    public void post(final T t) {
        for (IEventReceiver<T> r : mRcvs) {
            if (r != null) {
                r.onReceived(t);
            }
        }
    }

    public void syncPost(final T t) {
        for (IEventReceiver<T> r : mRcvs) {
            ThreadPoolManager.getInstance().addTask(() -> {
                if (r != null) {
                    r.onReceived(t);
                }
            });
        }
    }

    public void postError(final String errMsg) {
        for (IEventReceiver<T> r : mRcvs) {
            if (r != null) {
                r.onReceivedError(errMsg);
            }
        }
    }

    public void syncPostError(final String errMsg) {
        for (IEventReceiver<T> r : mRcvs) {
            ThreadPoolManager.getInstance().addTask(() -> {
                if (r != null) {
                    r.onReceivedError(errMsg);
                }
            });
        }
    }

}
