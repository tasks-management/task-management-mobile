package com.duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.duongll.succotask.R;
import com.duongll.succotask.config.AppConfig;
import com.duongll.succotask.entity.Task;

public class HistoryTaskDetailActivity extends AppCompatActivity {

    private Button btnReAssign;
    private TextView txtIdDetail, txtNameDetail, txtDescriptionDetail, txtProcessDetail,
            txtCreatedDate, txtStartDate, txtEndDate, txtLastModified,
            txtRating, txtComment, txtCommentTime, txtCreator, txtHandler, txtStatus;
    private String role;
    private ImageView imageView;
    private Task taskDto;
    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_task_detail);
        Intent intent = this.getIntent();
        role = intent.getStringExtra("role");
        taskDto = (Task) intent.getSerializableExtra("DTO");
        String status = taskDto.getTaskStatus();
        btnReAssign = findViewById(R.id.btnReassignTask);
        if (!status.equals("FAIL")) {
            btnReAssign.setVisibility(View.GONE);
            btnReAssign.setEnabled(false);
        }
        userId = intent.getLongExtra("user_id", new Long(0));
        String[] strTmpStart = taskDto.getStartDate().toString().split(" ");
        String dayFrom = strTmpStart[2];
        String monthFrom = strTmpStart[1];
        String yearFrom = strTmpStart[5];
        String[] strTmpEnd = taskDto.getEndDate().toString().split(" ");
        String dayEnd = strTmpEnd[2];
        String monthEnd = strTmpEnd[1];
        String yearEnd = strTmpEnd[5];
        String[] strTmpCreated = taskDto.getCreated().toString().split(" ");
        String dayCreated = strTmpCreated[2];
        String monthCreated = strTmpCreated[1];
        String yearCreated = strTmpCreated[5];
        String start = yearFrom + "/" +monthFrom + "/" + dayFrom;
        String end = yearEnd + "/" + monthEnd + "/" + dayEnd;
        String created = yearCreated + "/" + monthCreated + "/" + dayCreated;
        txtIdDetail = findViewById(R.id.txtTaskIdDetail);
        txtIdDetail.setText(taskDto.getId() + "");
        txtNameDetail = findViewById(R.id.txtTaskNameDetail);
        if (taskDto.getName() != null) {
            txtNameDetail.setText(taskDto.getName());
        } else {
            txtNameDetail.setText("No Name Task");
        }
        txtDescriptionDetail = findViewById(R.id.txtTaskDescriptionDetail);
        if (taskDto.getDescription() != null) {
            txtDescriptionDetail.setText(taskDto.getDescription());
        } else {
            txtDescriptionDetail.setText("No Description Task");
        }
        txtProcessDetail = findViewById(R.id.txtProcessContentDetail);
        if (taskDto.getContentProcess() != null) {
            txtProcessDetail.setText(taskDto.getContentProcess());
        } else {
            txtProcessDetail.setText("No Process Content");
        }
        txtCreatedDate = findViewById(R.id.txtTaskCreatedDetail);
        if (taskDto.getCreated() != null) {
            txtCreatedDate.setText(created);
        }
        txtStartDate = findViewById(R.id.txtTaskStartDateDetail);
        if (taskDto.getStartDate() != null) {
            txtStartDate.setText(start);
        }
        txtEndDate = findViewById(R.id.txtTaskEndDateDetail);
        if (taskDto.getEndDate() != null) {
            txtEndDate.setText(end);
        }
        txtLastModified = findViewById(R.id.txtTaskModifiedDetail);
        if (taskDto.getLastModified() != null) {
            String[] strTmpModify = taskDto.getLastModified().toString().split(" ");
            String modifyDay = strTmpModify[2];
            String modifyMonth = strTmpModify[1];
            String modifyYear = strTmpModify[5];
            String modify = modifyYear + "/" + modifyMonth + "/" + modifyDay;
            txtLastModified.setText(modify);
        } else {
            txtLastModified.setText("Not Modified Yet");
        }
        txtRating = findViewById(R.id.txtTaskRateDetail);
        txtRating.setText(taskDto.getRate() + "");
        txtComment = findViewById(R.id.txtTaskCommentDetail);
        if (taskDto.getCommentContent() == null) {
            txtComment.setText("No Comment");
        } else {
            txtComment.setText(taskDto.getCommentContent());
        }
        txtCommentTime = findViewById(R.id.txtTaskTimeCommentDetail);
        if (taskDto.getTimeComment() == null) {
            txtCommentTime.setText("No Comment Time");
        } else {
            txtCommentTime.setText(taskDto.getTimeComment().toString());
        }
        txtCreator = findViewById(R.id.txtTaskCreatorDetail);
        if (taskDto.getCreatorId() != null) {
            txtCreator.setText(taskDto.getCreatorId().getId() + "");
        }
        txtHandler = findViewById(R.id.txtTaskHandlerDetail);
        if (taskDto.getHandlerId() != null) {
            txtHandler.setText(taskDto.getHandlerId().getId() + "");
        }
        txtStatus = findViewById(R.id.txtTaskStatusDetail);
        txtStatus.setText(taskDto.getTaskStatus());
        imageView = findViewById(R.id.TaskImageDetail);
        if (taskDto.getImage() != null) {
            Bitmap bitmapImage = AppConfig.fromString64ToBitmap(taskDto.getImage());
            imageView.setImageBitmap(bitmapImage);
        }
    }

    public void clickToReassignTask(View view) {
        Intent intent = new Intent(this, RecreateTaskActivity.class);
        Long creator;
        if (userId != 0) {
            creator = userId;
        } else {
            creator = Long.parseLong(txtHandler.getText().toString());
        }
        intent.putExtra("DTO", taskDto);
        intent.putExtra("user_id", creator);
        intent.putExtra("role", role);
        intent.putExtra("message", "reassign");
        startActivity(intent);
    }
}
