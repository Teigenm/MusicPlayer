package com.teigenm.coursemanager.ui;



import android.content.ContentValues;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teigenm.coursemanager.R;
import com.teigenm.coursemanager.database.AppDatabase;
import com.teigenm.coursemanager.database.dao.StudentDao;
import com.teigenm.coursemanager.model.StudentEntity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText text_sid;
    private EditText text_password;
    private EditText text_repassword;
    private EditText text_name;
    private AppDatabase appDatabase;
    private StudentDao studentDao;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private List list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button_register=(Button)findViewById(R.id.button_register_confirm);
        Button button_exit=(Button)findViewById(R.id.button_register_exit);
        text_sid=(EditText)findViewById(R.id.text_register_sid);
        text_password=(EditText)findViewById(R.id.text_register_password);
        text_repassword=(EditText)findViewById(R.id.text_register_repassword);
        text_name=(EditText)findViewById(R.id.text_register_name);
        appDatabase=AppDatabase.getInstance();
        studentDao=appDatabase.getStudentDao();

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values=new ContentValues();
                String sid=text_sid.getText().toString().trim();
                String password=text_password.getText().toString().trim();
                String repassword=text_repassword.getText().toString().trim();
                String name=text_name.getText().toString().trim();
                Pattern pattern = Pattern.compile("[0-9]*");
                Matcher isNum = pattern.matcher(sid);
                Log.d("字符",sid+" "+password+" "+repassword+" "+name);
                if("".equals(sid)){
                    Toast.makeText(RegisterActivity.this,"请输入学号！",Toast.LENGTH_SHORT).show();

                }else if("".equals(password)||"".equals(repassword)){
                    Toast.makeText(RegisterActivity.this,"请输入密码！",Toast.LENGTH_SHORT).show();

                }else if("".equals(name)) {
                    Toast.makeText(RegisterActivity.this, "请输入姓名！", Toast.LENGTH_SHORT).show();
                }else if(sid.length()!=12||!isNum.matches()){
                    Toast.makeText(RegisterActivity.this,"学号由12位数字组成！",Toast.LENGTH_SHORT).show();
                }else if(!password.equals(repassword)){
                    Toast.makeText(RegisterActivity.this,"两次输入密码不匹配！",Toast.LENGTH_SHORT).show();
                }else{
                    StudentEntity student=new StudentEntity(sid,password,name);
                    mDisposable.add(Completable.fromAction(() -> {studentDao.add(student);})
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                Toast.makeText(getApplicationContext(),"注册成功！",Toast.LENGTH_SHORT).show();
                                finish();
                            }, throwable -> {
                                Toast.makeText(getApplicationContext(),"注册失败,该学号已存在！",Toast.LENGTH_SHORT).show();
                            }));
                    }
            }
        });
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
