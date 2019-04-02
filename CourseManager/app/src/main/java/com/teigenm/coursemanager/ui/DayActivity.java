package com.teigenm.coursemanager.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.teigenm.coursemanager.R;
import com.teigenm.coursemanager.base.CourseAdapter;
import com.teigenm.coursemanager.base.RecyclerOnTouchListener;
import com.teigenm.coursemanager.database.AppDatabase;
import com.teigenm.coursemanager.database.dao.CourseDao;
import com.teigenm.coursemanager.model.CourseEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DayActivity extends AppCompatActivity{
    private RecyclerView recyclerViewDay;
    private CourseAdapter courseAdapter;
    private static final int REQUEST_CODE = 0x01111;
    private List<CourseEntity> list = new ArrayList<CourseEntity>();
    private AppDatabase appDatabase;
    private String sid;
    private int weekDay;
    private CourseDao courseDao;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private Context context = DayActivity.this;
    private TextView day_title_info;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_day);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        weekDay = intent.getIntExtra("weekDay",0);

        Resources resources =getResources();
        String[] strings=resources.getStringArray(R.array.week_index_array);
        day_title_info = (TextView)findViewById(R.id.day_title_info);
        day_title_info.setText(strings[weekDay-1]);

        recyclerViewDay = (RecyclerView)findViewById(R.id.recycler_day_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerViewDay.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        courseAdapter=new CourseAdapter(list);
        recyclerViewDay.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewDay.setAdapter(courseAdapter);

        appDatabase=AppDatabase.getInstance();
        courseDao=appDatabase.getCourseDao();

        recyclerViewDay.addOnItemTouchListener(new RecyclerOnTouchListener(this, recyclerViewDay,
                new RecyclerOnTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Log.d("输出",list.get(position).toString());
                        //Toast.makeText(DayActivity.this,"Click "+list.get(position),Toast.LENGTH_SHORT).show();
                        CourseViewActivity.startActivityForResult(DayActivity.this, REQUEST_CODE,list.get(position).getId());
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        CourseViewActivity.startActivityForResult(DayActivity.this, REQUEST_CODE,list.get(position).getId());
                    }
                }));
        FloatingActionButton button_list = (FloatingActionButton) findViewById(R.id.button_list_day);
        button_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertActivity.startActivityForResult(DayActivity.this, REQUEST_CODE,sid,weekDay);
            }
        });

        init();
    }
    public void init(){
        mDisposable.add(
                courseDao.getWeekDay(sid,weekDay)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subjects -> {
                            //获取数据后的处理
                            //Log.d("list输出",subjects.toString());
                            list.clear();
                            list.addAll(subjects);
                            courseAdapter.notifyDataSetChanged();
                        }, throwable -> {
                            Log.e("加载","加载数据出错");
                        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE) {
            init();
        }
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
