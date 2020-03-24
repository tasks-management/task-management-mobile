package com.duongll.succotask.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.duongll.succotask.R;
import com.duongll.succotask.api.TaskApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.config.AppConfig;
import com.duongll.succotask.dto.SubmitTaskDto;
import com.duongll.succotask.entity.Task;
import com.duongll.succotask.fragment.FinishDialogFragment;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskDetailActivity extends AppCompatActivity implements FinishDialogFragment.OnSuccess {

    private TextView txtIdDetail, txtNameDetail, txtDescriptionDetail, txtProcessDetail,
            txtCreatedDate, txtStartDate, txtEndDate, txtLastModified,
            txtCreator, txtHandler, txtStatus;
    private String role;
    private Button btnSubmit, btnUndone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Intent intent = this.getIntent();
        role = intent.getStringExtra("role");
        Task dto = (Task) intent.getSerializableExtra("DTO");
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
            String[] strTmpModify = dto.getLastModified().toString().split(" ");
            String modifyDay = strTmpModify[2];
            String modifyMonth = strTmpModify[1];
            String modifyYear = strTmpModify[5];
            String modify = modifyYear + "/" + modifyMonth + "/" + modifyDay;
            txtLastModified.setText(modify);
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
        txtStatus = findViewById(R.id.txtTaskStatusDetail);
        txtStatus.setText(dto.getTaskStatus());
        btnSubmit = findViewById(R.id.btnSubmitTask);
        btnUndone = findViewById(R.id.btnUndoneTask);
        if (new Date().after(dto.getEndDate())) {
            btnSubmit.setEnabled(false);
        }
        if (new Date().before(dto.getEndDate())){
            btnUndone.setEnabled(false);
        }
    }

    public void clickToSubmitTask(View view) {
        FinishDialogFragment userSubmitted = new FinishDialogFragment();
        userSubmitted.setOnSuccessCallback(this);
        userSubmitted.show(getSupportFragmentManager(), "TAG");
    }

    public void clickToUndoneTask(View view){
        FinishDialogFragment userSubmitted = new FinishDialogFragment();
        userSubmitted.setOnSuccessCallback(this);
        userSubmitted.show(getSupportFragmentManager(), "TAG");
    }

    @Override
    public void onSuccess(Bitmap bitmap) {
        String status;
        if (btnSubmit.isEnabled()) {
            if (role.equals("user")) {
                status = "SUBMITTED";
            } else {
                status = "SUCCEED";
            }
        } else {
            status = "FAIL";
        }
        if (bitmap != null) {
            Retrofit retrofit = APIConfig.createRetrofitForAPI();
            TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
            SubmitTaskDto submitTask = new SubmitTaskDto();
            submitTask.setImage(AppConfig.fromBitmapToString64(bitmap));
            submitTask.setStatus(status);
            Call<Task> call = taskApi.submitTaskFromUser(Long.parseLong(txtIdDetail.getText().toString()), submitTask);
            call.enqueue(new Callback<Task>() {
                @Override
                public void onResponse(Call<Task> call, Response<Task> response) {
                    if (response.code() == 200) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TaskDetailActivity.this);
                        alertDialog.setTitle("Message");
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage("You have submitted task successfully");
                        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TaskDetailActivity.this.finish();
                            }
                        });
                        alertDialog.show();
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TaskDetailActivity.this);
                        alertDialog.setTitle("Message");
                        alertDialog.setMessage("You have submitted task failed");
                        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<Task> call, Throwable t) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(TaskDetailActivity.this);
                    alertDialog.setTitle("Error Message");
                    alertDialog.setMessage("Error when submitted task please try again.");
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
}
