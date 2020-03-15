package duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import duongll.succotask.R;
import duongll.succotask.api.TaskApi;
import duongll.succotask.config.APIConfig;
import duongll.succotask.entity.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskDetailActivity extends AppCompatActivity {

    private Button btnSubmit;
    private TextView txtIdDetail, txtNameDetail, txtDescriptionDetail, txtProcessDetail,
            txtSourceDetail, txtCreatedDate, txtStartDate, txtEndDate, txtLastModified,
            txtRating, txtComment, txtCommentTime, txtCreator, txtHandler, txtStatus, txtImage;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Intent intent = this.getIntent();
        if (intent.getStringExtra("message").equals("history")) {
            btnSubmit = findViewById(R.id.btnSubmitTask);
            btnSubmit.setVisibility(View.GONE);
        }
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
        txtSourceDetail = findViewById(R.id.txtSourceDetail);
        if (dto.getSourceHandler() != null) {
            txtSourceDetail.setText(dto.getSourceHandler());
        } else {
            txtSourceDetail.setText("No Source Handler");
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
        txtRating = findViewById(R.id.txtTaskRateDetail);
        txtRating.setText(dto.getRate() + "");
        txtComment = findViewById(R.id.txtTaskCommentDetail);
        if (dto.getCommentContent() == null) {
            txtComment.setText("No Comment");
        } else {
            txtComment.setText(dto.getCommentContent());
        }
        txtCommentTime = findViewById(R.id.txtTaskTimeCommentDetail);
        if (dto.getTimeComment() == null) {
            txtCommentTime.setText("No Comment Time");
        } else {
            txtCommentTime.setText(dto.getTimeComment().toString());
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
        txtImage = findViewById(R.id.txtTaskImageDetail);
        if (dto.getImage() == null) {
            txtImage.setText("No Image");
        } else {
            txtImage.setText(dto.getImage());
        }
    }

    public void clickToSubmitTask(View view) {
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        String status;
        if (role.equals("user")) {
            status = "SUBMITTED";
        } else {
            status = "SUCCEED";
        }
        Call<Task> call = taskApi.changeTaskStatus(Long.parseLong(txtIdDetail.getText().toString()), status);
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.code() == 200) {
                    Toast.makeText(TaskDetailActivity.this, "Your task has been submitted", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(TaskDetailActivity.this, "Decline task failed", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(TaskDetailActivity.this, "Have error when connect to backend", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}
