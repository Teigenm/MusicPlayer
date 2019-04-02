package com.teigenm.coursemanager.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;

import android.view.MenuItem;
import android.view.View;

import com.teigenm.coursemanager.R;
import com.teigenm.coursemanager.base.RecyclerOnTouchListener;
import com.teigenm.coursemanager.base.WeekAdapter;
import com.teigenm.coursemanager.database.AppDatabase;
import com.teigenm.coursemanager.database.dao.CourseDao;
import com.teigenm.coursemanager.model.WeekEntity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerViewWeek;
    private WeekAdapter weekAdapter;
    List<WeekEntity> list = new ArrayList<WeekEntity>();
    private AppDatabase appDatabase;
    private String sid="";
    private CourseDao courseDao;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private Context context=HomeActivity.this;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        sid=intent.getStringExtra("sid");
        String password=intent.getStringExtra("password");
        String isRemeber=intent.getStringExtra("isRemeber");
        LoginActivity.getIntstance().editorCommit(sid,password,isRemeber);

        recyclerViewWeek=(RecyclerView)findViewById(R.id.recycler_home_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerViewWeek.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        weekAdapter=new WeekAdapter(list);
        recyclerViewWeek.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //recyclerView.addItemDecoration(getRecyclerViewDivider(R.drawable.inset_recyclerview_divider));
        recyclerViewWeek.setAdapter(weekAdapter);


        appDatabase=AppDatabase.getInstance();
        courseDao=appDatabase.getCourseDao();

        recyclerViewWeek.addOnItemTouchListener(new RecyclerOnTouchListener(this, recyclerViewWeek,
                new RecyclerOnTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(HomeActivity.this,"Click "+position,Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(HomeActivity.this,DayActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putInt("weekDay",position+1);
                        bundle.putString("sid",sid);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    @Override
                    public void onItemLongClick(View view, int position) {
                        //Toast.makeText(HomeActivity.this,"Long Click "+list.get(position),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(HomeActivity.this,DayActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putInt("weekDay",position+1);
                        bundle.putString("sid",sid);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }));
        init();
    }
    public void init(){
        Resources resources =getResources();
        String[] strings=resources.getStringArray(R.array.week_index_array);
        for(int i=0;i<strings.length;i++){
            list.add(new WeekEntity(strings[i]));
        }
        weekAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
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
//    public RecyclerView.ItemDecoration getRecyclerViewDivider(@DrawableRes int drawableId) {
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        itemDecoration.setDrawable(getResources().getDrawable(drawableId));
//        return itemDecoration;
//    }

}
