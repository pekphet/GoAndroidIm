package go.fish.goapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import go.fish.goapp.App;
import go.fish.goapp.R;
import go.fish.goapp.command.LoginCommands;
import go.fish.goapp.entity.CommandData;
import go.fish.goapp.interfaces.TCPCallback;
import go.fish.goapp.service.SocketService;
import go.fish.goapp.utils.EventHelper;
import go.fish.goapp.utils.IEventReceiver;

import static go.fish.goapp.Constants.E_CODE_EXISTS;
import static go.fish.goapp.Constants.E_CODE_PWD;
import static go.fish.goapp.Constants.P_RS_LOGIN;
import static go.fish.goapp.Constants.P_RS_REG;

/**
 * Created by fish on 17-2-17.
 */

public class LoginActivity extends AppCompatActivity implements TCPCallback {

    private IEventReceiver<CommandData> mReceiver;
    private boolean mFlagReg = false;


    private EditText mEtAcc;
    private EditText mEtPwd;
    private EditText mEtNick;
    private Button mBtnCmt;
    private Button mBtnChg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_login);
        initEventReceiver();
        initView();
    }

    private void initView() {
        mEtAcc = (EditText) findViewById(R.id.et_acc);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mEtNick = (EditText) findViewById(R.id.et_nick);
        mBtnCmt = (Button) findViewById(R.id.btn_cmt);
        mBtnChg = (Button) findViewById(R.id.btn_chg_reg);
        mBtnChg.setOnClickListener(v -> doChgReg());
        mBtnCmt.setOnClickListener(v -> doCommit());
    }

    private void initEventReceiver() {
        mReceiver = EventHelper.createEventReceiver(this);
        SocketService.gPoster.clean();
        SocketService.gPoster.registered(mReceiver);
    }


    private void doChgReg() {
        mFlagReg = !mFlagReg;
        mEtNick.setText("");
        mEtNick.setVisibility(mFlagReg ? View.VISIBLE : View.GONE);
        mBtnChg.setText(mFlagReg ? "To Login" : "To Register");
        mBtnCmt.setText(mFlagReg ? "Register" : "Login");
    }

    private void doCommit() {
        if (!checkCommit()) {
            Toast.makeText(this, "account,pwd and nickname mustn't have ',' ':' '<' '>' or empty!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mFlagReg) {
            doReg();
        }else {
            doLogin();
        }
    }

    private void doReg() {
        LoginCommands.register(
                mEtAcc.getText().toString(),
                mEtPwd.getText().toString(),
                mEtNick.getText().toString());
    }

    private void doLogin() {
        LoginCommands.login(
                mEtAcc.getText().toString(),
                mEtPwd.getText().toString());
    }

    @Override
    public void doCmd(CommandData cmdData) {
        switch (cmdData.getAct()) {
            case P_RS_REG:
                SocketService.HToast("Register Succeed, Logging in...");
                doLogin();
                break;
            case P_RS_LOGIN:
                SocketService.HToast("Welcome~");
                App.sName = cmdData.getArg();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void doErr(String errCode) {
        switch(errCode) {
            case E_CODE_EXISTS:
                SocketService.HToast("Ur Account is Exists!");
                break;
            case E_CODE_PWD:
                SocketService.HToast("Ur PWD is incorrect!");
                break;
        }
    }


    private boolean checkCommit() {
        if (mFlagReg) {
            if (checkStr(mEtNick.getText().toString())){
                return false;
            }
        }
        return !(checkStr(mEtAcc.getText().toString()) || checkStr(mEtPwd.getText().toString()));
    }

    private boolean checkStr(String str) {
        return str.equals("") || str.contains(",") ||
                str.contains(":") || str.contains("<") ||
                str.contains(">");
    }

    private void clearEdits() {
        mEtAcc.setText("");
        mEtPwd.setText("");
        mEtNick.setText("");
    }
}
