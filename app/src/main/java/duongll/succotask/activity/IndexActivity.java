package duongll.succotask.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import duongll.succotask.R;
import duongll.succotask.adapter.TaskAdapter;
import duongll.succotask.api.TaskApi;
import duongll.succotask.api.TeamApi;
import duongll.succotask.config.APIConfig;
import duongll.succotask.entity.Task;
import duongll.succotask.entity.Team;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IndexActivity extends AppCompatActivity {

    private ListView listTask;
    private String role;
    private Long userId;
    private TextView txtRole, txtName, txtId, txtTeamName;

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
        txtTeamName = findViewById(R.id.txtUserTeamName);
        listTask = findViewById(R.id.listTask);
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TeamApi teamApi = APIConfig.getAPIFromClass(retrofit, TeamApi.class);
        Call<Team> teamCall = teamApi.getTeamInfoById(intent.getLongExtra("team_id", new Long(0)));
        teamCall.enqueue(new Callback<Team>() {
            @Override
            public void onResponse(Call<Team> call, Response<Team> response) {
                if(response.code() == 200) {
                    IndexActivity.this.txtTeamName.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<Team> call, Throwable t) {
                Toast.makeText(IndexActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT);
                return;
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_qrcode:
                        Toast.makeText(IndexActivity.this, "QR Code", Toast.LENGTH_SHORT).show();
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
        Call<List<Task>> listTaskCall = taskApi.getTaskListForUser(Long.parseLong(txtId.getText().toString()));
        listTaskCall.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.code() == 200) {
                    TaskAdapter taskAdapter = new TaskAdapter();
                    taskAdapter.setTaskList(response.body());
                    IndexActivity.this.listTask.setAdapter(taskAdapter);
                    listTask.setAdapter(taskAdapter);
                    listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Task dto = (Task) listTask.getItemAtPosition(position);
                            Intent intentDetail = new Intent(IndexActivity.this, TaskDetailActivity.class);
                            intentDetail.putExtra("DTO", dto);
                            intentDetail.putExtra("message", "index");
                            intentDetail.putExtra("role", IndexActivity.this.role);
                            startActivity(intentDetail);
                        }
                    });
                } else {
                    Toast.makeText(IndexActivity.this, "Cannot found task list from that user id", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(IndexActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
        });
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
