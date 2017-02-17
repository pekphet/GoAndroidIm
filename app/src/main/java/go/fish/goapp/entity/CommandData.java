package go.fish.goapp.entity;

import static go.fish.goapp.Constants.P_SP;

/**
 * Created by fish on 17-2-14.
 */

public class CommandData {
    private String act;
    private String arg;

    public CommandData(String rcv) {
        this.act = rcv.split(P_SP)[0];
        this.arg = rcv.split(P_SP)[1];
    }

    public CommandData(String act, String arg) {
        this.act = act;
        this.arg = arg;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }
}
