package duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

public class YourTaskRequestActivity extends AppCompatActivity {

    private ListView listTaskRequest;
    private Retrofit retrofit;
    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_task_request);
        Intent intent = this.getIntent();
        listTaskRequest = findViewById(R.id.listTaskRequest);
        userId = intent.getLongExtra("user_id", new Long(0));
        retrofit = APIConfig.createRetrofitForAPI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<List<Task>> call = taskApi.getAllTaskNeedApproveForManager(userId);
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.code() == 200) {
                    TaskAdapter taskAdapter = new TaskAdapter();
                    taskAdapter.setTaskList(response.body());
                    YourTaskRequestActivity.this.listTaskRequest.setAdapter(taskAdapter);
                    listTaskRequest.setAdapter(taskAdapter);
                    listTaskRequest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Task dto = (Task) listTaskRequest.getItemAtPosition(position);
                            Intent intentDetail = new Intent(YourTaskRequestActivity.this, UserTaskDetailActivity.class);
                            intentDetail.putExtra("DTO", dto);
                            startActivity(intentDetail);
                        }
                    });
                } else {
                    Toast.makeText(YourTaskRequestActivity.this, "Don't have any task request to manager", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                    Toast.makeText(YourTaskRequestActivity.this, "Have error when connect to server", Toast.LENGTH_SHORT).show();
                    return;
            }
        });
    }
}
