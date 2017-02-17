package go.fish.goapp.interfaces;

import go.fish.goapp.entity.CommandData;

/**
 * Created by fish on 17-2-17.
 */

public interface TCPCallback {
    void doCmd(CommandData cmdData);
}
