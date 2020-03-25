package com.duongll.succotask.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.duongll.succotask.adapter.EmptyAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import com.duongll.succotask.R;
import com.duongll.succotask.adapter.TaskAdapter;
import com.duongll.succotask.api.TaskApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.entity.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HistoryTaskActivity extends AppCompatActivity {

    private ListView listTask;
    private Long userId;
    private String role;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_task);
        final Intent intent = this.getIntent();
        userId = intent.getLongExtra("user_id", 0);
        role = intent.getStringExtra("role");
        BottomNavigationView bottomNavigationView = findViewById(R.id.manager_history_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_manager_history_date:
                        clickToFilterByDate(null);
                        break;
                    case R.id.navigation_manager_history_status:
                        clickToFilterByStatus(null);
                        break;
                    case R.id.navigation_manager_history_user_id:
                        clickToFilterByUserId(null);
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
                    if (response.body().size() != 0) {
                        TaskAdapter adapter = new TaskAdapter();
                        adapter.setTaskList(response.body());
                        listTask = findViewById(R.id.listHistoryTask);
                        listTask.setAdapter(adapter);
                        listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Task dto = (Task) listTask.getItemAtPosition(position);
                                Intent intentDetail = new Intent(HistoryTaskActivity.this, HistoryTaskDetailActivity.class);
                                intentDetail.putExtra("DTO", dto);
                                intentDetail.putExtra("role", role);
                                startActivity(intentDetail);
                            }
                        });
                    } else {
                        EmptyAdapter emptyAdapter = new EmptyAdapter();
                        emptyAdapter.setTaskList(new ArrayList<Task>());
                        listTask.setAdapter(emptyAdapter);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HistoryTaskActivity.this);
                        alertDialog.setTitle("Message");
                        alertDialog.setMessage("You don't have any history task yet");
                        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                    }
                } else {
                    EmptyAdapter emptyAdapter = new EmptyAdapter();
                    emptyAdapter.setTaskList(new ArrayList<Task>());
                    listTask.setAdapter(emptyAdapter);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(HistoryTaskActivity.this);
                    alertDialog.setTitle("Message");
                    alertDialog.setMessage("Cannot get history task");
                    alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                EmptyAdapter emptyAdapter = new EmptyAdapter();
                emptyAdapter.setTaskList(new ArrayList<Task>());
                listTask.setAdapter(emptyAdapter);
                if (!flag) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(HistoryTaskActivity.this);
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
        intent.putExtra("role", role);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

    public void clickToFilterByStatus(View view) {
        Intent intent = new Intent(this, FilterByStatusActivity.class);
        intent.putExtra("role", role);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

    public void clickToFilterByUserId(View view) {
        Intent intent = new Intent(this, FilterByUserIdActivity.class);
        intent.putExtra("role", role);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }
}
