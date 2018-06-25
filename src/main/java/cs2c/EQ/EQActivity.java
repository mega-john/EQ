package cs2c.EQ;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
//import android.cs2c.IEQService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import cs2c.EQ.VerticalSeekBar.OnSeekBarChangeListener;

public class EQActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {
    private static int eqValue = 6;
    public static int height = 0;
    private static boolean isSystemWallperSettings = false;
    private static int vbProgress = 0;
    private static int vbProgress1 = 0;
    private static int vbProgress2 = 0;
    private static int vbProgress3 = 0;
    private static int vbProgress4 = 0;
    private static int vbProgress5 = 0;
    private static int vbProgress6 = 0;
    private static int vbProgressValue = 0;
    private static int vbProgressValue1 = 0;
    private static int vbProgressValue2 = 0;
    private static int vbProgressValue3 = 0;
    private static int vbProgressValue4 = 0;
    private static int vbProgressValue5 = 0;
    private static int vbProgressValue6 = 0;
    private static int vbProgressValue7 = 0;
    private static int vbProgressVariable = 0;
    private static int vbProgressVariable1 = 0;
    private static int vbProgressVariable2 = 0;
    private static int vbProgressVariable3 = 0;
    private static int vbProgressVariable4 = 0;
    private static int vbProgressVariable5 = 0;
    private static int vbProgressVariable6 = 0;
    private static int vbProgress_loud = 0;
    public static int width = 0;
    private RelativeLayout activity_background_rl;
    private TextView mBalancerTextView;
    DispearA mDispearA;
    private IEQService mEQService;
    private VerticalSeekBar mHighFVSB;
    private TextView mHighFVValue;
    private VerticalSeekBar mHighVoiceVSB;
    private TextView mHighVoiceValue;
    private VerticalSeekBar mLoudSeekBar;
    private TextView mLoudValue;
    private VerticalSeekBar mLowFVSB;
    private TextView mLowFVValue;
    private VerticalSeekBar mLowVoiceVSB;
    private TextView mLowVoiceValue;
    private VerticalSeekBar mMiddleFVSB;
    private TextView mMiddleFVValue;
    private VerticalSeekBar mMiddleVoiceVSB;
    private TextView mMiddleVoiceValue;
    private OnClickListener mOnClickListener = new C00043();
    private OnLongClickListener mOnLongClickListener = new C00032();
    private TextView mPWClasses;
    private TextView mPWFashion;
    private TextView mPWFlat;
    private TextView mPWJazz;
    private TextView mPWRock;
    private TextView mPWUser;
    private TextView mResetTextView;
    private TextView mRockTextView;
    Switch mSwitch;
    private SharedPreferences preferences;
    private PopupWindow window;

    /* renamed from: cs2c.EQ.EQActivity$1 */
    class C00021 implements OnCheckedChangeListener {
        C00021() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Editor editor = EQActivity.this.preferences.edit();
            if (isChecked) {
                try {
                    EQActivity.this.mEQService.set_volume(22, 1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                editor.putInt("eq_loud", 1);
                editor.commit();
                Log.d("EQ", "switch LOUD 1");
                return;
            }
            try {
                EQActivity.this.mEQService.set_volume(22, 0);
            } catch (RemoteException e2) {
                e2.printStackTrace();
            }
            editor.putInt("eq_loud", 0);
            editor.commit();
            Log.d("EQ", "switch LOUD 0");
        }
    }

    /* renamed from: cs2c.EQ.EQActivity$2 */
    class C00032 implements OnLongClickListener {
        C00032() {
        }

        public boolean onLongClick(View v) {
            if (EQActivity.width > 800) {
                EQActivity.this.showBigPopWindow();
            } else {
                EQActivity.this.showPopWindow();
            }
            EQActivity.this.mDispearA = new DispearA();
            EQActivity.this.mDispearA.sendEmptyMessageDelayed(1, 5000);
            return true;
        }
    }

