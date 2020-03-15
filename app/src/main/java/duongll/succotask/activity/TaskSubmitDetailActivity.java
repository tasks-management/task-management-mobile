package duongll.succotask.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import duongll.succotask.R;
import duongll.succotask.api.TaskApi;
import duongll.succotask.config.APIConfig;
import duongll.succotask.dto.TaskCommentDto;
import duongll.succotask.entity.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskSubmitDetailActivity extends AppCompatActivity {

    private TextView txtIdDetail, txtNameDetail, txtDescriptionDetail, txtProcessDetail,
            txtSourceDetail, txtCreatedDate, txtStartDate, txtEndDate, txtLastModified,
            txtComment, txtCreator, txtHandler, txtStatus, txtImage,
            txtCommentTilte, txtRatingTitle, txtTitleSubmit;

    private Spinner spRating;
    private Long selectedRating = new Long(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_submit_detail);
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
        txtComment = findViewById(R.id.txtCommentSubmitTask);
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
        txtCommentTilte = findViewById(R.id.txtCommentText);
        txtRatingTitle = findViewById(R.id.txtRatingText);
        txtTitleSubmit = findViewById(R.id.txtTitleComment);
        BottomNavigationView pendingNavigationView = findViewById(R.id.admin_approve_pending_bottom_navigation);
        BottomNavigationView submitedNavigationView = findViewById(R.id.admin_approve_submited_bottom_navigation);
        if (dto.getTaskStatus().equals("PENDING")) {
            txtComment.setVisibility(View.GONE);
            spRating.setVisibility(View.GONE);
            txtCommentTilte.setVisibility(View.GONE);
            txtRatingTitle.setVisibility(View.GONE);
            txtTitleSubmit.setVisibility(View.GONE);
            submitedNavigationView.setVisibility(View.GONE);
            submitedNavigationView.setEnabled(false);
            pendingNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
        } else {
            pendingNavigationView.setVisibility(View.GONE);
            pendingNavigationView.setEnabled(false);
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
    }

    private void clickToAcceptSubmitTask() {
        if (selectedRating == 0) {
            Toast.makeText(this, "You must selected rating", Toast.LENGTH_SHORT).show();
            return;
        }
        Long taskId = Long.parseLong(txtIdDetail.getText().toString());
        String comment = txtComment.getText().toString();
        int rating = Integer.parseInt(selectedRating.toString());
        TaskCommentDto dto = new TaskCommentDto(comment, rating,"DONE");
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<Task> callTask = taskApi.acceptOrDeclineSubmitTask(taskId, dto);
        callTask.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.code() == 200) {
                    Toast.makeText(TaskSubmitDetailActivity.this, "Accept task successfully", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(TaskSubmitDetailActivity.this, "Accept task failed", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(TaskSubmitDetailActivity.this, "Have error when connect to backend", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void clickToDeclineSubmitTask() {
        if (selectedRating == 0) {
            Toast.makeText(this, "You must selected rating", Toast.LENGTH_SHORT).show();
            return;
        }
        Long taskId = Long.parseLong(txtIdDetail.getText().toString());
        String comment = txtComment.getText().toString();
        int rating = Integer.parseInt(selectedRating.toString());
        TaskCommentDto dto = new TaskCommentDto(comment, rating,"FAILED");
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<Task> callTask = taskApi.acceptOrDeclineSubmitTask(taskId, dto);
        callTask.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.code() == 200) {
                    Toast.makeText(TaskSubmitDetailActivity.this, "Decline task successfully", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(TaskSubmitDetailActivity.this, "Decline task failed", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(TaskSubmitDetailActivity.this, "Have error when connect to backend", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void clickToAcceptPendingTask(){
        Long taskId = Long.parseLong(txtIdDetail.getText().toString());
        String comment = txtComment.getText().toString();
        int rating = Integer.parseInt(selectedRating.toString());
        TaskCommentDto dto = new TaskCommentDto(comment, rating,"IN PROGRESS");
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<Task> callTask = taskApi.acceptOrDeclineSubmitTask(taskId, dto);
        callTask.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.code() == 200) {
                    Toast.makeText(TaskSubmitDetailActivity.this, "Decline task successfully", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(TaskSubmitDetailActivity.this, "Decline task failed", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(TaskSubmitDetailActivity.this, "Have error when connect to backend", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void clickToModifyTask() {

    }
}