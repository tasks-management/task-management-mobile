package com.duongll.succotask.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.duongll.succotask.R;
import com.duongll.succotask.adapter.EmptyAdapter;
import com.duongll.succotask.adapter.TaskAdapter;
import com.duongll.succotask.api.TaskApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.entity.Task;
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
        Call<List<Task>> call = taskApi.getUserHistory(userId, null, null, "PENDING");
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.code() == 200) {
                    if (response.body().size() != 0) {
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
                        EmptyAdapter emptyAdapter = new EmptyAdapter();
                        emptyAdapter.setTaskList(new ArrayList<Task>());
                        listTaskRequest.setAdapter(emptyAdapter);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(YourTaskRequestActivity.this);
                        alertDialog.setTitle("Message");
                        alertDialog.setMessage("You don't have any task request");
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
                listTaskRequest.setAdapter(emptyAdapter);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(YourTaskRequestActivity.this);
                alertDialog.setTitle("Message");
                alertDialog.setMessage("You don't have any task request");
                alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
    }
}
