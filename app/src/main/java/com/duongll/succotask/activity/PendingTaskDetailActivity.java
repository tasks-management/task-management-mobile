package com.duongll.succotask.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.duongll.succotask.R;
import com.duongll.succotask.api.TaskApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.entity.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PendingTaskDetailActivity extends AppCompatActivity {

    private TextView txtIdDetail, txtNameDetail, txtDescriptionDetail, txtProcessDetail,
            txtCreatedDate, txtStartDate, txtEndDate, txtLastModified,
            txtCreator, txtHandler;
    private Task dto;
    private Long userId;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_task_detail);
        Intent intent = this.getIntent();
        dto = (Task) intent.getSerializableExtra("DTO");
        String[] strTmpStart = dto.getStartDate().toString().split(" ");
        String dayFrom = strTmpStart[2];
        String monthFrom = strTmpStart[1];
        String yearFrom = strTmpStart[5];
        String[] strTmpEnd = dto.getEndDate().toString().split(" ");
        String dayEnd = strTmpEnd[2];
        String monthEnd = strTmpEnd[1];
        String yearEnd = strTmpEnd[5];
        String[] strTmpCreated = dto.getCreated().toString().split(" ");
        String dayCreated = strTmpCreated[2];
        String monthCreated = strTmpCreated[1];
        String yearCreated = strTmpCreated[5];
        String start = yearFrom + "/" +monthFrom + "/" + dayFrom;
        String end = yearEnd + "/" + monthEnd + "/" + dayEnd;
        String created = yearCreated + "/" + monthCreated + "/" + dayCreated;
        txtIdDetail = findViewById(R.id.txtTaskIdDetail);
        txtIdDetail.setText(dto.getId() + "");
        txtNameDetail = findViewById(R.id.txtTaskNameDetail);
        userId = intent.getLongExtra("user_id", new Long(0));
        role = intent.getStringExtra("role");
        if (dto.getName() != null) {
            txtNameDetail.setText(dto.getName());
        } else {
            txtNameDetail.setText("No Name Task");
        }
        txtDescriptionDetail = findViewById(R.id.txtTaskDescriptionDetail);
        if (dto.getDescription() != null) {
            txtDescriptionDetail.setText(dto.getDescription());
        } else {
            txtDescriptionDetail.setText("No Description Task");
        }
        txtProcessDetail = findViewById(R.id.txtProcessContentDetail);
        if (dto.getContentProcess() != null) {
            txtProcessDetail.setText(dto.getContentProcess());
        } else {
            txtProcessDetail.setText("No Process Content");
        }
        txtCreatedDate = findViewById(R.id.txtTaskCreatedDetail);
        if (dto.getCreated() != null) {
            txtCreatedDate.setText(created);
        }
        txtStartDate = findViewById(R.id.txtTaskStartDateDetail);
        if (dto.getStartDate() != null) {
            txtStartDate.setText(start);
        }
        txtEndDate = findViewById(R.id.txtTaskEndDateDetail);
        if (dto.getEndDate() != null) {
            txtEndDate.setText(end);
        }
        txtLastModified = findViewById(R.id.txtTaskModifiedDetail);
        if (dto.getLastModified() != null) {
            txtLastModified.setText(dto.getLastModified().toString());
        } else {
            txtLastModified.setText("Not Modified Yet");
        }
        txtCreator = findViewById(R.id.txtTaskCreatorDetail);
        if (dto.getCreatorId() != null) {
            txtCreator.setText(dto.getCreatorId().getId() + "");
        }
        txtHandler = findViewById(R.id.txtTaskHandlerDetail);
        if (dto.getHandlerId() != null) {
            txtHandler.setText(dto.getHandlerId().getId() + "");
        }
        BottomNavigationView submitedNavigationView = findViewById(R.id.admin_approve_pending_bottom_navigation);
        submitedNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_accept:
                        clickToAcceptPendingTask();
                        break;
                    case R.id.navigation_decline:
                        clickToDeclineSubmitTask();
                        break;
                    case R.id.navigation_modify:
                        clickToModifyTask();
                        break;
                }
                return true;
            }
        });
    }

    private void clickToDeclineSubmitTask() {
        Long taskId = Long.parseLong(txtIdDetail.getText().toString());
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<Task> callTask = taskApi.changeTaskStatus(taskId, "REJECT");
        callTask.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.code() == 200) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PendingTaskDetailActivity.this);
                    alertDialog.setTitle("Message");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("You have decline user pending task successfully");
                    alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PendingTaskDetailActivity.this.finish();
                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PendingTaskDetailActivity.this);
                alertDialog.setTitle("Error Message");
                alertDialog.setMessage("You have decline user pending task failed");
                alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
    }

    private void clickToAcceptPendingTask(){
        Long taskId = Long.parseLong(txtIdDetail.getText().toString());
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<Task> callTask = taskApi.changeTaskStatus(taskId, "IN PROGRESS");
        callTask.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                Log.e("TAGAGAG", response.code() + "");
                if (response.code() == 200) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PendingTaskDetailActivity.this);
                    alertDialog.setTitle("Message");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("You have accepted user pending task successfully");
                    alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PendingTaskDetailActivity.this.finish();
                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PendingTaskDetailActivity.this);
                alertDialog.setTitle("Error Message");
                alertDialog.setMessage("You have accepted user pending task failed");
                alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
    }

    private void clickToModifyTask() {
        Intent intent = new Intent(this, RecreateTaskActivity.class);
        intent.putExtra("DTO", dto);
        intent.putExtra("message", "modify");
        intent.putExtra("user_id", userId);
        intent.putExtra("role", role);
        finish();
        startActivity(intent);
    }
}
