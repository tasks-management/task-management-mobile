package com.duongll.succotask.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.duongll.succotask.adapter.TaskAdapter;
import com.duongll.succotask.api.TaskApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.entity.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.duongll.succotask.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserHistoryTaskActivity extends AppCompatActivity {

    private Long userId;
    private ListView listTask;
    private String role;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history_task);
        Intent intent = this.getIntent();
        userId = intent.getLongExtra("user_id", new Long(0));
        role = intent.getStringExtra("role");
        listTask = findViewById(R.id.listUserHistoryTask);
        BottomNavigationView bottomNavigationView = findViewById(R.id.user_history_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_user_history_date:
                        clickToFilterByDate(null);
                        break;
                    case R.id.navigation_user_history_status:
                        clickToFilterByStatus(null);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<List<Task>> taskCall = taskApi.getUserHistory(userId, null, null, null);
        taskCall.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.code() == 200) {
                    TaskAdapter adapter = new TaskAdapter();
                    adapter.setTaskList(response.body());
                    listTask.setAdapter(adapter);
                    listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Task dto = (Task) listTask.getItemAtPosition(position);
                            Intent intentDetail = new Intent(UserHistoryTaskActivity.this, HistoryTaskDetailActivity.class);
                            intentDetail.putExtra("DTO", dto);
                            intentDetail.putExtra("role", role);
                            startActivity(intentDetail);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                if (!flag) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserHistoryTaskActivity.this);
                    alertDialog.setTitle("Message");
                    alertDialog.setMessage("You don't have any history task yet");
                    alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                    flag = true;
                }
            }
        });
    }

    public void clickToFilterByDate(View view) {
        Intent intent = new Intent(this, FilterByDateActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("role", role);
        startActivity(intent);
    }

    public void clickToFilterByStatus(View view) {
        Intent intent = new Intent(this, FilterByStatusActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("role", role);
        startActivity(intent);
    }

}
