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

import java.util.List;

import com.duongll.succotask.R;
import com.duongll.succotask.adapter.TaskSubmitAdapter;
import com.duongll.succotask.api.TaskApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.entity.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListApproveTaskRequestActivity extends AppCompatActivity {

    private ListView listTask;
    private Long userId;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_approve_task_request);
        listTask = findViewById(R.id.listFinishTask);
        Intent intent = this.getIntent();
        userId = intent.getLongExtra("user_id", new Long(0));
        role = intent.getStringExtra("role");
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
                                Intent intentDetail;
                                if (dto.getTaskStatus().equals("SUBMITTED")) {
                                    intentDetail = new Intent(ListApproveTaskRequestActivity.this, TaskSubmitDetailActivity.class);
                                } else {
                                    intentDetail = new Intent(ListApproveTaskRequestActivity.this, PendingTaskDetailActivity.class);
                                }
                                intentDetail.putExtra("DTO", dto);
                                intentDetail.putExtra("user_id", userId);
                                intentDetail.putExtra("role", role);
                                startActivity(intentDetail);
                            }
                        });
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListApproveTaskRequestActivity.this);
                    alertDialog.setTitle("Error Message");
                    alertDialog.setMessage("Cannot list any pending task");
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListApproveTaskRequestActivity.this);
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

