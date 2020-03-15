package duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import duongll.succotask.R;
import duongll.succotask.adapter.TaskSubmitAdapter;
import duongll.succotask.api.TaskApi;
import duongll.succotask.config.APIConfig;
import duongll.succotask.entity.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListApproveTaskRequestActivity extends AppCompatActivity {

    private ListView listTask;
    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_approve_task_request);
        listTask = findViewById(R.id.listFinishTask);
        Intent intent = this.getIntent();
        userId = intent.getLongExtra("user_id", new Long(0));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<List<Task>> taskCall = taskApi.getAllTaskNeedApproveForManager(userId);
        taskCall.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.code() == 200) {
                    TaskSubmitAdapter taskAdapter = new TaskSubmitAdapter();
                    taskAdapter.setTaskList(response.body());
                    ListApproveTaskRequestActivity.this.listTask.setAdapter(taskAdapter);
                    listTask.setAdapter(taskAdapter);
                    listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Task dto = (Task) listTask.getItemAtPosition(position);
                            Intent intentDetail = new Intent(ListApproveTaskRequestActivity.this, TaskSubmitDetailActivity.class);
                            intentDetail.putExtra("DTO", dto);
                            startActivity(intentDetail);
                        }
                    });
                } else {
                    Toast.makeText(ListApproveTaskRequestActivity.this, "Cannot list submited task", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(ListApproveTaskRequestActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}

