package com.teigenm.coursemanager.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teigenm.coursemanager.R;
import com.teigenm.coursemanager.database.AppDatabase;
import com.teigenm.coursemanager.database.dao.CourseDao;
import com.teigenm.coursemanager.model.CourseEntity;
import com.teigenm.coursemanager.ui.view.SelectRemindCyclePopup;
import com.teigenm.coursemanager.ui.view.SelectRemindWayPopup;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ClockActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout repeat_rl, ring_rl,time_rl;
    private TextView tv_repeat_value, tv_ring_value,tv_time_value;
    private LinearLayout allLayout;
    private Button clock_confirm;
    private String time;
    private int cycle;
    private int ring;
    private CourseEntity courseEntity;
    private AppDatabase appDatabase;
    private CourseDao courseDao;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_clock);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseEntity = (CourseEntity) getIntent().getExtras().getSerializable("courseEntity");

        Log.d("输出",courseEntity.toString());

        allLayout = (LinearLayout) findViewById(R.id.all_layout);
        clock_confirm = (Button) findViewById(R.id.button_clock_confirm);
        clock_confirm.setOnClickListener(this);
        repeat_rl = (RelativeLayout) findViewById(R.id.repeat_rl);
        repeat_rl.setOnClickListener(this);
        ring_rl = (RelativeLayout) findViewById(R.id.ring_rl);
        ring_rl.setOnClickListener(this);
        time_rl = (RelativeLayout) findViewById(R.id.time_rl);
        time_rl.setOnClickListener(this);
        tv_repeat_value=(TextView)findViewById(R.id.tv_repeat_value);
        tv_ring_value = (TextView)findViewById(R.id.tv_ring_value);
        tv_time_value = (TextView)findViewById(R.id.tv_time_value);
        appDatabase=AppDatabase.getInstance();
        courseDao = appDatabase.getCourseDao();
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.repeat_rl:
                selectRemindCycle();
                break;
            case R.id.ring_rl:
                selectRingWay();
                break;
            case R.id.time_rl:
                selectTimeWay();
                break;
            case R.id.button_clock_confirm:
                setClock();
                break;
            default:
                break;
        }
    }

    private void setClock() {
        Toast.makeText(getApplicationContext(),"设置闹钟成功",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void selectTimeWay(){

    }

    public void selectRemindCycle() {
        final SelectRemindCyclePopup fp = new SelectRemindCyclePopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindCyclePopupListener(new SelectRemindCyclePopup
                .SelectRemindCyclePopupOnClickListener() {

            @Override
            public void obtainMessage(int flag, String ret) {
                switch (flag) {
                    case 0:
                        tv_repeat_value.setText("只响一次");
                        cycle = 0;
                        fp.dismiss();
                        break;
                    case 1:
                        tv_repeat_value.setText("每周");
                        cycle = 1;
                        fp.dismiss();
                        break;
                    case 2:
                        fp.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public void selectRingWay() {
        SelectRemindWayPopup fp = new SelectRemindWayPopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindWayPopupListener(new SelectRemindWayPopup
                .SelectRemindWayPopupOnClickListener() {

            @Override
            public void obtainMessage(int flag) {
                switch (flag) {
                    // 震动
                    case 0:
                        tv_ring_value.setText("静音");
                        ring = 0;
                        break;
                    case 1:
                        tv_ring_value.setText("震动");
                        ring = 1;
                        break;
                    // 铃声
                    case 2:
                        tv_ring_value.setText("铃声");
                        ring = 1;
                        break;
                    case 3:
                        tv_ring_value.setText("铃声+震动");
                        ring = 3;
                        break;
                    default:
                        break;
                }
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
                courseEntity.setCremind(0);
                mDisposable.add(Completable.fromAction(() -> {courseDao.update(courseEntity);})
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Log.d("输出",courseEntity.toString());
                            finish();
                        }, throwable -> {

                        }));
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
}
