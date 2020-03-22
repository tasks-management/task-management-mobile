package com.duongll.succotask.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import com.duongll.succotask.R;
import com.duongll.succotask.api.TaskApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.config.AppConfig;
import com.duongll.succotask.dto.TaskCommentDto;
import com.duongll.succotask.entity.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskSubmitDetailActivity extends AppCompatActivity {

    private TextView txtIdDetail, txtNameDetail, txtDescriptionDetail, txtProcessDetail,
            txtCreatedDate, txtStartDate, txtEndDate, txtLastModified,
            txtCreator, txtHandler;
    private ImageView imageView;
    private Spinner spRating;
    private Long selectedRating = new Long(0);
    private Task dto;
    private EditText edtComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_submit_detail);
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
        imageView = findViewById(R.id.txtTaskImageDetail);
        if (dto.getImage() == null) {
            imageView.setImageBitmap(null);
        } else {
            Bitmap bitmap = AppConfig.fromString64ToBitmap(dto.getImage());
            imageView.setImageBitmap(bitmap);
        }
        edtComment = findViewById(R.id.txtCommentSubmitTask);
        spRating = findViewById(R.id.spinner_rating_submit_task);
        ArrayList<Integer> dataSrc = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            dataSrc.add(i + 1);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataSrc);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRating.setAdapter(dataAdapter);
        spRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TaskSubmitDetailActivity.this.selectedRating = Long.parseLong(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(TaskSubmitDetailActivity.this, "Must choose team for user", Toast.LENGTH_SHORT).show();
            }
        });
        BottomNavigationView submitedNavigationView = findViewById(R.id.admin_approve_submited_bottom_navigation);
        submitedNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_accept:
                            clickToAcceptSubmitTask();
                            break;
                        case R.id.navigation_decline:
                            clickToDeclineSubmitTask();
                            break;
                    }
                    return true;
                }
            });
    }

    private void clickToAcceptSubmitTask() {
        if (selectedRating == 0) {
            Toast.makeText(this, "You must selected rating", Toast.LENGTH_SHORT).show();
            return;
        }
        Long taskId = Long.parseLong(txtIdDetail.getText().toString());
        String comment = edtComment.getText().toString();
        int rating = Integer.parseInt(selectedRating.toString());
        TaskCommentDto dto = new TaskCommentDto(comment, rating,"SUCCEED");
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<Task> callTask = taskApi.acceptOrDeclineSubmitTask(taskId, dto);
        callTask.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.code() == 200) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(TaskSubmitDetailActivity.this);
                    alertDialog.setTitle("Message");
                    alertDialog.setMessage("You have accepted user submitted task successfully");
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TaskSubmitDetailActivity.this);
                alertDialog.setTitle("Error Message");
                alertDialog.setMessage("You have accepted user submitted task successfully");
                alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
        finish();
    }

    private void clickToDeclineSubmitTask() {
        if (selectedRating == 0) {
            Toast.makeText(this, "You must selected rating", Toast.LENGTH_SHORT).show();
            return;
        }
        Long taskId = Long.parseLong(txtIdDetail.getText().toString());
        String comment = edtComment.getText().toString();
        int rating = Integer.parseInt(selectedRating.toString());
        TaskCommentDto dto = new TaskCommentDto(comment, rating,"FAIL");
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<Task> callTask = taskApi.acceptOrDeclineSubmitTask(taskId, dto);
        callTask.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.code() == 200) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(TaskSubmitDetailActivity.this);
                    alertDialog.setTitle("Message");
                    alertDialog.setMessage("You have accepted user submitted task successfully");
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TaskSubmitDetailActivity.this);
                alertDialog.setTitle("Error Message");
                alertDialog.setMessage("You have accepted user submitted task successfully");
                alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
        finish();
    }

}