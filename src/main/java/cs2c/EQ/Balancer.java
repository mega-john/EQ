package cs2c.EQ;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
//import android.cs2c.IEQService;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Balancer extends Activity implements OnClickListener {
    private static int balanceDivisorX = 1;
    private static int balanceDivisorY = 1;
    private static int height = 0;
    private static int increaseValue = 0;
    private static boolean isSystemWallperSettings = false;
    private static int sbProgress = 0;
    private LinearLayout activity_background_rl;
    private int coordinateX;
    private int coordinateY;
    private int leftX;
    private int limitHeight = 344;
    private int limitWidth = 291;
    private TextView mAdd;
    private TextView mAddValueTextView;
    private IEQService mEQService;
    private TextView mEQTextView;
    private ImageView mImageView;
    private OnSeekBarChangeListener mOnSeekBarChangeListener = new C00001();
    OnTouchListener mOnTouchListener = new C00012();
    private TextView mResetTextView;
    private SeekBar mSeekBar;
    private SharedPreferences preferences;
    private int topY;

    /* renamed from: cs2c.EQ.Balancer$1 */
    class C00001 implements OnSeekBarChangeListener {
        C00001() {
        }

        public void onProgressChanged(SeekBar sb, int progress, boolean arg2) {
            Balancer.sbProgress = progress;
            Balancer.increaseValue = (Balancer.sbProgress * 4) / 101;
            Balancer.this.mAdd.setTextColor(Balancer.this.getResources().getColor(R.color.blue));
            Balancer.this.mAddValueTextView.setText("+" + String.valueOf(Balancer.increaseValue));
        }

        public void onStartTrackingTouch(SeekBar sb) {
        }

        public void onStopTrackingTouch(SeekBar sb) {
            Balancer.this.mSeekBar.setMax(100);
            if (Balancer.sbProgress < 25) {
                Balancer.sbProgress = 0;
            } else if (Balancer.sbProgress > 25 && Balancer.sbProgress < 50) {
                Balancer.sbProgress = 33;
            } else if (Balancer.sbProgress > 50 && Balancer.sbProgress < 75) {
                Balancer.sbProgress = 66;
            } else if (Balancer.sbProgress > 75) {
                Balancer.sbProgress = 100;
            }
            Balancer.this.mSeekBar.setProgress(Balancer.sbProgress);
            Balancer.this.mAdd.setTextColor(Balancer.this.getResources().getColor(R.color.white));
            Editor editor = Balancer.this.preferences.edit();
            editor.putInt("seekbar_progress_key", Balancer.sbProgress);
            editor.putInt("increase_value_key", Balancer.increaseValue);
            editor.commit();
        }
    }

    /* renamed from: cs2c.EQ.Balancer$2 */
    class C00012 implements OnTouchListener {
        int downX = 0;
        int downY = 0;
        int dx = 0;
        int dy = 0;
        int moveX = 0;
        int moveY = 0;
        int upX = 0;
        int upY = 0;

        C00012() {
        }

        public boolean onTouch(View v, MotionEvent mEvent) {
            switch (mEvent.getAction()) {
                case 0:
                    this.downX = (int) mEvent.getRawX();
                    this.downY = (int) mEvent.getRawY();
                    this.moveX = this.downX;
                    this.moveY = this.downY;
                    Log.d("lzc", "in down lastX=" + this.downX + "     lastY=" + this.downY);
                    break;
                case 1:
                    int diffX;
                    int diffY;
                    Log.d("lzc", "leftX=" + Balancer.this.leftX + "    topY=" + Balancer.this.topY);
                    if (EQActivity.width > 800) {
                        diffX = ((Balancer.this.leftX * 10) - 100) / Balancer.balanceDivisorX;
                        diffY = (257 - Balancer.this.topY) / Balancer.balanceDivisorY;
                    } else {
                        diffX = ((Balancer.this.leftX * 10) - 100) / Balancer.balanceDivisorX;
                        diffY = (205 - Balancer.this.topY) / Balancer.balanceDivisorY;
                    }
                    Log.d("lzc", "diffX=" + diffX + "    diffY=" + diffY);
                    Balancer.this.setXY(diffX, diffY);
                    Editor editor = Balancer.this.preferences.edit();
                    editor.putInt("CoordinateX", Balancer.this.leftX);
                    editor.putInt("CoordinateY", Balancer.this.topY);
                    editor.commit();
                    break;
                case 2:
                    this.dx = ((int) mEvent.getRawX()) - this.moveX;
                    this.dy = ((int) mEvent.getRawY()) - this.moveY;
                    int left = v.getLeft() + this.dx;
                    int top = v.getTop() + this.dy;
                    int right = v.getRight() + this.dx;
                    int bottom = v.getBottom() + this.dy;
                    Balancer.this.leftX = left;
                    Balancer.this.topY = top;
                    if (left < 10) {
                        left = 10;
                        right = 10 + v.getWidth();
                    }
                    if (right > Balancer.this.limitWidth - 10) {
                        right = Balancer.this.limitWidth - 10;
                        left = right - v.getWidth();
                    }
                    if (top < 10) {
                        top = 10;
                        bottom = 10 + v.getHeight();
                    }
                    if (bottom > Balancer.this.limitHeight - 10) {
                        bottom = Balancer.this.limitHeight - 10;
                        top = bottom - v.getHeight();
                    }
                    Log.d("hhhhhhhhhhhhhhhhhhhhhhlzc", "left=" + left + "  top=" + top + "  right=" + right + "  bottom=" + bottom);
                    v.layout(left, top, right, bottom);
                    v.postInvalidate();
                    this.moveX = (int) mEvent.getRawX();
                    this.moveY = (int) mEvent.getRawY();
                    this.upX = this.moveX;
                    this.upY = this.moveY;
                    break;
            }
            return true;
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        requestWindowFeature(1);
        setContentView(R.layout.music_balancer);
//        this.mEQService = (IEQService) getSystemService("eq");
        this.mEQService = (cs2c.EQ.IEQService) new EQService();

        initLimitValue();
        initCompoment();
        displayView();
        resetImageView();
        setupCompoment();
    }

    protected void onResume() {
        super.onResume();
        isSystemWallperSettings = this.preferences.getBoolean("cs2c.wallper.setting", false);
    }

    private void initLimitValue() {
        System.out.println("huahuahuahua  " + EQActivity.width);
        if (EQActivity.width > 800) {
            balanceDivisorX = 137;
            balanceDivisorY = 17;
            this.limitWidth = 305;
            this.limitHeight = 360;
            return;
        }
        balanceDivisorX = 100;
        balanceDivisorY = 12;
        this.limitWidth = 238;
        this.limitHeight = 288;
    }

    private void resetImageView() {
        int diffX;
        int diffY;
        Log.d("lzc", "resetImageView");
        if (EQActivity.width > 800) {
            this.coordinateX = this.preferences.getInt("CoordinateX", 106);
            this.coordinateY = this.preferences.getInt("CoordinateY", 138);
            this.mImageView.setLayoutParams(new LayoutParams(93, 93, this.coordinateX, this.coordinateY));
            diffX = ((this.coordinateX * 10) - 100) / balanceDivisorX;
            diffY = (257 - this.coordinateY) / balanceDivisorY;
        } else {
            this.coordinateX = this.preferences.getInt("CoordinateX", 82);
            this.coordinateY = this.preferences.getInt("CoordinateY", 110);
            this.mImageView.setLayoutParams(new LayoutParams(72, 72, this.coordinateX, this.coordinateY));
            diffX = ((this.coordinateX * 10) - 100) / balanceDivisorX;
            diffY = (205 - this.coordinateY) / balanceDivisorY;
        }
        setXY(diffX, diffY);
    }

    private void displayView() {
        increaseValue = this.preferences.getInt("increase_value_key", 0);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&7");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&7");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&7");
        System.out.println(increaseValue);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&7");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&7");
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&7");
        sbProgress = this.preferences.getInt("seekbar_progress_key", 3);
        try {
            this.mEQService.setSound(32, increaseValue);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.mAddValueTextView.setText("+" + String.valueOf(increaseValue));
        this.mSeekBar.setProgress(sbProgress);
    }

    private void initCompoment() {
        this.preferences = getSharedPreferences("musicEQ", 0);
        this.activity_background_rl = (LinearLayout) findViewById(R.id.activity_background_rl);
        this.mImageView = (ImageView) findViewById(R.id.music_balancer_dot);
        this.mAddValueTextView = (TextView) findViewById(R.id.music_balancer_add);
        this.mEQTextView = (TextView) findViewById(R.id.music_equalizer);
        this.mResetTextView = (TextView) findViewById(R.id.music_reset);
        this.mSeekBar = (SeekBar) findViewById(R.id.music_balancer_seekbar);
        this.mAdd = (TextView) findViewById(R.id.music_balancer_add);
    }

    private void setupCompoment() {
        this.mImageView.setOnTouchListener(this.mOnTouchListener);
        this.mEQTextView.setOnClickListener(this);
        this.mResetTextView.setOnClickListener(this);
        this.mSeekBar.setOnSeekBarChangeListener(this.mOnSeekBarChangeListener);
        this.mAdd.setOnClickListener(this);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
            case 1:
                int endx = ((int) event.getRawX()) - 680;
                int endy = 420 - ((int) event.getRawY());
                int diffx = endx / 15;
                int diffy = endy / 20;
                int end = 280 - endy;
                if (diffx >= 0 && diffx < 14 && diffy >= 0 && diffy <= 14) {
                    this.mImageView.setLayoutParams(new LayoutParams(93, 93, endx, end));
                    setXY(diffx, diffy);
                    Editor editor = this.preferences.edit();
                    editor.putInt("CoordinateX", endx);
                    editor.putInt("CoordinateY", end);
                    editor.commit();
                    break;
                }
        }
        return true;
    }

    public int checkItem(int value) {
        if (value > 14) {
            return 14;
        }
        if (value < 0) {
            return 0;
        }
        return value;
    }

    public void setXY(int x, int y) {
        x = checkItem(x);
        y = checkItem(y);
        Log.d("royu5", "====x=" + x + "y=" + y);
        try {
            this.mEQService.setSurround(x, 24 - y);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music_equalizer:
                Intent equalizerIntent = new Intent();
                equalizerIntent.setClass(this, EQActivity.class);
                startActivity(equalizerIntent);
                return;
            case R.id.music_reset:
                int diffX;
                int diffY;
                sbProgress = 0;
                this.mSeekBar.setMax(100);
                this.mSeekBar.setProgress(sbProgress);
                increaseValue = 0;
                if (EQActivity.width > 800) {
                    this.coordinateX = 106;
                    this.coordinateY = 138;
                    this.mImageView.setLayoutParams(new LayoutParams(93, 93, this.coordinateX, this.coordinateY));
                    diffX = ((this.coordinateX * 10) - 100) / balanceDivisorX;
                    diffY = (257 - this.coordinateY) / balanceDivisorY;
                } else {
                    this.coordinateX = 82;
                    this.coordinateY = 110;
                    this.mImageView.setLayoutParams(new LayoutParams(72, 72, this.coordinateX, this.coordinateY));
                    diffX = ((this.coordinateX * 10) - 100) / balanceDivisorX;
                    diffY = (205 - this.coordinateY) / balanceDivisorY;
                }
                try {
                    this.mEQService.setSound(32, increaseValue);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                setXY(diffX, diffY);
                this.mAdd.setTextColor(getResources().getColor(R.color.white));
                Editor editor = this.preferences.edit();
                editor.putInt("CoordinateX", this.coordinateX);
                editor.putInt("CoordinateY", this.coordinateY);
                editor.putInt("seekbar_progress_key", sbProgress);
                editor.putInt("increase_value_key", increaseValue);
                editor.commit();
                return;
            default:
                Log.d("lzc", "v.getId()=" + v.getId());
                return;
        }
    }
}
