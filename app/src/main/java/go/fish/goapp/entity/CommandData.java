package go.fish.goapp.entity;

/**
 * Created by fish on 17-2-14.
 */

public class CommandData {
    private CommandAction act;
    private String arg;

    public CommandData() {
    }

    public CommandData(String cmd) {
        this.arg = cmd.split("<-")[1];
        switch (cmd.split("<-")[0]) {
            case "user_list" :
                this.act = CommandAction.Users;
                return;
        }
    }

    public CommandData(CommandAction act, String arg) {
        this.act = act;
        this.arg = arg;
    }

    public CommandAction getAct() {
        return act;
    }

    public void setAct(CommandAction act) {
        this.act = act;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }
}
