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
import android.view.Menu;
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

public class CourseViewActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText text_cteacher;
    private EditText text_cname;
    private EditText text_cno;
    private Spinner spinner_week_start;
    private Spinner spinner_week_end;
    private Spinner spinner_section_start;
    private Spinner spinner_section_end;
    private CheckBox checkBox_cremind_no;
    private CheckBox checkBox_cremind_yes;
    private Button button_confrim;
    private List<String> weekArray= new ArrayList<String>();
    private List<String> dayArray= new ArrayList<String>();
    private String sid;
    private int cweekDay;
    private AppDatabase appDatabase;
    private CourseDao courseDao;
    private CourseEntity courseEntity;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    public static long idStatic=0;
    private  CourseViewActivity courseViewActivity=CourseViewActivity.this;

    public static void startActivityForResult(Activity context, int requestCode,long id) {
        CourseViewActivity.idStatic=id;
        Intent intent = new Intent();
        intent.setClass(context, CourseViewActivity.class);
        context.startActivityForResult(intent, requestCode);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_course);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        text_cteacher=(EditText)findViewById(R.id.text_course_cteacher);
        text_cname=(EditText)findViewById(R.id.text_course_cname);
        text_cno=(EditText)findViewById(R.id.text_course_cno);
        spinner_week_start=(Spinner)findViewById(R.id.spinner_course_week_start);
        spinner_week_end=(Spinner)findViewById(R.id.spinner_course_week_end);
        spinner_section_start=(Spinner)findViewById(R.id.spinner_course_section_start);
        spinner_section_end=(Spinner)findViewById(R.id.spinner_course_section_end);
        checkBox_cremind_yes=(CheckBox)findViewById(R.id.checkbox_course_cremind_yes);
        checkBox_cremind_no=(CheckBox)findViewById(R.id.checkbox_course_cremind_no);
        button_confrim=(Button)findViewById(R.id.button_course_confirm);
        appDatabase=AppDatabase.getInstance();
        courseDao = appDatabase.getCourseDao();


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
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
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
        button_confrim.setOnClickListener(CourseViewActivity.this);
        mDisposable.add(
                courseDao.getOne(idStatic)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subject -> {
                            text_cteacher.setText(subject.getCteacher());
                            text_cname.setText(subject.getCname());
                            text_cno.setText(subject.getCno());
                            spinner_week_start.setSelection(subject.getCweekStart()-1,true);
                            spinner_week_end.setSelection(subject.getCweekEnd()-1,true);
                            spinner_section_start.setSelection(subject.getCsectionStart()-1,true);
                            spinner_section_end.setSelection(subject.getCsectionEnd()-1,true);
                            if(subject.getCremind()==1){
                                checkBox_cremind_yes.setChecked(true);
                            }else{
                                checkBox_cremind_no.setChecked(true);
                            }
                            sid=subject.getSid();
                            cweekDay=subject.getCweekDay();
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(),"加载数据出错",Toast.LENGTH_SHORT).show();
                        }));
    }

    public void init(){
        Resources resources =getResources();
        weekArray = Arrays.asList(resources.getStringArray(R.array.week_array));
        dayArray = Arrays.asList(resources.getStringArray(R.array.day_array));
    }
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button_course_confirm:
                String cno = text_cno.getText().toString().trim();
                String cteacher = text_cteacher.getText().toString().trim();
                String cname = text_cname.getText().toString().trim();
                int cweekStart = Integer.parseInt(spinner_week_start.getSelectedItem().toString());
                int cweekEnd = Integer.parseInt(spinner_week_end.getSelectedItem().toString());
                int csectionStart = Integer.parseInt(spinner_section_start.getSelectedItem().toString());
                int csectionEnd = Integer.parseInt(spinner_section_end.getSelectedItem().toString());
                int check = checkBox_cremind_yes.isChecked()?1:0;
                if("".equals(cno)){
                    Toast.makeText(CourseViewActivity.this,"请输入课程编号！",Toast.LENGTH_SHORT).show();
                }else if("".equals(cname)){
                    Toast.makeText(CourseViewActivity.this,"请输入课程名！",Toast.LENGTH_SHORT).show();
                }else if("".equals(cteacher)){
                    Toast.makeText(CourseViewActivity.this,"请输入老师姓名！",Toast.LENGTH_SHORT).show();
                }else if(cweekStart>cweekEnd||csectionStart>csectionEnd){
                    Toast.makeText(CourseViewActivity.this,"不规范的选择！",Toast.LENGTH_SHORT).show();
                }else {
                    courseEntity = new CourseEntity(idStatic,sid,cno,cname,cteacher,cweekStart,cweekEnd
                            ,csectionStart,csectionEnd,check,cweekDay);
                    if(courseEntity.getCremind()==1){
                        showTimeDialog();
                    }else{
                        update();
                    }
                }
                break;
            default:break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.action_course_view_diary:
                showNormalDialog();
                break;
            case R.id.action_course_view_delete:
                showDelDialog();
                break;
            case android.R.id.home:
                finish();
                break;
                default:break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showNormalDialog(){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(CourseViewActivity.this);
        normalDialog.setIcon(R.drawable.logo);
        normalDialog.setTitle("课程笔记");
        normalDialog.setMessage("你确定要查看课程笔记吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CourseViewActivity.this,DiaryEditActivity.class);
                        startActivity(intent);
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //normalDialog.setCancelable(true);
                    }
                });
        normalDialog.show();
    }
    private void showTimeDialog(){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(CourseViewActivity.this);
        normalDialog.setIcon(R.drawable.logo);
        normalDialog.setTitle("课程闹钟");
        normalDialog.setMessage("已设置为提醒，是否设置闹钟,若选否,则不再设置提醒?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        courseEntity.setCremind(1);
                        update();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        courseEntity.setCremind(0);
                        update();
                    }
                });
        normalDialog.show();
    }
    private void showDelDialog(){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(CourseViewActivity.this);
        normalDialog.setIcon(R.drawable.logo);
        normalDialog.setTitle("删除课程");
        normalDialog.setMessage("你确定要删除该课程吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CourseEntity courseEntity = new CourseEntity();
                        courseEntity.setId(idStatic);
                        mDisposable.add(Completable.fromAction(() -> {courseDao.update(courseEntity);})
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    Toast.makeText(getApplicationContext(),"课程删除成功！",Toast.LENGTH_SHORT).show();
                                    finish();
                                    }, throwable -> {
                                    Toast.makeText(getApplicationContext(),"课程删除失败！",Toast.LENGTH_SHORT).show();
                                }));
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        normalDialog.setCancelable(true);
                    }
                });
        normalDialog.show();
    }
    public void update(){
        mDisposable.add(Completable.fromAction(() -> {courseDao.update(courseEntity);})
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    if(courseEntity.getCremind()==1){
                        Intent intent = new Intent(courseViewActivity,ClockActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("courseEntity", (Serializable) courseEntity);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"课程保存成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"课程保存成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, throwable -> {
                    Toast.makeText(getApplicationContext(),"课程保存失败！",Toast.LENGTH_SHORT).show();
                }));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
