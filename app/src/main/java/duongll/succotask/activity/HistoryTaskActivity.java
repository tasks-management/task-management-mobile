package duongll.succotask.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import duongll.succotask.R;
import duongll.succotask.adapter.TaskAdapter;
import duongll.succotask.api.TaskApi;
import duongll.succotask.config.APIConfig;
import duongll.succotask.entity.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HistoryTaskActivity extends AppCompatActivity {

    private ListView listTask;
    private Long userId;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_task);
        final Intent intent = this.getIntent();
        userId = intent.getLongExtra("user_id", 0);
        role = intent.getStringExtra("role");
        if (userId == 0) {
            Toast.makeText(this, "Cannot get user history", Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<List<Task>> taskCall = taskApi.getUserHistory(userId, null, null, null);
        taskCall.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.code() == 200) {
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
                    Toast.makeText(HistoryTaskActivity.this, "Cannot get history task list", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(HistoryTaskActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
        });
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
