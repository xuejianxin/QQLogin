好久不见，有段时间没更新了，今天给大家个实用的吧！仿 qq 的登录和注册模块，具体登录注册逻辑实现还需要自己实现，希望能帮到你。

## 布局界面的编写

布局界面采用 ConstraintLayout 来实现，比较简单，但是代码较多，大家稍后直接看工程，这里放一下运行结果。

![这里写图片描述](http://img.blog.csdn.net/20171010210846156?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamltX19jaGFybGVz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![这里写图片描述](http://img.blog.csdn.net/20171010210904534?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamltX19jaGFybGVz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![这里写图片描述](http://img.blog.csdn.net/20171010210921301?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamltX19jaGFybGVz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![这里写图片描述](http://img.blog.csdn.net/20171010210938404?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamltX19jaGFybGVz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![这里写图片描述](http://img.blog.csdn.net/20171010210952090?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamltX19jaGFybGVz/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

## 自定义动画及控件风格

### 自定义动画

首先是 PopupWindow 弹出动画的定义，我们在 res 下新建 anim package，然后定义划入和划出动画，代码如下：

popup_anim_in.xml
```
<translate xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="100"
    android:fromYDelta="100%"
    android:toYDelta="0%" >

</translate>
```

popup_anim_out.xml
```
<translate xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="100"
    android:fromYDelta="0%"
    android:toYDelta="100%" >

</translate>
```

然后在 values 下定义样式即可，添加如下代码：

```
<style name="popwin_anim_style">
        <item name="android:windowEnterAnimation">@anim/popup_anim_in</item>
        <item name="android:windowExitAnimation">@anim/popup_anim_out</item>
</style>
```

到这一步已经完成了，我们通过设置 style 属性即可使用此动画。

### 自定义控件风格、背景

这个很容易理解吧，Android 提供给我们的控件通常不够优美，需要对其进行修改，而自定义风格则是其中一种。下面以 Button 为例，在 drawable 中新建资源文件：

button_background.xml
```
<shape xmlns:android="http://schemas.android.com/apk/res/android">

    <corners android:radius="8dp" />
    <solid android:color="#ff00ddff" />

</shape>
```

然后通过 background 属性设置该风格即可实现效果。 

### 应用启动动画

通过自定义启动动画为应用增添色彩也是不见不错的事情，直接看代码：

```
public class SplashScreen {

    public final static int SLIDE_LEFT = 1;
    private final static int SLIDE_UP = 2;
    private final static int FADE_OUT = 3;

    private Activity activity;
    private Dialog splashDialog;

    public SplashScreen(Activity activity) {
        this.activity = activity;
    }

    public void show(final int imageResource, final int animation) {
        Runnable runnable = new Runnable() {
            public void run() {
                // Get reference to display
                DisplayMetrics metrics = new DisplayMetrics();

                // Create the layout for the dialog
                LinearLayout root = new LinearLayout(activity);
                root.setMinimumHeight(metrics.heightPixels);
                root.setMinimumWidth(metrics.widthPixels);
                root.setOrientation(LinearLayout.VERTICAL);
                root.setBackgroundColor(Color.BLACK);
                root.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 0.0F));
                root.setBackgroundResource(imageResource);

                // Create and show the dialog
                splashDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
                // check to see if the splash screen should be full screen
                if ((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
                    splashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                Window window = splashDialog.getWindow();
                switch (animation) {
                    case SLIDE_LEFT:
                        assert window != null;
                        window.setWindowAnimations(R.style.dialog_anim_slide_left);
                        break;
                    case SLIDE_UP:
                        assert window != null;
                        window.setWindowAnimations(R.style.dialog_anim_slide_up);
                        break;
                    case FADE_OUT:
                        assert window != null;
                        window.setWindowAnimations(R.style.dialog_anim_fade_out);
                        break;
                }

                splashDialog.setContentView(root);
                splashDialog.setCancelable(false);
                splashDialog.show();

            }
        };
        activity.runOnUiThread(runnable);
    }

    public void removeSplashScreen() {
        if (splashDialog != null && splashDialog.isShowing()) {
            splashDialog.dismiss();
            splashDialog = null;
        }
    }

}
```

这里通过动态加载布局来实现，没什么好说的，直接使用即可。

## PopupWindow 弹出底部选择框

底部弹出框通过 PopupWindow 实现，具体实现逻辑如下：

```
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
```
