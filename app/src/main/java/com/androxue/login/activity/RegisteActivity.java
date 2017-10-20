package com.androxue.login.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androxue.login.R;
import com.androxue.login.util.RegexUtils;
import com.androxue.login.util.ToastUtils;
import com.androxue.login.widget.ActivityCollector;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by JimCharles on 2017/9/27.
 */

public class RegisteActivity extends AppCompatActivity implements View.OnClickListener {

    private String result = "";
    protected ImageView back;
    protected Button registe;
    protected TextView textView6;
    protected EditText editText3, editText4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    protected void init() {
        back = (ImageView) findViewById(R.id.imageView);
        back.setOnClickListener(this);

        textView6 = (TextView) findViewById(R.id.textView6);
        SpannableStringBuilder builder = new SpannableStringBuilder(textView6.getText().toString());
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#ff00ddff"));
        ForegroundColorSpan blueSpan_1 = new ForegroundColorSpan(Color.parseColor("#ff00ddff"));
        builder.setSpan(blueSpan, 7, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(blueSpan_1, 12, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView6.setText(builder);

        registe = (Button) findViewById(R.id.button2);
        registe.setOnClickListener(this);

        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.finishAll();
    }

    protected void registe(String account) {

    }

    protected void clickRegiste(String account) {
        finish();
        if (checkInput(account)) {
            registe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == 1) {
                                //提示读取结果
                                Toast.makeText(RegisteActivity.this, result, Toast.LENGTH_SHORT).show();
                                ToastUtils.showShort(RegisteActivity.this,
                                        result);

                                    final Intent it = new Intent(RegisteActivity.this, MainActivity.class); //开启动画
                                    Timer timer = new Timer();
                                    TimerTask task = new TimerTask() {
                                        @Override
                                        public void run() {
                                            startActivity(it); //执行
                                        }
                                    };
                                    timer.schedule(task, 1000); //1秒后
                            }
                        }
                    };

                    new Thread() {
                        public void run() {
                            //请求网络
                            registe(editText3.getText().toString());
                            Message m = new Message();
                            m.what = 1;
                            // 发送消息到Handler
                            handler.sendMessage(m);
                        }
                    }.start();
                }
            });
        }
    }

    /**
     * 检查输入
     *
     * @param account
     * @return
     */
    public boolean checkInput(String account) {
        // 账号为空时提示
        if (account == null || account.trim().equals("")) {
            Toast.makeText(this, R.string.tip_account_empty, Toast.LENGTH_SHORT)
                    .show();
        } else {
            // 账号不匹配手机号格式（11位数字且以1开头）
            if ( !RegexUtils.checkMobile(account)) {
                Toast.makeText(this, R.string.tip_account_regex_not_right,
                        Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                finish();
                break;
            case R.id.button2:
                if (RegexUtils.checkMobile(editText3.getText().toString())) {
                    registe.setBackground(this.getResources().getDrawable(R.drawable.button_login));
                    clickRegiste(editText3.getText().toString());
                } else {
                    registe.setBackgroundColor(Color.TRANSPARENT);
                }
                break;
            default:
                break;
        }
    }
}
