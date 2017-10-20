package com.androxue.login.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.androxue.login.R;
import com.androxue.login.util.RegexUtils;

/**
 * Created by JimCharles on 2017/10/6.
 */

public class SMSActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView back;
    protected Button next, another;
    protected EditText editText5, editText6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_model);
        init();
    }

    protected  void init() {
        back = (ImageView) findViewById(R.id.imageView2);
        back.setOnClickListener(this);

        next = (Button) findViewById(R.id.button3);
        next.setOnClickListener(this);

        another = (Button) findViewById(R.id.button7);
        another.setOnClickListener(this);

        editText5 = (EditText) findViewById(R.id.editText5);
        editText6 = (EditText) findViewById(R.id.editText6);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView3:
                finish();
                break;
            case R.id.button3:
                if (RegexUtils.checkMobile(editText5.getText().toString())) {
                    next.setBackground(this.getResources().getDrawable(R.drawable.button_login));
                    finish();
                } else {
                    next.setBackgroundColor(Color.TRANSPARENT);
                }
                break;
            default:
                break;
        }
    }
}
