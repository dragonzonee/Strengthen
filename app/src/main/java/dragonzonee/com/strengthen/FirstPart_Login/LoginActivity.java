package dragonzonee.com.strengthen.FirstPart_Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dragonzonee.com.strengthen.R;

public class LoginActivity extends AppCompatActivity {

        private String user_to_password;
        private boolean isture;
        private boolean pass_isture;
        private EditText mET_user;
        private EditText mET_password;
        private Button mBT_signup;
        private Button mBT_login;
        private String db_name="db";
        private ImageView mIV_username_right;
        private ImageView mIV_username_wrong;
        private ImageView mIV_password_right;
        private ImageView mIV_password_wrong;
        private CheckBox mCB_savecodes;
        private SharedPreferences mPreferences;
        private final String msecondActivity="dragonzonee.com.store.main.SecondActivity";
        private final String mcontentActivity="dragonzonee.com.strengthen.SecondPart_Locate.ContentActivity";
        private final String msignupActivity="dragonzonee.com.strengthen.SignUpActivity";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            //requestExternalPermission();
            //定义layout布局文件
            mBT_signup=(Button)findViewById(R.id.bt_signup);    mBT_login=(Button)findViewById(R.id.bt_login);
            mET_user= (EditText) findViewById(R.id.et_user);    mET_password= (EditText) findViewById(R.id.et_password);
            mIV_username_right=(ImageView)findViewById(R.id.iv_username_right);    mIV_username_wrong=(ImageView)findViewById(R.id.iv_username_wrong);  mIV_password_right=(ImageView)findViewById(R.id.iv_password_right); mIV_password_wrong=(ImageView)findViewById(R.id.iv_password_wrong);
            mCB_savecodes=(CheckBox)findViewById(R.id.cb_savecodes);
            //获取上次登录的用户信息
            mPreferences=this.getSharedPreferences("userinfo",MODE_PRIVATE);
            //设置密码对话框的hint风格与用户注册的一致。
            setDefaultPasswordTheme(mET_password);
            //检测文本框钟用户的键入信息
            mET_user.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    VertifyUsername();
                }
            });
            //为两个按钮添加点击响应事件
            ButtonOnclickListener(mBT_signup,msignupActivity);
            ButtonOnclickListener(mBT_login,mcontentActivity);
            //检查用户是否已经选择了保存密码的选项
            isexist();

        }

        //set the default style of the password edittext
        public void setDefaultPasswordTheme(EditText editText){
            editText.setTypeface(mET_user.getTypeface());
        }


        public void ButtonOnclickListener(Button button, final String activity){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentChange(activity);
                /*Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);*/
                }
            });
        }

        public void intentChange(String activity){
            switch (activity) {
                case mcontentActivity:

                    System.out.println("interchange function is running: " + activity);
                    if(isEmpty(mET_user,mET_password)){
                        Toast.makeText(getApplicationContext(),"请填写用户名或密码",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(isture){
                            VertifyPassword();
                            if(pass_isture){
                                checkIsSaveCodes();
                                mIV_password_right.setVisibility(View.VISIBLE);
                                mIV_password_wrong.setVisibility(View.INVISIBLE);
                                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent();
                                intent2.putExtra("username",mET_user.getText().toString());
                                intent2.setClassName(LoginActivity.this,mcontentActivity);
                                startActivity(intent2);
                                finish();}
                            else{
                                mIV_password_right.setVisibility(View.INVISIBLE);
                                mIV_password_wrong.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this,"密码不正确",Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(LoginActivity.this,"用户名不存在，点击注册按钮进入注册界面",Toast.LENGTH_SHORT).show();
                        }

                    }
                    break;
                case msignupActivity:
                    Intent intent1 = new Intent();
                    System.out.println("interchange function is running: " + activity);
                    intent1.setClass(LoginActivity.this,SignupActivity.class);
                    startActivity(intent1);
                    //finish();
                    break;
                default:
                    System.out.println("none change activity function is running");
                    break;

            }
        }


        public boolean isEmpty(EditText username,EditText password){
            System.out.println(username.getText().toString()+"password-->"+password.getText().toString());
            if(username.getText().toString().length()==0||password.getText().toString().length()==0)
                return true;
            else
                return false;
        }




        public void VertifyUsername(){
            isture=false;
            Set<String> names = new HashSet<String>();
            Database databaseSetting = new Database(LoginActivity.this,"test_db");
            SQLiteDatabase db =databaseSetting.getReadableDatabase();
            Cursor cursor = db.rawQuery("select username from user",null);
            //db.query("user", new String[]{"username","password"}, "id=?", new String[]{"1"}, null, null, null, null);
            //利用游标遍历所有数据对象
            while(cursor.moveToNext()){
                names.add(cursor.getString(cursor.getColumnIndex("username")));
            }
            cursor.close();

            Iterator<String> it=names.iterator();
            while (it.hasNext()){
                if(it.next().equals(mET_user.getText().toString())){
                    isture=true;
                    user_to_password=mET_user.getText().toString();
                    break;
                }else{
                    continue;
                }
            }
            if(isture){
                mIV_username_right.setVisibility(View.VISIBLE);
                mIV_username_wrong.setVisibility(View.INVISIBLE);
            }else{
                mIV_username_right.setVisibility(View.INVISIBLE);
                mIV_username_wrong.setVisibility(View.VISIBLE);
            }

        }



        public void VertifyPassword(){
            pass_isture=false;
            Set<String> names = new HashSet<String>();
            Database databaseSetting = new Database(LoginActivity.this,"test_db");
            SQLiteDatabase db =databaseSetting.getReadableDatabase();
            Cursor cursor = db.rawQuery("select password from user where username=?",new String[]{user_to_password});
            //db.query("user", new String[]{"username","password"}, "id=?", new String[]{"1"}, null, null, null, null);
            //利用游标遍历所有数据对象
            while(cursor.moveToNext()){
                names.add(cursor.getString(cursor.getColumnIndex("password")));
            }
            cursor.close();

            Iterator<String> it=names.iterator();
            while (it.hasNext()){
                if(it.next().equals(mET_password.getText().toString())){
                    pass_isture=true;
                    break;
                }else{
                    continue;
                }
            }
/*        if(pass_isture){
            mIV_password_right.setVisibility(View.VISIBLE);
            mIV_password_wrong.setVisibility(View.INVISIBLE);
        }else{
            mIV_password_right.setVisibility(View.INVISIBLE);
            mIV_password_wrong.setVisibility(View.VISIBLE);
        }*/
        }


        public void checkIsSaveCodes(){
            System.out.println("checkIsSaveCodes is running");
            if(mCB_savecodes.isChecked()){
                SharedPreferences.Editor editor=mPreferences.edit();
                editor.clear();
                editor.commit();
                System.out.println("checkIsSaveCodes is clear all");
                editor.putString("username",mET_user.getText().toString());
                editor.putString("password",mET_password.getText().toString());
                editor.putBoolean("isexist",true);
                editor.putBoolean("ischeck",true);
                editor.commit();
            }else{
                System.out.println("checkIsSaveCodes() not using ischecked");
                SharedPreferences.Editor editor=mPreferences.edit();
                editor.clear();
                editor.commit();
                System.out.println(mPreferences.getBoolean("ischeck",false)+"@@@@@@@@@");
            }
        }


        public void isexist(){
            System.out.println(mPreferences.getBoolean("ischeck",false)+"!!!!!!!!!!!!");
            if(mPreferences.getBoolean("ischeck",false)){
                mCB_savecodes.setChecked(true);
                System.out.println("getting ischeck successfully,   ans is true");
            }else{
                mCB_savecodes.setChecked(false);
                System.out.println("getting ischeck successfully,   ans is false");
            }
            System.out.println("isexist is running");
            if(mCB_savecodes.isChecked()&&mPreferences.getBoolean("isexist",false)){
                System.out.println("isexist is matched");
                mET_user.setText(mPreferences.getString("username",null));
                mET_password.setText(mPreferences.getString("password",null));
            }
        }


   /* private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE_CODE = 1;
    private void requestExternalPermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission_group.LOCATION}, REQUEST_PERMISSION_EXTERNAL_STORAGE_CODE);
    }*/



    }
