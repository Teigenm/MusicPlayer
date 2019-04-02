package com.teigenm.coursemanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import android.content.ContentValues;
import android.os.Bundle;
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

public class ChangePassActivity extends AppCompatActivity {

    private EditText text_sid;
    private EditText text_oldpassword;
    private EditText text_newpassword;
    private EditText text_renewpassword;
    private AppDatabase appDatabase;
    private StudentDao studentDao;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private List list;
    private  int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_change_pass);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button_change=(Button)findViewById(R.id.button_change_confirm);
        Button button_exit=(Button)findViewById(R.id.button_change_exit);
        text_sid=(EditText)findViewById(R.id.text_change_sid);
        text_oldpassword=(EditText)findViewById(R.id.text_change_oldpassword);
        text_newpassword=(EditText)findViewById(R.id.text_change_newpassword);
        text_renewpassword=(EditText)findViewById(R.id.text_change_renewpassword);
        appDatabase=AppDatabase.getInstance();
        studentDao=appDatabase.getStudentDao();

        button_change.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ContentValues values=new ContentValues();
                String sid=text_sid.getText().toString().trim();
                String oldpassword=text_oldpassword.getText().toString().trim();
                String newpassword=text_newpassword.getText().toString().trim();
                String renewpassword=text_renewpassword.getText().toString().trim();
                Pattern pattern = Pattern.compile("[0-9]*");
                Matcher isNum = pattern.matcher(sid);
               // Log.d("字符",sid+" "+oldpassword+" "+newpassword+" "+renewpassword);
                if("".equals(sid)){
                    Toast.makeText(ChangePassActivity.this,"请输入学号！",Toast.LENGTH_SHORT).show();

                }else if("".equals(oldpassword)){
                    Toast.makeText(ChangePassActivity.this,"请输入旧密码！",Toast.LENGTH_SHORT).show();

                }else if("".equals(newpassword)) {
                    Toast.makeText(ChangePassActivity.this, "请输入新密码！", Toast.LENGTH_SHORT).show();
                }else if(sid.length()!=12||!isNum.matches()){
                    Toast.makeText(ChangePassActivity.this,"学号由12位数字组成！",Toast.LENGTH_SHORT).show();
                }else if(!newpassword.equals(renewpassword)){
                    Toast.makeText(ChangePassActivity.this,"两次输入密码不匹配！",Toast.LENGTH_SHORT).show();
                }else{
                    flag=0;
                    StudentEntity student=new StudentEntity(sid,newpassword);
                    mDisposable.add(Completable.fromAction(() -> {
                            if(studentDao.login_one(sid,oldpassword)>=1){
                                studentDao.update(student);
                            }else{
                                flag=1;
                            }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                if(flag==1){
                                    Toast.makeText(getApplicationContext(),"修改密码失败,原账号或密码错误！",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(),"修改密码成功,返回登陆界面进行登陆！",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }, throwable -> {
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
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
