package dragonzonee.com.strengthen.FirstPart_Login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Created by kongl_000 on 2016/12/4.
 */
public class Database extends SQLiteOpenHelper {


        private boolean INITIAL=false;
        private static final int VERSION=1;
        public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context,name,null,version);
        }


        public Database(Context context, String name){
            super(context,name,null,VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            System.out.println("OnCreate method is running");
            String sql="create table if not exists user(user_id integer primary key autoincrement,username varchar(30),password varchar(30), gender char(1),age smallint(3))";
            db.execSQL(sql);
            String sql2="insert into user(user_id,username,password) values(1,'administrator','admin')";
            db.execSQL(sql2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            System.out.println("OnUpgrade method is running");
        }
    }
