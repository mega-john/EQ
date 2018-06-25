package cs2c.EQ;


import android.util.Log;

public class EQService implements IEQService {
    @Override
    public void setSound(int i, int i1)  {
        Log.d("eq", String.format("setSound(%d, %d)",i,i1));
    }

    @Override
    public void setSurround(int i, int i1){
        Log.d("eq", String.format("setSurround(%d, %d)",i,i1));
    }

    @Override
    public void set_volume(int i, int i1)  {
        Log.d("eq", String.format("set_volume(%d, %d)",i,i1));
    }
}
