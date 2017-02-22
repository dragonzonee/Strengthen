package dragonzonee.com.strengthen.SecondPart_Locate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kongl_000 on 2016/12/5.
 */

public class Database2 extends SQLiteOpenHelper{

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Database2(Context context, String name){
        super(context, name, null, 1, null);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String s="create table if not exists "+getUsername();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
