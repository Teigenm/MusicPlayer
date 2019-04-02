package com.teigenm.coursemanager.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.teigenm.coursemanager.R;
import com.teigenm.coursemanager.database.AppDatabase;
import com.teigenm.coursemanager.database.dao.StudentDao;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private EditText text_username;
    private EditText text_password;
    private CheckBox checkboxRemeber;
    private AppDatabase database ;
    private AppDatabase appDatabase;
    private StudentDao studentDao;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private Context context = LoginActivity.this;
    private SharedPreferences.Editor editor;
    private static LoginActivity loginActivity = null;
    public static LoginActivity getIntstance(){
        return loginActivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        text_username=(EditText)findViewById(R.id.text_login_username);
        text_password=(EditText)findViewById(R.id.text_login_password);
        checkboxRemeber=(CheckBox)findViewById(R.id.checkbox_login_remeber);
        Button button_login=(Button)findViewById(R.id.button_login_confirm);
        Button button_register=(Button)findViewById(R.id.button_login_register);
        Button button_change_pass=(Button)findViewById(R.id.button_login_change_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        loginActivity=LoginActivity.this;
        database=AppDatabase.getInstance();
        sp=PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        String check=sp.getString("check","0");
        appDatabase=AppDatabase.getInstance();
        studentDao=appDatabase.getStudentDao();


        if(check.equals("1")){
            text_username.setText(sp.getString("username",""));
            text_password.setText(sp.getString("password",""));
            checkboxRemeber.setChecked(true);
        }
        button_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                editor=sp.edit();
                String username=text_username.getText().toString().trim();
                String password=text_password.getText().toString().trim();

                Log.d("输出",username+" "+password);

                if("".equals(username)){
                    Toast.makeText(LoginActivity.this,"请输入学号！",Toast.LENGTH_SHORT).show();

                }else if("".equals(password)){
                    Toast.makeText(LoginActivity.this,"请输入密码！",Toast.LENGTH_SHORT).show();

                }else{
                    mDisposable.add(
                            studentDao.login(username,password)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(subject -> {
                                        if(subject>=1){
                                            Toast.makeText(getApplicationContext(),"登陆成功！",Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(context,HomeActivity.class);
                                            Bundle bundle=new Bundle();
                                            bundle.putString("sid",username);
                                            bundle.putString("password",password);
                                            bundle.putString("isRemeber",checkboxRemeber.isChecked()?"1":"0");
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(getApplicationContext(),"登陆失败，账号或密码错误！",Toast.LENGTH_SHORT).show();
                                        }
                                    }, throwable -> {
                                        //Toast.makeText(getApplicationContext(),"登陆失败，账号或密码错误！",Toast.LENGTH_SHORT).show();
                                    }));
                }
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        button_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,ChangePassActivity.class);
                startActivity(intent);
            }
        });
    }
    public void editorCommit(String username,String password,String isRemeber){
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putString("check",isRemeber);
        editor.apply();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
