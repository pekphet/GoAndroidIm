package go.fish.goapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import goandroid.Goandroid;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {


    private LinearLayout mLlContent;
    private LinearLayout.LayoutParams mTvLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private EditText    mEtSend;
    private Button      mBtnSend;
    private Handler     mHandler    = new Handler(Looper.myLooper());
    private boolean     isConn      = false;
    private ScrollView  mScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScroll     = (ScrollView) findViewById(R.id.scroll);
        mLlContent  = (LinearLayout) findViewById(R.id.ll_content);
        mEtSend     = (EditText) findViewById(R.id.et_send);
        mBtnSend    = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mBtnSend.setOnLongClickListener(this);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                initChatter();
//            }
//        }, 2000);
        initChatter();
    }

    private void initChatter() {
        Thread msgThread = new Thread(new Runnable() {
            private String msg;
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "连接服务器中。。。", Toast.LENGTH_SHORT).show();
                    }
                });
                if (Goandroid.conn()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "连接服务器成功", Toast.LENGTH_SHORT).show();
                    }
                });
                isConn = true;
                while (true) {
                    msg = Goandroid.receiver();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            addTextView(msg);
                        }
                    });
                }
            }
        });
        msgThread.start();
    }

    private void addTextView(String msg) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(mTvLayoutParams);
        tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        tv.setPadding(0, 5, 0, 5);
        tv.setText(msg);
        mLlContent.addView(tv);
        mScroll.fullScroll(ScrollView.FOCUS_DOWN);
    }

    private void addTextViewR(String msg) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(mTvLayoutParams);
        tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        tv.setPadding(0, 5, 0, 5);
        tv.setText(msg);
        mLlContent.addView(tv);
        mScroll.fullScroll(ScrollView.FOCUS_DOWN);
    }


    @Override
    public void onClick(View v) {
        if (isConn) {
            Goandroid.send(mEtSend.getText().toString());
            addTextViewR(mEtSend.getText().toString());
            mEtSend.setText("");
        } else {
            Toast.makeText(this, "没有连接到服务器", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (isConn) {
            String tmp = mEtSend.getText().toString();
            Goandroid.changeName(tmp);
            mEtSend.setText("");
        } else {
            Toast.makeText(this, "没有连接到服务器", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
