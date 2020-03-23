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


import com.duongll.succotask.api.UserApi;
import com.duongll.succotask.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    boolean flag = false;

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
                    if (response.body().size() != 0) {
                        List<String> taskNearExpired = new ArrayList<>();
                        TaskAdapter taskAdapter = new TaskAdapter();
                        taskAdapter.setTaskList(response.body());
                        Calendar calendar = Calendar.getInstance();
                        Date currentDay = new Date();
                        Date endDate;
                        calendar.setTime(currentDay);
                        calendar.add(Calendar.DATE, 1);
                        currentDay = calendar.getTime();
                        for (Task task: response.body()) {
                            calendar.setTime(task.getEndDate());
                            endDate = calendar.getTime();
                            if (currentDay.after(endDate)) {
                                taskNearExpired.add("ID: " + task.getId() + ", Name: " + task.getName());
                            }
                        }
                        if (taskNearExpired.size() != 0) {
                            String message = "";
                            for (String s : taskNearExpired) {
                                message += s + " will expired in 24h. \n" ;
                            }
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminIndexActivity.this);
                            alertDialog.setTitle("Message");
                            alertDialog.setMessage(message);
                            alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
                        }
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
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                if (!flag) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminIndexActivity.this);
                    alertDialog.setTitle("Message");
                    alertDialog.setMessage("You don't have any task yet");
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                    AdminIndexActivity.this.flag = true;
                }
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
        intent.putExtra("user_id", userId);
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
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        UserApi userApi = APIConfig.getAPIFromClass(retrofit, UserApi.class);
        Call<User> call = userApi.refreshedNewToken(userId, "");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
        finish();
        startActivity(intent);
    }
}
