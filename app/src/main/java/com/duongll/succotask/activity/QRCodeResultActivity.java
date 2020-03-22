package com.duongll.succotask.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.duongll.succotask.R;
import com.duongll.succotask.adapter.TaskAdapter;
import com.duongll.succotask.api.TaskApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.entity.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QRCodeResultActivity extends AppCompatActivity {

    private Long userId;
    private ListView listTask;
    private TextView txtRole, txtName, txtId, txtTeamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_result);
        Intent intent = this.getIntent();
        userId = intent.getLongExtra("user_id", new Long(0));
        txtRole = findViewById(R.id.txtUserRole);
        String role = intent.getStringExtra("role");
        txtRole.setText(role);
        txtName = findViewById(R.id.txtUserName);
        txtName.setText(intent.getStringExtra("name"));
        txtId = findViewById(R.id.txtUserId);
        txtId.setText(userId + "");
        txtTeamName = findViewById(R.id.txtUserTeamName);
        txtTeamName.setText(intent.getStringExtra("team_name"));
        listTask = findViewById(R.id.listUserTask);
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<List<Task>> listTaskCall = taskApi.getTaskListForUser(userId);
        listTaskCall.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.code() == 200) {
                    TaskAdapter taskAdapter = new TaskAdapter();
                    taskAdapter.setTaskList(response.body());
                    QRCodeResultActivity.this.listTask.setAdapter(taskAdapter);
                    listTask.setAdapter(taskAdapter);
                    listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Task dto = (Task) listTask.getItemAtPosition(position);
                            Intent intentDetail = new Intent(QRCodeResultActivity.this, UserTaskDetailActivity.class);
                            intentDetail.putExtra("DTO", dto);
                            startActivity(intentDetail);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(QRCodeResultActivity.this);
                alertDialog.setTitle("Error Message");
                alertDialog.setMessage("User don't have any task in progress yet.");
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
