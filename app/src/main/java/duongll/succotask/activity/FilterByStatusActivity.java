package duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
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

public class FilterByStatusActivity extends AppCompatActivity {

    private Spinner spStatus;
    private String selectedStatus;
    private Long userId;
    private ListView listHistoryByStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_by_status);
        Intent intent = this.getIntent();
        userId = intent.getLongExtra("user_id", new Long(0));
        spStatus = findViewById(R.id.spinner_status_history);
        List<String> dataSrc = new ArrayList<>();
        dataSrc.add("DONE");
        dataSrc.add("FAIL");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataSrc);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(dataAdapter);
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FilterByStatusActivity.this.selectedStatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(FilterByStatusActivity.this, "Must choose team for user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void clickToSearchByStatus(View view) {
        if (selectedStatus == null) {
            Toast.makeText(this, "Your must choose status first", Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        System.out.println(selectedStatus);
        Call<List<Task>> callApi = taskApi.getUserHistory(userId, null, null ,selectedStatus);
        callApi.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.code() == 200) {
                    FilterByStatusActivity.this.listHistoryByStatus = findViewById(R.id.listHistoryByStatus);
                    TaskAdapter adapter = new TaskAdapter();
                    adapter.setTaskList(response.body());
                    listHistoryByStatus.setAdapter(adapter);
                    listHistoryByStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Task dto = (Task) listHistoryByStatus.getItemAtPosition(position);
                            Intent intentDetail = new Intent(FilterByStatusActivity.this, TaskDetailActivity.class);
                            intentDetail.putExtra("DTO", dto);
                            intentDetail.putExtra("message", "history");
                            startActivity(intentDetail);
                        }
                    });
                } else {
                    Toast.makeText(FilterByStatusActivity.this, "Cannot get list task by status", Toast.LENGTH_SHORT);
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(FilterByStatusActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT);
                return;
            }
        });
    }
}
