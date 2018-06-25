package cs2c.EQ;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.cs2c.IEQService;
import android.os.RemoteException;
import android.util.Log;

public class EQBroadcastReceiver extends BroadcastReceiver {
    private static int balanceDivisorX = 1;
    private static int balanceDivisorY = 1;
    private static int increaseValue = 0;
    private int backLight = 0;
    private int coordinateX;
    private int coordinateY;
    private IEQService mEQService;
    private int mainVolume = 0;
    private SharedPreferences preferences;
    private int vbProgressVariable1 = 0;
    private int vbProgressVariable2 = 0;
    private int vbProgressVariable3 = 0;
    private int vbProgressVariable4 = 0;
    private int vbProgressVariable5 = 0;
    private int vbProgressVariable6 = 0;

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        this.preferences = context.getSharedPreferences("musicEQ", 2);
//        this.mEQService = (IEQService) context.getSystemService("eq");
        this.mEQService = (cs2c.EQ.IEQService) new EQService();

        if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            int diffX;
            int diffY;
            this.vbProgressVariable1 = this.preferences.getInt("lowFreqSBProgressValue", 1);
            this.vbProgressVariable2 = this.preferences.getInt("middleFreqSBProgressValue", 1);
            this.vbProgressVariable3 = this.preferences.getInt("highFreqSBProgressValue", 0);
            this.vbProgressVariable4 = this.preferences.getInt("lowVoiceSBProgressValue", 7);
            this.vbProgressVariable5 = this.preferences.getInt("middleVoiceSBProgressValue", 7);
            this.vbProgressVariable6 = this.preferences.getInt("highVoiceSBProgressValue", 7);
            initLimitValue();
            if (EQActivity.width > 800) {
                this.coordinateX = this.preferences.getInt("CoordinateX", 155);
                this.coordinateY = this.preferences.getInt("CoordinateY", 180);
                System.out.println("coordinateX  " + this.coordinateX);
                System.out.println("coordinateY  " + this.coordinateY);
                diffX = ((this.coordinateX * 10) - 100) / balanceDivisorX;
                diffY = (345 - this.coordinateY) / balanceDivisorY;
            } else {
                this.coordinateX = this.preferences.getInt("CoordinateX", 155);
                this.coordinateY = this.preferences.getInt("CoordinateY", 180);
                System.out.println("coordinateX  " + this.coordinateX);
                System.out.println("coordinateY  " + this.coordinateY);
                diffX = ((this.coordinateX * 10) - 100) / balanceDivisorX;
                diffY = (260 - this.coordinateY) / balanceDivisorY;
            }
            setXY(diffX, diffY);
            try {
                this.mEQService.setSound(3, this.vbProgressVariable6);
                this.mEQService.setSound(2, this.vbProgressVariable5);
                this.mEQService.setSound(1, this.vbProgressVariable4);
                this.mEQService.setSound(13, this.vbProgressVariable3);
                this.mEQService.setSound(12, this.vbProgressVariable2);
                this.mEQService.setSound(11, this.vbProgressVariable1);
                increaseValue = this.preferences.getInt("increase_value_key", 2);
                System.out.println(increaseValue);
                System.out.println("preferences.getAll().size()  " + this.preferences.getAll().size());
                this.mEQService.setSound(32, increaseValue);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (action.equals("cn.com.cs2c.android.vehicle.action.EQ_KEY")) {
            Intent it = new Intent();
            it.setClass(context, EQActivity.class);
            it.addFlags(268435456);
            context.startActivity(it);
            Log.d("lzc", "receiver EQ key");
        }
    }

    private void initLimitValue() {
        if (EQActivity.width > 800) {
            balanceDivisorX = 116;
            balanceDivisorY = 13;
            return;
        }
        balanceDivisorX = 88;
        balanceDivisorY = 10;
    }

    public int checkItem(int value) {
        if (value > 24) {
            return 24;
        }
        if (value < 0) {
            return 0;
        }
        return value;
    }

    public void setXY(int x, int y) {
        try {
            this.mEQService.setSurround(checkItem(x), 24 - checkItem(y));
            System.out.println("-------------------------------------------");
            System.out.println("--------------------------------------------");
            System.out.println("--------------------------------------------");
            System.out.println("--------------------------------------------");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
