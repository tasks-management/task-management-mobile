package com.duongll.succotask.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.duongll.succotask.R;
import com.duongll.succotask.adapter.EmptyAdapter;
import com.duongll.succotask.adapter.TaskSubmitAdapter;
import com.duongll.succotask.api.TaskApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.entity.Task;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListSubmitTaskForAdminActivity extends AppCompatActivity {

    private Long userId;
    private ListView listSubmitTask;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_submit_task_for_admin);
        Intent intent = this.getIntent();
        userId = intent.getLongExtra("user_id", new Long(0));
        listSubmitTask = findViewById(R.id.listFinishTaskAdmin);
        role = intent.getStringExtra("role");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<List<Task>> taskCall = taskApi.getAllSubmittedTaskForAdmin(userId);
        taskCall.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.code() == 200) {
                    TaskSubmitAdapter taskAdapter = new TaskSubmitAdapter();
                    taskAdapter.setTaskList(response.body());
                    listSubmitTask.setAdapter(taskAdapter);
                    listSubmitTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Task dto = (Task) listSubmitTask.getItemAtPosition(position);
                            Intent intentDetail;
                            if (dto.getTaskStatus().equals("SUBMITTED")) {
                                intentDetail = new Intent(ListSubmitTaskForAdminActivity.this, TaskSubmitDetailActivity.class);
                            } else {
                                intentDetail = new Intent(ListSubmitTaskForAdminActivity.this, PendingTaskDetailActivity.class);
                            }
                            intentDetail.putExtra("DTO", dto);
                            intentDetail.putExtra("user_id", userId);
                            intentDetail.putExtra("role", role);
                            startActivity(intentDetail);
                        }
                    });
                } else {
                    TaskSubmitAdapter taskAdapter = new TaskSubmitAdapter();
                    taskAdapter.setTaskList(new ArrayList<Task>());
                    ListSubmitTaskForAdminActivity.this.listSubmitTask.setAdapter(taskAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                EmptyAdapter emptyAdapter = new EmptyAdapter();
                emptyAdapter.setTaskList(new ArrayList<Task>());
                listSubmitTask.setAdapter(emptyAdapter);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListSubmitTaskForAdminActivity.this);
                alertDialog.setTitle("Error Message");
                alertDialog.setMessage("You don't have any pending task yet");
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
