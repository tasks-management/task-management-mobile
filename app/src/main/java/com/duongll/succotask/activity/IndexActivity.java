package com.duongll.succotask.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.duongll.succotask.adapter.EmptyAdapter;
import com.duongll.succotask.api.UserApi;
import com.duongll.succotask.config.AppConfig;
import com.duongll.succotask.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

public class IndexActivity extends AppCompatActivity {

    private ListView listTask;
    private String role;
    private Long userId, teamId;
    private TextView txtRole, txtName, txtId, txtTeamName;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        final Intent intent = this.getIntent();
        userId = intent.getLongExtra("user_id", new Long(0));
        txtRole = findViewById(R.id.txtUserRole);
        role = intent.getStringExtra("role");
        txtRole.setText(role);
        txtName = findViewById(R.id.txtUserName);
        txtName.setText(intent.getStringExtra("name"));
        txtId = findViewById(R.id.txtUserId);
        txtId.setText(userId + "");
        teamId = intent.getLongExtra("team_id", new Long(0));
        txtTeamName = findViewById(R.id.txtUserTeamName);
        txtTeamName.setText(intent.getStringExtra("team_name"));
        listTask = findViewById(R.id.listTask);
        String token = AppConfig.getFirebaseToken();
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        UserApi api = APIConfig.getAPIFromClass(retrofit, UserApi.class);
        Call<User> userCall = api.refreshedNewToken(userId, token);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_qrcode:
                        clickToQrCodePage();
                        break;
                    case R.id.navigation_tasks:
                        clickToManageTask(null);
                        break;
                    case R.id.navigation_history:
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
        Call<List<Task>> listTaskCall = taskApi.getTaskListForUser(userId);
        listTaskCall.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.code() == 200) {
                    if (response.body().size() != 0) {
                        List<String> taskNearExpired = new ArrayList<>();
                        List<String> taskExpired = new ArrayList<>();
                        TaskAdapter taskAdapter = new TaskAdapter();
                        taskAdapter.setTaskList(response.body());
                        Calendar calendar = Calendar.getInstance();
                        Date currentDay = new Date();
                        Date deadline;
                        calendar.setTime(currentDay);
                        currentDay = calendar.getTime();
                        for (Task task : response.body()) {
                            calendar.setTime(currentDay);
                            calendar.add(Calendar.DATE, 1);
                            int date = calendar.get(Calendar.DATE);
                            int month = calendar.get(Calendar.MONTH);
                            int year = calendar.get(Calendar.YEAR);
                            int currentYear = year - 1900;
                            deadline = new Date(currentYear, month, date);
                            if (deadline.equals(task.getEndDate())) {
                                taskNearExpired.add("ID: " + task.getId() + " , Name: " + task.getName());
                            }
                            if (task.getEndDate().before(currentDay)) {
                                task.setTaskStatus("EXPIRED");
                                taskExpired.add("ID: " + task.getId() + " , Name: " + task.getName());
                            }
                        }
                        String message = "";
                        if (taskExpired.size() != 0) {
                            for (String s : taskExpired) {
                                message += s + " is expired. \n";
                            }
                        }
                        if (taskNearExpired.size() != 0) {
                            for (String s : taskNearExpired) {
                                message += s + " will expired in 24h. \n";
                            }
                        }
                        if (message.length() != 0) {
                            if (!flag) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(IndexActivity.this);
                                alertDialog.setTitle("Message");
                                alertDialog.setMessage(message);
                                alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                alertDialog.show();
                                IndexActivity.this.flag = true;
                            }
                        }
                        IndexActivity.this.listTask.setAdapter(taskAdapter);
                        listTask.setAdapter(taskAdapter);
                        listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Task dto = (Task) listTask.getItemAtPosition(position);
                                Intent intentDetail = new Intent(IndexActivity.this, TaskDetailActivity.class);
                                intentDetail.putExtra("DTO", dto);
                                intentDetail.putExtra("role", IndexActivity.this.role);
                                startActivity(intentDetail);
                            }
                        });
                    } else {
                        EmptyAdapter emptyAdapter = new EmptyAdapter();
                        emptyAdapter.setTaskList(new ArrayList<Task>());
                        listTask.setAdapter(emptyAdapter);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(IndexActivity.this);
                        alertDialog.setTitle("Message");
                        alertDialog.setMessage("You don't have any task yet");
                        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                EmptyAdapter emptyAdapter = new EmptyAdapter();
                emptyAdapter.setTaskList(new ArrayList<Task>());
                listTask.setAdapter(emptyAdapter);
                if (!flag) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(IndexActivity.this);
                    alertDialog.setTitle("Message");
                    alertDialog.setMessage("You don't have any task yet");
                    alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                    IndexActivity.this.flag = true;
                }
            }
        });
    }

    public void clickToQrCodePage() {
        Intent intent = new Intent(this, ScanQRCodeActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("role", role);
        intent.putExtra("team_id", teamId);
        startActivity(intent);
    }

    public void clickToHistoryPage(View view) {
        Intent intent;
        if (role.contains("manager")) {
            intent = new Intent(this, HistoryTaskActivity.class);
        } else {
            intent = new Intent(this, UserHistoryTaskActivity.class);
        }
        intent.putExtra("role", role);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }


    public void clickToLogOut(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        Retrofit retrofit = APIConfig.createRetrofitForAPI();
//        UserApi userApi = APIConfig.getAPIFromClass(retrofit, UserApi.class);
//        Call<User> call = userApi.refreshedNewToken(userId, "");
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//            }
//        });
        finish();
        startActivity(intent);
    }

    public void clickToManageTask(View view) {
        Intent intent;
        if (role.equals("manager")) {
            intent = new Intent(this, ManageTaskActivity.class);
        } else {
            intent = new Intent(this, UserManageTaskActivity.class);
        }
        intent.putExtra("user_id", userId);
        intent.putExtra("role", role);
        startActivity(intent);
    }
}
