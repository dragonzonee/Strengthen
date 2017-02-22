package dragonzonee.com.strengthen.FirstPart_Login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dragonzonee.com.strengthen.SecondPart_Locate.ContentActivity;
import dragonzonee.com.strengthen.R;

public class SignupActivity extends AppCompatActivity {

        private EditText mET_signup_user;
        private EditText mET_signup_password;
        private Button mBT_signup_login;
        private boolean ISTURE;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);
            mET_signup_user=(EditText)findViewById(R.id.et_signup_user);
            mET_signup_password=(EditText)findViewById(R.id.et_signup_password);
            mBT_signup_login=(Button)findViewById(R.id.bt_signup_login);
            setDefaultPasswordTheme();
            mBT_signup_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RegistUser();
                    if(isEmpty(mET_signup_user,mET_signup_password))
                    {
                        Toast.makeText(SignupActivity.this,"Username or Password is null",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(ISTURE){
                            Toast.makeText(SignupActivity.this,"This username has been registed",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Database databaseSetting = new Database(SignupActivity.this, "test_db");
                            SQLiteDatabase db1 = databaseSetting.getReadableDatabase();
                            ContentValues values = new ContentValues();
                            //像ContentValues中存放数据
                            values.put("username", mET_signup_user.getText().toString());
                            values.put("password", mET_signup_password.getText().toString());
                            db1.insert("user", null, values);

                            Intent intent = new Intent();
                            intent.putExtra("username",mET_signup_user.getText().toString());
                            intent.setClass(SignupActivity.this,ContentActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });
        }


        //set the default style of the password edittext
        public void setDefaultPasswordTheme(){
            mET_signup_password.setTypeface(mET_signup_user.getTypeface());
        }


        public void RegistUser() {
            ISTURE=false;
            Set<String> namess = new HashSet<String>();
            Database databaseSetting = new Database(SignupActivity.this, "test_db");
            SQLiteDatabase db = databaseSetting.getReadableDatabase();
            Cursor cursor = db.rawQuery("select username from user",null);
            while(cursor.moveToNext()){
                namess.add(cursor.getString(cursor.getColumnIndex("username")));
            }
            cursor.close();
            Iterator<String> iterator=namess.iterator();
            String tst = mET_signup_user.getText().toString();
            while(iterator.hasNext()){
                // System.out.println("NEXT: "+iterator.next()+" ETIEW: "+tst);
                if(iterator.next().equals(mET_signup_user.getText().toString())){
                    ISTURE=true;
                    break;
                }else{
                    continue;
                }
            }

        }

        public boolean isEmpty(EditText username,EditText password){
            System.out.println(username.getText().toString()+"password-->"+password.getText().toString());
            if(username.getText().toString().length()==0||password.getText().toString().length()==0)
                return true;
            else
                return false;
        }
}

