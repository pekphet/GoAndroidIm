package go.fish.goapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import go.fish.goapp.App;
import go.fish.goapp.R;
import go.fish.goapp.command.ChatCommands;
import go.fish.goapp.entity.CommandData;
import go.fish.goapp.interfaces.TCPCallback;
import go.fish.goapp.service.SocketService;
import go.fish.goapp.utils.EventHelper;
import go.fish.goapp.utils.IEventReceiver;

import static go.fish.goapp.Constants.MSG_BROADCAST_FLAG;
import static go.fish.goapp.Constants.MSG_NORMAL_FLAG;
import static go.fish.goapp.Constants.MSG_PRIV_FLAG;
import static go.fish.goapp.Constants.P_RS_USER_LIST;
import static go.fish.goapp.Constants.P_SEND_MSG;
import static go.fish.goapp.Constants.P_SP_ARG;
import static go.fish.goapp.Constants.P_SP_SEND;

public class MainActivity extends AppCompatActivity implements TCPCallback {


    private ArrayAdapter<String> mSpiAdapter;
    private LinearLayout mLlContent;
    private EditText mEtSend;
    private Button mBtnSend;
    private ScrollView mScroll;
    private Button mBtnRefresh;
    private Spinner mSpiUsers;

    private Handler mHandler = new Handler(Looper.myLooper());
    private IEventReceiver<CommandData> mReceiver;

    private boolean isAdapterFirst = true;
    private boolean isPriv = false;
    private int targetPriv = -1;
    private String[] r_users;
    private LinearLayout.LayoutParams mTvLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startSocketServer();
        initView();
        initEventReceiver();
        initAdapter();
    }

    private void startSocketServer() {
        Intent intent = new Intent(this, SocketService.class);
        startService(intent);
    }

    private void initView() {
        mScroll = (ScrollView) findViewById(R.id.scroll);
        mLlContent = (LinearLayout) findViewById(R.id.ll_content);
        mEtSend = (EditText) findViewById(R.id.et_send);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnRefresh = (Button) findViewById(R.id.btn_fresh_spi);
        mSpiUsers = (Spinner) findViewById(R.id.spi_users);

        mBtnSend.setOnClickListener(v -> {
            if (isPriv) {
                ChatCommands.sendMsgTo(targetPriv, (mEtSend.getText().toString()));
            } else {
                ChatCommands.sendMsg(mEtSend.getText().toString());
            }
            addTextViewR(mEtSend.getText().toString());
            mEtSend.setText("");
        });

        mBtnRefresh.setOnClickListener(v -> ChatCommands.listUsers());
    }

    private void initEventReceiver() {
        mReceiver = EventHelper.createEventReceiver(this);
        SocketService.gPoster.clean();
        SocketService.gPoster.registered(mReceiver);
    }

    private void initAdapter() {
        mSpiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mSpiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpiUsers.setAdapter(mSpiAdapter);
        mSpiUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isAdapterFirst) {
                    isAdapterFirst = false;
                    return;
                }
                if (r_users == null || r_users.length < 1) {
                    return;
                }
                if(r_users[position].split(":")[1].equals("-1")) {
                    isPriv = false;
                    targetPriv = -1;
                } else {
                    isPriv = true;
                    targetPriv = Integer.parseInt(r_users[position].split(":")[1]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void addTextView(String msg, int flag) {
        if (msg.split(":")[0].equals(App.sName)) {
            return;
        }
        TextView tv = new TextView(this);
        tv.setLayoutParams(mTvLayoutParams);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        tv.setPadding(0, 5, 0, 5);
        tv.setText(msg);
        if (flag == MSG_PRIV_FLAG) {
            tv.setTextColor(0xFF77bbff);
        } else if (flag == MSG_BROADCAST_FLAG) {
            tv.setTextColor(0xffee22ee);
        }
        mLlContent.addView(tv);
        mScroll.fullScroll(ScrollView.FOCUS_DOWN);
    }

    private void addTextViewR(String msg) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(mTvLayoutParams);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        tv.setPadding(0, 5, 0, 5);
        tv.setText(App.sName + ":" + msg);
        mLlContent.addView(tv);
        DoDelayed(()->mScroll.fullScroll(ScrollView.FOCUS_DOWN));
    }

    private void flushSpinner(String[] strs) {
        r_users = strs;
        List<String> tmp = new ArrayList<>();
        for(String s : strs) {
            tmp.add(s.split(":")[0]);
        }
        mSpiAdapter.clear();
        mSpiAdapter.addAll(tmp);
    }


    private void DoDelayed(Runnable r) {
        mHandler.postDelayed(r, 500);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SocketService.gPoster.registered(mReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void doCmd(CommandData cmdData) {
        switch (cmdData.getAct()) {
            case P_SEND_MSG :
                addTextView(cmdData.getArg(), MSG_NORMAL_FLAG);
                break;
            case P_SEND_MSG + P_SP_SEND:
                addTextView(cmdData.getArg(), MSG_PRIV_FLAG);
                break;
            case P_RS_USER_LIST:
                flushSpinner(cmdData.getArg().split(P_SP_ARG));
                break;
        }
    }

    @Override
    public void doErr(String errCode) {
        switch (errCode) {

        }
    }
}
