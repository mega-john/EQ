package cs2c.EQ;

import android.os.RemoteException;

public interface IEQService {
    void setSound(int var1, int var2) throws RemoteException;

    void setSurround(int var1, int var2)  throws RemoteException;

    void set_volume(int var1, int var2)  throws RemoteException;
}
