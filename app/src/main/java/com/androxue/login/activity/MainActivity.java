package com.androxue.login.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.androxue.login.R;
import com.androxue.login.util.AppConstants;
import com.androxue.login.util.CheckUtils;
import com.androxue.login.util.HandlerUtil;
import com.androxue.login.util.LogUtils;
import com.androxue.login.util.ProgressDialogUtils;
import com.androxue.login.util.RegexUtils;
import com.androxue.login.util.SpUtils;
import com.androxue.login.util.ToastUtils;
import com.androxue.login.widget.CircleProgressView;
import com.androxue.login.widget.SplashScreen;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";

    private String result = "";
    private CircleProgressView circleProgressView;
    protected Button login, another;
    protected PopupWindow popupWindow;
    protected SplashScreen splashScreen;
    protected EditText editText, editText2;
    protected ImageView wechat, qq, sina;
    protected TextView textView, textView2, textView3;

    // 第三方平台获取的访问token，有效时间，uid
    private String sns;
    private String uid;
    private String expires_in;
    private String accessToken;

    private UMSocialService mController = UMServiceFactory
            .getUMSocialService(AppConstants.DESCRIPTOR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        splashScreen = new SplashScreen(this);
        splashScreen.show(R.drawable.art_login_bg,
                SplashScreen.SLIDE_LEFT);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);
        HandlerUtil.getInstance(this).postDelayed(new Runnable() {
            @Override
            public void run() {
                splashScreen.removeSplashScreen();
            }
        }, 1500);
        init();
    }

    protected void init() {
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        qq = (ImageView) findViewById(R.id.iv_qq);
        sina = (ImageView) findViewById(R.id.iv_sina);
        wechat = (ImageView) findViewById(R.id.iv_wechat);

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);

        login = (Button) findViewById(R.id.button);
        another = (Button) findViewById(R.id.button5);

        //circleProgressView = (CircleProgressView) findViewById(R.id.);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomMenu(view);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisteActivity.class);
                startActivity(intent);
            }
        });

        SpannableString spannableString = new SpannableString("登录即代表同意服务条款");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#ff00ddff")), 7,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView3.setText(spannableString);


        login.setOnClickListener(this);
        another.setOnClickListener(this);
    }

    protected void showBottomMenu(View v) {
        //加载PopupWindow布局
        View view = View.inflate(this, R.layout.activity_forgrt_password, null);
        popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);//设置动画
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);//设置popupwindow的位置
        backgroundAlpha(0.6f);//设置背景半透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }

    protected void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 检查输入
     *
     * @param account
     * @param password
     * @return
     */
    public boolean checkInput(String account, String password) {
        // 账号为空时提示
        if (account == null || account.trim().equals("")) {
            Toast.makeText(this, R.string.tip_account_empty, Toast.LENGTH_SHORT)
                    .show();
        } else {
            // 账号不匹配手机号格式（11位数字且以1开头）
            if ( !RegexUtils.checkMobile(account)) {
                Toast.makeText(this, R.string.tip_account_regex_not_right,
                        Toast.LENGTH_SHORT).show();
            } else if (password == null || password.trim().equals("")) {
                Toast.makeText(this, R.string.tip_password_can_not_be_empty,
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
            case R.id.cancel:
                popupWindow.dismiss();
                break;
            case R.id.feedback:
                Intent intent = new Intent(MainActivity.this, Find_BackActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
                break;
            case R.id.back:
                Intent intent_1 = new Intent(MainActivity.this, SMSActivity.class);
                startActivity(intent_1);
                popupWindow.dismiss();
                break;
            case R.id.iv_qq:
                clickLoginQQ();
                break;
            case R.id.iv_sina:
                loginThirdPlatform(SHARE_MEDIA.SINA);
                break;
            case R.id.iv_wechat:
                clickLoginWexin();
                break;
            case R.id.button:
                clickLogin();
                //circleProgressView.startAnimation();//加载动画
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashScreen.removeSplashScreen();
    }

    private void login(String account, String password) {

    }

    /**
     * 点击使用QQ快速登录
     */
    private void clickLoginQQ() {
        if (!CheckUtils.isQQClientAvailable(this)) {
            ToastUtils.showShort(MainActivity.this,
                    getString(R.string.no_install_qq));
        } else {
            loginThirdPlatform(SHARE_MEDIA.QZONE);
        }
    }

    /**
     * 点击使用微信登录
     */
    private void clickLoginWexin() {
        if (!CheckUtils.isWeixinAvilible(this)) {
            ToastUtils.showShort(MainActivity.this,
                    getString(R.string.no_install_wechat));
        } else {
            loginThirdPlatform(SHARE_MEDIA.WEIXIN);
        }
    }

    private void clickLogin() {
        String account = editText2.getText().toString();
        String password = editText.getText().toString();

        if (checkInput(account, password)) {
            // TODO: 请求服务器登录账号
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    //使用子线程执行访问服务器，获取返回信息后通知主线程更新UI或者提示信息。
                    @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == 1) {
                                //提示读取结果
                                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                                ToastUtils.showShort(MainActivity.this,
                                        result);
                                if (result.contains("！")) {
                                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                                    ToastUtils.showShort(MainActivity.this,
                                            "密码错误......");
                                } else {
                                    final Intent it = new Intent(MainActivity.this, RegisteActivity.class); //开启动画
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
                        }
                    };
                    // 启动线程来执行任务
                    new Thread() {
                        public void run() {
                            //请求网络
                            login(editText.getText().toString(), editText2.getText().toString());
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

    private void loginThirdPlatform(final SHARE_MEDIA platform) {

        mController.doOauthVerify(MainActivity.this, platform,
                new SocializeListeners.UMAuthListener() {

                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        LogUtils.i(TAG, "onStart------"
                                + Thread.currentThread().getId());
                        ProgressDialogUtils.getInstance().show(
                                MainActivity.this,
                                getString(R.string.tip_begin_oauth));
                    }

                    @Override
                    public void onError(SocializeException e,
                                        SHARE_MEDIA platform) {
                        LogUtils.i(TAG, "onError------"
                                + Thread.currentThread().getId());
                        ToastUtils.showShort(MainActivity.this,
                                getString(R.string.oauth_fail));
                        ProgressDialogUtils.getInstance().dismiss();
                    }

                    @Override
                    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                        LogUtils.i(TAG, "onComplete------" + value.toString());
                        if (platform == SHARE_MEDIA.SINA) {
                            accessToken = value.getString("access_key");
                        } else {
                            accessToken = value.getString("access_token");
                        }
                        expires_in = value.getString("expires_in");
                        // 获取uid
                        uid = value.getString(AppConstants.UID);
                        if (!TextUtils.isEmpty(uid)) {
                            // uid不为空，获取用户信息
                            getUserInfo(platform);
                        } else {
                            ToastUtils.showShort(MainActivity.this,
                                    getString(R.string.oauth_fail));
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        LogUtils.i(TAG, "onCancel------"
                                + Thread.currentThread().getId());
                        ToastUtils.showShort(MainActivity.this,
                                getString(R.string.oauth_cancle));
                        ProgressDialogUtils.getInstance().dismiss();

                    }
                });
    }

    private void getUserInfo(final SHARE_MEDIA platform) {
        mController.getPlatformInfo(MainActivity.this, platform,
                new SocializeListeners.UMDataListener() {

                    @Override
                    public void onStart() {
                        // 开始获取
                        LogUtils.i("getUserInfo", "onStart------");
                        ProgressDialogUtils.getInstance().dismiss();
                        ProgressDialogUtils.getInstance().show(
                                MainActivity.this, "正在请求...");
                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {

                        try {
                            String sns_id = "";
                            String sns_loginname = "";
                            if (info != null && info.size() != 0) {
                                LogUtils.i("third login", info.toString());

                                if (platform == SHARE_MEDIA.SINA) { // 新浪微博
                                    sns = AppConstants.SINA;
                                    if (info.get(AppConstants.UID) != null) {
                                        sns_id = info.get(AppConstants.UID)
                                                .toString();
                                    }
                                    if (info.get(AppConstants.SCREEN_NAME) != null) {
                                        sns_loginname = info.get(AppConstants.SCREEN_NAME)
                                                .toString();
                                    }
                                } else if (platform == SHARE_MEDIA.QZONE) { // QQ
                                    sns = AppConstants.QQ;
                                    if (info.get(AppConstants.UID) == null) {
                                        ToastUtils.showShort(MainActivity.this, getString(R.string.oauth_fail));
                                        return;
                                    }
                                    sns_id = info.get(AppConstants.UID)
                                            .toString();
                                    sns_loginname = info.get(
                                            AppConstants.SCREEN_NAME)
                                            .toString();
                                } else if (platform == SHARE_MEDIA.WEIXIN) { // 微信
                                    sns = AppConstants.WECHAT;
                                    sns_id = info.get(AppConstants.OPENID)
                                            .toString();
                                    sns_loginname = info.get(
                                            AppConstants.NICKNAME).toString();
                                }

                                // 这里直接保存第三方返回来的用户信息
                                SpUtils.putBoolean(MainActivity.this,
                                        AppConstants.THIRD_LOGIN);

                                LogUtils.e(sns + "," + sns_id + ","
                                        + sns_loginname);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