    /* renamed from: cs2c.EQ.EQActivity$3 */
    class C00043 implements OnClickListener {
        C00043() {
        }

        public void onClick(View v) {
            Editor editor = EQActivity.this.preferences.edit();
            if (EQActivity.this.mPWRock == v) {
                EQActivity.eqValue = 1;
            } else if (EQActivity.this.mPWClasses == v) {
                EQActivity.eqValue = 2;
            } else if (EQActivity.this.mPWJazz == v) {
                EQActivity.eqValue = 3;
            } else if (EQActivity.this.mPWFashion == v) {
                EQActivity.eqValue = 4;
            } else if (EQActivity.this.mPWFlat == v) {
                EQActivity.eqValue = 5;
            } else if (EQActivity.this.mPWUser == v) {
                EQActivity.eqValue = 6;
            }
            editor.putInt("eq_value", EQActivity.eqValue);
            EQActivity.this.changeBottomSeekBar(EQActivity.eqValue);
            editor.commit();
            EQActivity.this.displayEQ();
            EQActivity.this.displayEQValue();
            EQActivity.this.setEQValue();
            EQActivity.this.window.dismiss();
        }
    }

    class DispearA extends Handler {
        DispearA() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    EQActivity.this.window.dismiss();
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.bt_frame_music_eq);
        if (this.mEQService == null) {
            this.mEQService = (IEQService) new EQService();
            System.out.println("------------------------------------------------");
        }
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        this.preferences = getSharedPreferences("musicEQ", 2);
        initCommonent();
        setOnClick();
        initSeekBarMax();
        this.mRockTextView.setSelected(true);
        eqValue = this.preferences.getInt("eq_value", 5);
        displayEQ();
        displayEQValue();
        changeBottomSeekBar(eqValue);
    }

    protected void onResume() {
        super.onResume();
        SharedPreferences settings = getApplicationContext().getSharedPreferences("musicEQ", 2);
        if (this.preferences.getInt("eq_loud", 0) == 1) {
            this.mSwitch.setChecked(true);
        } else {
            this.mSwitch.setChecked(false);
        }
        int progress = this.preferences.getInt("loud_bar", 50);
        this.mLoudValue.setText(String.valueOf(((progress * 14) / 100) - 7));
        this.mLoudSeekBar.setProgress(progress);
        isSystemWallperSettings = settings.getBoolean("musicEQ", false);
        Log.d("lzc", "Mode=2");
        Log.d("lzc", "isSystemWallperSettings=" + isSystemWallperSettings);
    }

    public void displayEQ() {
        switch (eqValue) {
            case 1:
                this.mRockTextView.setText(R.string.rock);
                return;
            case 2:
                this.mRockTextView.setText(R.string.classes);
                return;
            case 3:
                this.mRockTextView.setText(R.string.jazz);
                return;
            case 4:
                this.mRockTextView.setText(R.string.fashion);
                return;
            case 5:
                this.mRockTextView.setText(R.string.flat);
                return;
            case 6:
                this.mRockTextView.setText(R.string.user);
                return;
            default:
                Log.d("lzc", "eqValue=" + eqValue);
                return;
        }
    }

    private void setVoiceValue(int[] value) {
        this.mLowVoiceValue.setText(value[0]);
        this.mMiddleVoiceValue.setText(value[1]);
        this.mHighVoiceValue.setText(value[2]);
        this.mLowFVValue.setText(value[3]);
        this.mMiddleFVValue.setText(value[4]);
        this.mHighFVValue.setText(value[5]);
    }

