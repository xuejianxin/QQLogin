package com.androxue.login.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.androxue.login.R;

/**
 * Created by JimCharles on 2017/10/6.
 */

public class Find_BackActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView back;
    protected EditText qq_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_find_back);
        init();
    }

    protected void init() {
        back = (ImageView) findViewById(R.id.imageView3);
        back.setOnClickListener(this);

        qq_number = (EditText) findViewById(R.id.editText7);
        qq_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qq_number.setSelection(qq_number.getText().length());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageView3:
                finish();
                break;
            case R.id.button4:
                break;
            default:
                break;
        }
    }
}
