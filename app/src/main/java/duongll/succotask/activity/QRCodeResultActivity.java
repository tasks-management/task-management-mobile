package duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import duongll.succotask.R;
import duongll.succotask.adapter.TaskAdapter;
import duongll.succotask.api.TaskApi;
import duongll.succotask.config.APIConfig;
import duongll.succotask.entity.Task;
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
                } else {
                    Toast.makeText(QRCodeResultActivity.this, "Cannot found task list from that user id", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(QRCodeResultActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}