    private void displayEQValue() {
        switch (eqValue) {
            case 1:
                setVoiceValue(new int[]{R.string.three, R.string.zero, R.string.three, R.string.eighty, R.string.one_khz, R.string.ten_khz});
                return;
            case 2:
                setVoiceValue(new int[]{R.string.three, R.string.negative_four, R.string.one, R.string.eighty, R.string.one_khz, R.string.ten_khz});
                return;
            case 3:
                setVoiceValue(new int[]{R.string.negative_there, R.string.two, R.string.negative_one, R.string.eighty, R.string.one_khz, R.string.ten_khz});
                return;
            case 4:
                setVoiceValue(new int[]{R.string.zero, R.string.five, R.string.three, R.string.eighty, R.string.one_khz, R.string.ten_khz});
                return;
            case 5:
                setVoiceValue(new int[]{R.string.zero, R.string.zero, R.string.zero, R.string.eighty, R.string.one_khz, R.string.ten_khz});
                return;
            case 6:
                vbProgressValue1 = this.preferences.getInt("lowVoiceSBProgressValue", 7) - 7;
                vbProgressValue2 = this.preferences.getInt("middleVoiceSBProgressValue", 7) - 7;
                vbProgressValue3 = this.preferences.getInt("highVoiceSBProgressValue", 7) - 7;
                vbProgressValue4 = this.preferences.getInt("lowFreqSBProgressValue", 1);
                vbProgressValue5 = this.preferences.getInt("middleFreqSBProgressValue", 1);
                vbProgressValue6 = this.preferences.getInt("highFreqSBProgressValue", 0);
                this.mLowVoiceValue.setText(String.valueOf(vbProgressValue1));
                this.mMiddleVoiceValue.setText(String.valueOf(vbProgressValue2));
                this.mHighVoiceValue.setText(String.valueOf(vbProgressValue3));
                disPlayLowFVByValue(vbProgressValue4);
                disPlayMiddleFVByValue(vbProgressValue5);
                disPlayHighFVByValue(vbProgressValue6);
                return;
            default:
                Log.d("lzc", "eqValue=" + eqValue);
                return;
        }
    }

    private void disPlayLowFVByValue(int value) {
        if (value == 0) {
            this.mLowFVValue.setText(R.string.sixty);
        } else if (value == 1) {
            this.mLowFVValue.setText(R.string.eighty);
        } else if (value == 2) {
            this.mLowFVValue.setText(R.string.one_hundred);
        } else {
            this.mLowFVValue.setText(R.string.two_hundred);
        }
    }

    private void disPlayMiddleFVByValue(int value) {
        if (value == 0) {
            this.mMiddleFVValue.setText(R.string.point_five_khz);
        } else if (value == 1) {
            this.mMiddleFVValue.setText(R.string.one_khz);
        } else if (value == 2) {
            this.mMiddleFVValue.setText(R.string.one_point_five_khz);
        } else {
            this.mMiddleFVValue.setText(R.string.two_point_five_khz);
        }
    }

    private void disPlayHighFVByValue(int value) {
        if (value == 0) {
            this.mHighFVValue.setText(R.string.ten_khz);
        } else if (value == 1) {
            this.mHighFVValue.setText(R.string.twelve_point_five_khz);
        } else if (value == 2) {
            this.mHighFVValue.setText(R.string.fivteen_khz);
        } else {
            this.mHighFVValue.setText(R.string.seventeen_point_five_khz);
        }
    }

