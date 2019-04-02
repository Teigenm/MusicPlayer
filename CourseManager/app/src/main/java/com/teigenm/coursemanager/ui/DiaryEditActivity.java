package com.teigenm.coursemanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teigenm.coursemanager.R;
import com.teigenm.coursemanager.database.AppDatabase;
import com.teigenm.coursemanager.database.dao.DiaryDao;
import com.teigenm.coursemanager.model.DiaryEntity;

public class DiaryEditActivity extends AppCompatActivity {

    private EditText text_title;
    private EditText text_content;
    private Button button_confirm;
    private AppDatabase appDatabase;
    private long id=0;
    private DiaryDao diaryDao;
    private int flag;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_diary_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        text_title = (EditText)findViewById(R.id.text_diary_title);
        text_content = (EditText)findViewById(R.id.text_diary_content);
        button_confirm = (Button)findViewById(R.id.button_diary_confirm);

        appDatabase=AppDatabase.getInstance();
        diaryDao = appDatabase.getDiaryDao();
        flag=0;
        mDisposable.add(
                diaryDao.getOne(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subject -> {
                            text_title.setText(subject.getDtitle());
                            text_content.setText(subject.getDcontent());
                            flag=1;
                        }, throwable -> {

                        }));

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = text_title.getText().toString().trim();
                String content = text_content.getText().toString().trim();
                DiaryEntity diaryEntity = new DiaryEntity(id,title,content);

                mDisposable.add(Completable.fromAction(() -> {
                    if(flag==0){
                        diaryDao.add(diaryEntity);
                    }else{
                        diaryDao.update(diaryEntity);
                    }}).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            Toast.makeText(getApplicationContext(),"保存笔记成功！",Toast.LENGTH_SHORT).show();
                            finish();
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(),"保存笔记失败！",Toast.LENGTH_SHORT).show();
                        }));

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
