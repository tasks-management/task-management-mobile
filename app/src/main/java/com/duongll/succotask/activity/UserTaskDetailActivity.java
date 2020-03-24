package com.duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.duongll.succotask.R;
import com.duongll.succotask.entity.Task;

public class UserTaskDetailActivity extends AppCompatActivity {

    private TextView txtIdDetail, txtNameDetail, txtDescriptionDetail, txtProcessDetail,
            txtCreatedDate, txtStartDate, txtEndDate, txtLastModified,
            txtCreator, txtHandler, txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_task_detail);
        Intent intent = this.getIntent();
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
    }
}
