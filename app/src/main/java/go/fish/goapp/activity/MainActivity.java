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

import go.fish.goapp.R;
import go.fish.goapp.entity.CommandData;
import go.fish.goapp.interfaces.TCPCallback;
import go.fish.goapp.service.SocketService;
import go.fish.goapp.utils.Chatter;
import go.fish.goapp.utils.EventHelper;
import go.fish.goapp.utils.IEventReceiver;

public class MainActivity extends AppCompatActivity implements TCPCallback {


    private LinearLayout mLlContent;
    private ArrayAdapter<String> mSpiAdapter;
    private EditText mEtSend;
    private Button mBtnSend;
    private ScrollView mScroll;
    private Button mBtnRefresh;
    private Spinner mSpiUsers;
    private String[] r_users;
    private String mName = "";
    private boolean isConn = false;
    private Handler mHandler = new Handler(Looper.myLooper());
    private boolean isAdapterFirst = true;
    private IEventReceiver<CommandData> mReceiver;
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
            Chatter.send(mEtSend.getText().toString());
            addTextViewR(mEtSend.getText().toString());
            mEtSend.setText("");
        });

        mBtnSend.setOnLongClickListener(v -> {
            String tmp = mEtSend.getText().toString();
            mName = tmp;
            Chatter.ChangeName(tmp);
            mEtSend.setText("");
            return false;
        });

        mBtnRefresh.setOnClickListener(v -> Chatter.freshUserList());
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
                if (r_users[position].equals("ALL")) {
                    Chatter.ClearChatUser();
                } else if (r_users[position].equals(mName) || r_users[position].equals("")) {
                    return;
                } else {
                    Chatter.ChangeUser(r_users[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void addTextView(String msg) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(mTvLayoutParams);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        tv.setPadding(0, 5, 0, 5);
        tv.setText(msg);
        if (msg.contains("(priv)")) {
            tv.setTextColor(0xFF77bbff);
        } else if (msg.startsWith("通知")) {
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
        tv.setText(mName + ":" + msg);
        mLlContent.addView(tv);
        DoDelayed(new Runnable() {
                      @Override
                      public void run() {
                          mScroll.fullScroll(ScrollView.FOCUS_DOWN);
                      }
                  }
        );

    }

    private void flushSpinner(String[] strs) {
        mSpiAdapter.clear();
        mSpiAdapter.addAll(strs);
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

        }
    }
}
