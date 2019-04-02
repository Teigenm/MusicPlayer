package com.teigenm.coursemanager.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.teigenm.coursemanager.R;
import com.teigenm.coursemanager.database.AppDatabase;
import com.teigenm.coursemanager.database.dao.CourseDao;
import com.teigenm.coursemanager.model.CourseEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertActivity extends AppCompatActivity implements  View.OnClickListener{

    private EditText text_cteacher;
    private EditText text_cname;
    private EditText text_cno;
    private Spinner spinner_week_start;
    private Spinner spinner_week_end;
    private Spinner spinner_section_start;
    private Spinner spinner_section_end;
    private CheckBox checkBox_cremind_no;
    private CheckBox checkBox_cremind_yes;
    private List<String> weekArray= new ArrayList<String>();
    private List<String> dayArray= new ArrayList<String>();
    public static String sid;
    public static int cweekDay;
    private AppDatabase appDatabase;
    private CourseDao courseDao;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private Button button_confrim;
    private Button button_exit;
    private CourseEntity courseEntity;
    private long idMax=0;
    private static InsertActivity insertActivity=null;
    public static InsertActivity getInstance(){
        return insertActivity;
    }
    public static void startActivityForResult(Activity context, int requestCode,String sid,int cweekDay) {
        InsertActivity.sid=sid;
        InsertActivity.cweekDay=cweekDay;
        Intent intent = new Intent();
        intent.setClass(context, InsertActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_insert);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        text_cteacher=(EditText)findViewById(R.id.text_insert_cteacher);
        text_cname=(EditText)findViewById(R.id.text_insert_cname);
        text_cno=(EditText)findViewById(R.id.text_insert_cno);
        spinner_week_start=(Spinner)findViewById(R.id.spinner_insert_week_start);
        spinner_week_end=(Spinner)findViewById(R.id.spinner_insert_week_end);
        spinner_section_start=(Spinner)findViewById(R.id.spinner_insert_section_start);
        spinner_section_end=(Spinner)findViewById(R.id.spinner_insert_section_end);
        checkBox_cremind_yes=(CheckBox)findViewById(R.id.checkbox_insert_cremind_yes);
        checkBox_cremind_no=(CheckBox)findViewById(R.id.checkbox_insert_cremind_no);
        button_confrim=(Button)findViewById(R.id.button_insert_confrim);
        button_exit=(Button)findViewById(R.id.button_insert_exit);
        insertActivity=InsertActivity.this;
        appDatabase=AppDatabase.getInstance();
        courseDao=appDatabase.getCourseDao();

        init();
        ArrayAdapter<String> arrayWeekAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,weekArray);
        spinner_week_start.setAdapter(arrayWeekAdapter);
        spinner_week_end.setAdapter(arrayWeekAdapter);

        ArrayAdapter<String> arrayDayAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,dayArray);
        spinner_section_start.setAdapter(arrayDayAdapter);
        spinner_section_end.setAdapter(arrayDayAdapter);

        checkBox_cremind_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    checkBox_cremind_no.setChecked(false);
                }else{
                    checkBox_cremind_no.setChecked(true);
                }
            }
        });
        checkBox_cremind_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    checkBox_cremind_yes.setChecked(false);
                }else{
                    checkBox_cremind_yes.setChecked(true);
                }
            }
        });
        button_confrim.setOnClickListener(InsertActivity.this);
        button_exit.setOnClickListener(InsertActivity.this);
    }

    public void init(){
        Resources resources =getResources();
        weekArray = Arrays.asList(resources.getStringArray(R.array.week_array));
        dayArray = Arrays.asList(resources.getStringArray(R.array.day_array));
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button_insert_confrim:
                String cno = text_cno.getText().toString().trim();
                String cteacher = text_cteacher.getText().toString().trim();
                String cname = text_cname.getText().toString().trim();
                int cweekStart = Integer.parseInt(spinner_week_start.getSelectedItem().toString());
                int cweekEnd = Integer.parseInt(spinner_week_end.getSelectedItem().toString());
                int csectionStart = Integer.parseInt(spinner_section_start.getSelectedItem().toString());
                int csectionEnd = Integer.parseInt(spinner_section_end.getSelectedItem().toString());
                int check = checkBox_cremind_yes.isChecked()?1:0;
                if("".equals(cno)){
                    Toast.makeText(InsertActivity.this,"请输入课程编号！",Toast.LENGTH_SHORT).show();
                }else if("".equals(cname)){
                    Toast.makeText(InsertActivity.this,"请输入课程名！",Toast.LENGTH_SHORT).show();
                }else if("".equals(cteacher)){
                    Toast.makeText(InsertActivity.this,"请输入老师姓名！",Toast.LENGTH_SHORT).show();
                }else if(cweekStart>cweekEnd||csectionStart>csectionEnd){
                    Toast.makeText(InsertActivity.this,"不规范的选择！",Toast.LENGTH_SHORT).show();
                }else {
                    courseEntity = new CourseEntity(sid,cno,cname,cteacher,cweekStart,cweekEnd
                            ,csectionStart,csectionEnd,check,cweekDay);
                    if(courseEntity.getCremind()==1){
                        showTimeDialog();
                    }else{
                        insert();
                    }
                }
                break;
            case R.id.button_insert_exit:
                finish();
                break;
            default:break;
        }
    }
    private void showTimeDialog(){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(InsertActivity.this);
        normalDialog.setIcon(R.drawable.logo);
        normalDialog.setTitle("课程闹钟");
        normalDialog.setMessage("已设置为提醒，是否设置闹钟,若选否,则不再设置提醒?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        courseEntity.setCremind(1);
                        insert();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        courseEntity.setCremind(0);
                        insert();
                    }
                });
        normalDialog.show();
    }
    public void insert(){
        mDisposable.add(
                courseDao.getCount()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subject -> {
                            idMax=subject;
                        }, throwable -> {

                        }));
        mDisposable.add(Completable.fromAction(() -> {courseDao.add(courseEntity);})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if(courseEntity.getCremind()==1){
                        Intent intent = new Intent(insertActivity,ClockActivity.class);
                        Bundle bundle = new Bundle();
                        courseEntity.setId(idMax);
                        bundle.putSerializable("courseEntity", (Serializable) courseEntity);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(),"课程添加成功！",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(),"课程添加成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, throwable -> {
                    Toast.makeText(getApplicationContext(),"课程添加失败！",Toast.LENGTH_SHORT).show();
                }));
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
