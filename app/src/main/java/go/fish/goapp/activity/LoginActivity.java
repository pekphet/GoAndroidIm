package go.fish.goapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import go.fish.goapp.entity.CommandData;
import go.fish.goapp.interfaces.TCPCallback;

/**
 * Created by fish on 17-2-17.
 */

public class LoginActivity extends AppCompatActivity implements TCPCallback{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void doCmd(CommandData cmdData) {
        switch (cmdData.getAct()) {

        }
    }
}