    private void setEQValue() {
        try {
            switch (eqValue) {
                case 1:
                    this.mEQService.setSound(3, 10);
                    this.mEQService.setSound(2, 7);
                    this.mEQService.setSound(1, 10);
                    this.mEQService.setSound(13, 0);
                    this.mEQService.setSound(12, 1);
                    this.mEQService.setSound(11, 1);
                    return;
                case 2:
                    this.mEQService.setSound(3, 8);
                    this.mEQService.setSound(2, 3);
                    this.mEQService.setSound(1, 10);
                    this.mEQService.setSound(13, 0);
                    this.mEQService.setSound(12, 1);
                    this.mEQService.setSound(11, 1);
                    return;
                case 3:
                    this.mEQService.setSound(3, 6);
                    this.mEQService.setSound(2, 9);
                    this.mEQService.setSound(1, 4);
                    this.mEQService.setSound(13, 0);
                    this.mEQService.setSound(12, 1);
                    this.mEQService.setSound(11, 1);
                    return;
                case 4:
                    this.mEQService.setSound(3, 10);
                    this.mEQService.setSound(2, 12);
                    this.mEQService.setSound(1, 7);
                    this.mEQService.setSound(13, 0);
                    this.mEQService.setSound(12, 1);
                    this.mEQService.setSound(11, 1);
                    return;
                case 5:
                    this.mEQService.setSound(3, 7);
                    this.mEQService.setSound(2, 7);
                    this.mEQService.setSound(1, 7);
                    this.mEQService.setSound(13, 0);
                    this.mEQService.setSound(12, 1);
                    this.mEQService.setSound(11, 1);
                    return;
                case 6:
                    vbProgressVariable1 = this.preferences.getInt("lowFreqSBProgressValue", 1);
                    vbProgressVariable2 = this.preferences.getInt("middleFreqSBProgressValue", 1);
                    vbProgressVariable3 = this.preferences.getInt("highFreqSBProgressValue", 0);
                    vbProgressVariable4 = this.preferences.getInt("lowVoiceSBProgressValue", 7);
                    vbProgressVariable5 = this.preferences.getInt("middleVoiceSBProgressValue", 7);
                    vbProgressVariable6 = this.preferences.getInt("highVoiceSBProgressValue", 7);
                    Log.i("1", String.valueOf(vbProgressVariable4));
                    Log.i("2", String.valueOf(vbProgressVariable5));
                    Log.i("3", String.valueOf(vbProgressVariable6));
                    this.mEQService.setSound(3, vbProgressVariable6);
                    this.mEQService.setSound(2, vbProgressVariable5);
                    this.mEQService.setSound(1, vbProgressVariable4);
                    this.mEQService.setSound(13, vbProgressVariable3);
                    this.mEQService.setSound(12, vbProgressVariable2);
                    this.mEQService.setSound(11, vbProgressVariable1);
                    return;
                default:
                    Log.d("lzc", "eqValue=" + eqValue);
                    return;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void initCommonent() {
        this.mSwitch = (Switch) findViewById(R.id.loud_sw);
        this.mRockTextView = (TextView) findViewById(R.id.bt_music_rock);
        this.mBalancerTextView = (TextView) findViewById(R.id.bt_music_balancer);
        this.mResetTextView = (TextView) findViewById(R.id.bt_music_reset);
        this.mLowVoiceVSB = (VerticalSeekBar) findViewById(R.id.bt_music_vseekbar1);
        this.mMiddleVoiceVSB = (VerticalSeekBar) findViewById(R.id.bt_music_vseekbar2);
        this.mHighVoiceVSB = (VerticalSeekBar) findViewById(R.id.bt_music_vseekbar3);
        this.mLowFVSB = (VerticalSeekBar) findViewById(R.id.bt_music_vseekbar4);
        this.mMiddleFVSB = (VerticalSeekBar) findViewById(R.id.bt_music_vseekbar5);
        this.mHighFVSB = (VerticalSeekBar) findViewById(R.id.bt_music_vseekbar6);
        this.mLoudSeekBar = (VerticalSeekBar) findViewById(R.id.bt_loud_vseekbar);
        this.mLowVoiceValue = (TextView) findViewById(R.id.bt_music_text_low_voice_value);
        this.mMiddleVoiceValue = (TextView) findViewById(R.id.bt_music_text_middle_voice_value);
        this.mHighVoiceValue = (TextView) findViewById(R.id.bt_music_text_high_voice_value);
        this.mLowFVValue = (TextView) findViewById(R.id.bt_music_text_low_f_value);
        this.mMiddleFVValue = (TextView) findViewById(R.id.bt_music_text_middle_f_value);
        this.mHighFVValue = (TextView) findViewById(R.id.bt_music_text_high_f_value);
        this.mLoudValue = (TextView) findViewById(R.id.bt_loud_text_tottom);
        this.activity_background_rl = (RelativeLayout) findViewById(R.id.activity_background_rl);
    }

    private void setOnClick() {
        this.mRockTextView.setOnClickListener(this);
        this.mRockTextView.setOnLongClickListener(this.mOnLongClickListener);
        this.mBalancerTextView.setOnClickListener(this);
        this.mResetTextView.setOnClickListener(this);
        this.mLowVoiceVSB.setOnSeekBarChangeListener(this);
        this.mMiddleVoiceVSB.setOnSeekBarChangeListener(this);
        this.mHighVoiceVSB.setOnSeekBarChangeListener(this);
        this.mLowFVSB.setOnSeekBarChangeListener(this);
        this.mMiddleFVSB.setOnSeekBarChangeListener(this);
        this.mHighFVSB.setOnSeekBarChangeListener(this);
        this.mLoudSeekBar.setOnSeekBarChangeListener(this);
        this.mSwitch.setOnCheckedChangeListener(new C00021());
    }

    public void showBigPopWindow() {
        View v = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.popwindow_frame, null);
        Log.d("TAG", "gggggggggggggggggggggggggggggggggggg");
        this.window = new PopupWindow(v, 942, 160);
        this.mPWRock = (TextView) v.findViewById(R.id.eq_music_rock);
        this.mPWClasses = (TextView) v.findViewById(R.id.eq_music_class);
        this.mPWJazz = (TextView) v.findViewById(R.id.eq_music_jazz);
        this.mPWFashion = (TextView) v.findViewById(R.id.eq_music_fashion);
        this.mPWFlat = (TextView) v.findViewById(R.id.eq_music_flat);
        this.mPWUser = (TextView) v.findViewById(R.id.eq_music_option);
        displayEQSelected();
        diaplayEQColor();
        this.mPWRock.setOnClickListener(this.mOnClickListener);
        this.mPWClasses.setOnClickListener(this.mOnClickListener);
        this.mPWJazz.setOnClickListener(this.mOnClickListener);
        this.mPWFashion.setOnClickListener(this.mOnClickListener);
        this.mPWFlat.setOnClickListener(this.mOnClickListener);
        this.mPWUser.setOnClickListener(this.mOnClickListener);
        this.window.setFocusable(true);
        this.window.showAtLocation(this.mRockTextView, 0, 38, 275);
    }

    public void showPopWindow() {
        View v = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.popwindow_frame, null);
        Log.d("TAG", "ddddddddddddddddddddddddddddddddd");
        this.window = new PopupWindow(v, 702, 125);
        this.mPWRock = (TextView) v.findViewById(R.id.eq_music_rock);
        this.mPWClasses = (TextView) v.findViewById(R.id.eq_music_class);
        this.mPWJazz = (TextView) v.findViewById(R.id.eq_music_jazz);
        this.mPWFashion = (TextView) v.findViewById(R.id.eq_music_fashion);
        this.mPWFlat = (TextView) v.findViewById(R.id.eq_music_flat);
        this.mPWUser = (TextView) v.findViewById(R.id.eq_music_option);
        displayEQSelected();
        diaplayEQColor();
        this.mPWRock.setOnClickListener(this.mOnClickListener);
        this.mPWClasses.setOnClickListener(this.mOnClickListener);
        this.mPWJazz.setOnClickListener(this.mOnClickListener);
        this.mPWFashion.setOnClickListener(this.mOnClickListener);
        this.mPWFlat.setOnClickListener(this.mOnClickListener);
        this.mPWUser.setOnClickListener(this.mOnClickListener);
        this.window.setFocusable(true);
        this.window.showAtLocation(this.mRockTextView, 0, 45, 275);
    }

    public void displayEQSelected() {
        switch (eqValue) {
            case 1:
                this.mPWRock.setSelected(true);
                this.mPWClasses.setSelected(false);
                this.mPWJazz.setSelected(false);
                this.mPWFashion.setSelected(false);
                this.mPWFlat.setSelected(false);
                this.mPWUser.setSelected(false);
                return;
            case 2:
                this.mPWRock.setSelected(false);
                this.mPWClasses.setSelected(true);
                this.mPWJazz.setSelected(false);
                this.mPWFashion.setSelected(false);
                this.mPWFlat.setSelected(false);
                this.mPWUser.setSelected(false);
                return;
            case 3:
                this.mPWRock.setSelected(false);
                this.mPWClasses.setSelected(false);
                this.mPWJazz.setSelected(true);
                this.mPWFashion.setSelected(false);
                this.mPWFlat.setSelected(false);
                this.mPWUser.setSelected(false);
                return;
            case 4:
                this.mPWRock.setSelected(false);
                this.mPWClasses.setSelected(false);
                this.mPWJazz.setSelected(false);
                this.mPWFashion.setSelected(true);
                this.mPWFlat.setSelected(false);
                this.mPWUser.setSelected(false);
                return;
            case 5:
                this.mPWRock.setSelected(false);
                this.mPWClasses.setSelected(false);
                this.mPWJazz.setSelected(false);
                this.mPWFashion.setSelected(false);
                this.mPWFlat.setSelected(true);
                this.mPWUser.setSelected(false);
                return;
            case 6:
                this.mPWRock.setSelected(false);
                this.mPWClasses.setSelected(false);
                this.mPWJazz.setSelected(false);
                this.mPWFashion.setSelected(false);
                this.mPWFlat.setSelected(false);
                this.mPWUser.setSelected(true);
                return;
            default:
                Log.d("lzc", "eqValue=" + eqValue);
                return;
        }
    }

    public void diaplayEQColor() {
        switch (eqValue) {
            case 1:
                this.mPWRock.setTextColor(getResources().getColor(R.color.blue));
                return;
            case 2:
                this.mPWClasses.setTextColor(getResources().getColor(R.color.blue));
                return;
            case 3:
                this.mPWJazz.setTextColor(getResources().getColor(R.color.blue));
                return;
            case 4:
                this.mPWFashion.setTextColor(getResources().getColor(R.color.blue));
                return;
            case 5:
                this.mPWFlat.setTextColor(getResources().getColor(R.color.blue));
                return;
            case 6:
                this.mPWUser.setTextColor(getResources().getColor(R.color.blue));
                return;
            default:
                Log.d("lzc", "eqValue=" + eqValue);
                return;
        }
    }

    private void initSeekBarMax() {
        this.mLowVoiceVSB.setMax(100);
        this.mMiddleVoiceVSB.setMax(100);
        this.mHighVoiceVSB.setMax(100);
        this.mLowFVSB.setMax(100);
        this.mMiddleFVSB.setMax(100);
        this.mHighFVSB.setMax(100);
        this.mLoudSeekBar.setMax(100);
    }

    private void changeBottomSeekBar(int paramInt) {
        switch (paramInt) {
            case 1:
                this.mLowVoiceVSB.setProgress(66);
                this.mMiddleVoiceVSB.setProgress(46);
                this.mHighVoiceVSB.setProgress(66);
                this.mLowFVSB.setProgress(25);
                this.mMiddleFVSB.setProgress(25);
                this.mHighFVSB.setProgress(0);
                return;
            case 2:
                this.mLowVoiceVSB.setProgress(67);
                this.mMiddleVoiceVSB.setProgress(20);
                this.mHighVoiceVSB.setProgress(53);
                this.mLowFVSB.setProgress(25);
                this.mMiddleFVSB.setProgress(25);
                this.mHighFVSB.setProgress(0);
                return;
            case 3:
                this.mLowVoiceVSB.setProgress(26);
                this.mMiddleVoiceVSB.setProgress(60);
                this.mHighVoiceVSB.setProgress(40);
                this.mLowFVSB.setProgress(25);
                this.mMiddleFVSB.setProgress(25);
                this.mHighFVSB.setProgress(0);
                return;
            case 4:
                this.mLowVoiceVSB.setProgress(46);
                this.mMiddleVoiceVSB.setProgress(80);
                this.mHighVoiceVSB.setProgress(67);
                this.mLowFVSB.setProgress(25);
                this.mMiddleFVSB.setProgress(25);
                this.mHighFVSB.setProgress(0);
                return;
            case 5:
                this.mLowVoiceVSB.setProgress(50);
                this.mMiddleVoiceVSB.setProgress(50);
                this.mHighVoiceVSB.setProgress(50);
                this.mLowFVSB.setProgress(25);
                this.mMiddleFVSB.setProgress(25);
                this.mHighFVSB.setProgress(0);
                return;
            case 6:
                vbProgress1 = this.preferences.getInt("lowVoiceSBProgress", 50);
                vbProgress2 = this.preferences.getInt("middleVoiceSBProgress", 50);
                vbProgress3 = this.preferences.getInt("highVoiceSBProgress", 50);
                vbProgress4 = this.preferences.getInt("lowFreqSBProgress", 25);
                vbProgress5 = this.preferences.getInt("middleFreqSBProgress", 25);
                vbProgress6 = this.preferences.getInt("highFreqSBProgress", 0);
                this.mLowVoiceVSB.setProgress(vbProgress1);
                this.mMiddleVoiceVSB.setProgress(vbProgress2);
                this.mHighVoiceVSB.setProgress(vbProgress3);
                this.mLowFVSB.setProgress(vbProgress4);
                this.mMiddleFVSB.setProgress(vbProgress5);
                this.mHighFVSB.setProgress(vbProgress6);
                return;
            default:
                return;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_music_rock:
                changePlayEQ();
                return;
            case R.id.bt_music_balancer:
                Intent balancerIntent = new Intent();
                balancerIntent.setClass(this, Balancer.class);
                startActivity(balancerIntent);
                return;
            case R.id.bt_music_reset:
                eqValue = 6;
                Editor editor = this.preferences.edit();
                editor.putInt("lowVoiceSBProgress", 50);
                editor.putInt("middleVoiceSBProgress", 50);
                editor.putInt("highVoiceSBProgress", 50);
                editor.putInt("lowFreqSBProgress", 25);
                editor.putInt("middleFreqSBProgress", 25);
                editor.putInt("highFreqSBProgress", 0);
                editor.putInt("lowVoiceSBProgressValue", 7);
                editor.putInt("middleVoiceSBProgressValue", 7);
                editor.putInt("highVoiceSBProgressValue", 7);
                editor.putInt("lowFreqSBProgressValue", 1);
                editor.putInt("middleFreqSBProgressValue", 1);
                editor.putInt("highFreqSBProgressValue", 0);
                editor.putInt("eq_value", eqValue);
                editor.putInt("loud_bar", 50);
                editor.putInt("eq_loud", 0);
                editor.commit();
                this.mLoudValue.setText("0");
                this.mLoudSeekBar.setProgress(50);
                this.mSwitch.setChecked(false);
                try {
                    this.mEQService.set_volume(22, 0);
                    this.mEQService.set_volume(6, 7);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                displayEQ();
                displayEQValue();
                setEQValue();
                changeBottomSeekBar(eqValue);
                return;
            default:
                return;
        }
    }

    public void changePlayEQ() {
        eqValue++;
        if (eqValue > 6) {
            eqValue = 1;
        }
        Editor editor = this.preferences.edit();
        editor.putInt("eq_value", eqValue);
        editor.commit();
        displayEQ();
        displayEQValue();
        setEQValue();
        changeBottomSeekBar(eqValue);
    }

    public void jumpUserEQ() {
        eqValue = 6;
        Editor editor = this.preferences.edit();
        editor.putInt("eq_value", eqValue);
        editor.commit();
        displayEQ();
        displayEQValue();
        setEQValue();
        changeBottomSeekBar(eqValue);
    }

    public void onProgressChanged(VerticalSeekBar VerticalSeekBar, int progress, boolean fromUser) {
        vbProgress = progress;
        Log.i("royu", String.valueOf(vbProgress));
    }

    public void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar) {
    }

    public void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar) {
        VerticalSeekBar.setMax(100);
        VerticalSeekBar.setProgress(vbProgress);
        Log.i("EQ", "onStopTrackingTouch: " + String.valueOf(vbProgress));
        Editor editor;
        switch (VerticalSeekBar.getId()) {
            case R.id.bt_music_vseekbar1:
                vbProgressValue = (vbProgress * 14) / 100;
                Log.i("EQ", "R.id.bt_music_vseekbar1:" + String.valueOf(vbProgressValue));
                editor = this.preferences.edit();
                editor.putInt("lowVoiceSBProgress", vbProgress);
                editor.putInt("lowVoiceSBProgressValue", vbProgressValue);
                editor.commit();
                break;
            case R.id.bt_music_vseekbar2:
                vbProgressValue = (vbProgress * 14) / 100;
                Log.i("vbProgressValue=", String.valueOf(vbProgressValue));
                Editor editor1 = this.preferences.edit();
                editor1.putInt("middleVoiceSBProgress", vbProgress);
                editor1.putInt("middleVoiceSBProgressValue", vbProgressValue);
                editor1.commit();
                break;
            case R.id.bt_music_vseekbar3:
                vbProgressValue = (vbProgress * 14) / 100;
                Editor editor2 = this.preferences.edit();
                editor2.putInt("highVoiceSBProgress", vbProgress);
                editor2.putInt("highVoiceSBProgressValue", vbProgressValue);
                editor2.commit();
                break;
            case R.id.bt_music_vseekbar4:
                vbProgress = swapToFV(vbProgress);
                vbProgressValue = vbProgress / 25;
                if (vbProgressValue > 3) {
                    vbProgressValue = 3;
                }
                Editor editor3 = this.preferences.edit();
                editor3.putInt("lowFreqSBProgress", vbProgress);
                editor3.putInt("lowFreqSBProgressValue", vbProgressValue);
                editor3.commit();
                break;
            case R.id.bt_music_vseekbar5:
                vbProgress = swapToFV(vbProgress);
                vbProgressValue = vbProgress / 25;
                if (vbProgressValue > 3) {
                    vbProgressValue = 3;
                }
                Editor editor4 = this.preferences.edit();
                editor4.putInt("middleFreqSBProgress", vbProgress);
                editor4.putInt("middleFreqSBProgressValue", vbProgressValue);
                editor4.commit();
                break;
            case R.id.bt_music_vseekbar6:
                vbProgress = swapToFV(vbProgress);
                vbProgressValue = vbProgress / 25;
                if (vbProgressValue > 3) {
                    vbProgressValue = 3;
                }
                Editor editor5 = this.preferences.edit();
                editor5.putInt("highFreqSBProgress", vbProgress);
                editor5.putInt("highFreqSBProgressValue", vbProgressValue);
                editor5.commit();
                break;
            case R.id.bt_loud_vseekbar:
                vbProgressValue = ((vbProgress * 14) / 100) - 7;
                editor = this.preferences.edit();
                this.mLoudValue.setText(String.valueOf(vbProgressValue));
                try {
                    this.mEQService.set_volume(6, vbProgressValue + 7);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                editor.putInt("loud_bar", vbProgress);
                editor.commit();
                Log.i("EQ", "R.id.bt_loud_vseekbar" + vbProgressValue);
                return;
        }
        jumpUserEQ();
    }

    private int swapToFV(int value) {
        if (value <= 25) {
            return 0;
        }
        if (value > 25 && value <= 50) {
            return 33;
        }
        if (value <= 50 || value > 75) {
            return 100;
        }
        return 66;
    }
}
