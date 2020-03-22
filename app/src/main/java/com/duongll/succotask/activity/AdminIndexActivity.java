package com.duongll.succotask.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;

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

public class AdminIndexActivity extends AppCompatActivity {

    private TextView txtName, txtUserId, txtUserRole;
    private Long userId;
    private ListView listTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_index);
        Intent intent = this.getIntent();
        txtName = findViewById(R.id.txtAdminName);
        txtName.setText(intent.getStringExtra("name"));
        txtUserId = findViewById(R.id.txtAdminId);
        txtUserId.setText(intent.getLongExtra("user_id", 0) + "");
        txtUserRole = findViewById(R.id.txtAdminRole);
        txtUserRole.setText(intent.getStringExtra("role"));
        userId = Long.parseLong(txtUserId.getText().toString());
        listTask = findViewById(R.id.listAdminTask);
        BottomNavigationView bottomNavigationView = findViewById(R.id.admin_menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_admin_tasks:
                        clickToManageTask(null);
                        break;
                    case R.id.navigation_admin_users:
                        clickToManageUser(null);
                        break;
                    case R.id.navigation_admin_history:
                        clickToHistoryPage(null);
                        break;
                    case R.id.navigation_logout:
                        clickToLogOut(null);
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
        Call<List<Task>> listTaskCall = taskApi.getTaskListForUser(Long.parseLong(txtUserId.getText().toString()));
        listTaskCall.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.code() == 200) {
                    TaskAdapter taskAdapter = new TaskAdapter();
                    taskAdapter.setTaskList(response.body());
                    AdminIndexActivity.this.listTask.setAdapter(taskAdapter);
                    listTask.setAdapter(taskAdapter);
                    listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Task dto = (Task) listTask.getItemAtPosition(position);
                            Intent intentDetail = new Intent(AdminIndexActivity.this, TaskDetailActivity.class);
                            intentDetail.putExtra("DTO", dto);
                            intentDetail.putExtra("message", "index");
                            intentDetail.putExtra("role", "admin");
                            startActivity(intentDetail);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminIndexActivity.this);
                alertDialog.setTitle("Message");
                alertDialog.setMessage("You don't have any task yet");
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
    }

    public void clickToManageUser(View view) {
        Intent intent = new Intent(this, ManageUserActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("role", txtUserRole.getText().toString());
        startActivity(intent);
    }

    public void clickToManageTask(View view) {
        Intent intent = new Intent(this, ManageTaskActivity.class);
        intent.putExtra("user_id", txtUserId.getText().toString());
        intent.putExtra("role", txtUserRole.getText().toString());
        startActivity(intent);
    }

    public void clickToHistoryPage(View view) {
        Intent intent = new Intent(this, HistoryTaskActivity.class);
        Long userId = Long.parseLong(txtUserId.getText().toString());
        intent.putExtra("role", "admin");
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

    public void clickToLogOut(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }
}
