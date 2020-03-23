package com.duongll.succotask.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

import com.duongll.succotask.R;
import com.duongll.succotask.adapter.TaskAdapter;
import com.duongll.succotask.api.TaskApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.entity.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FilterByStatusActivity extends AppCompatActivity {

    private Spinner spStatus;
    private String selectedStatus;
    private Long userId;
    private ListView listHistoryByStatus;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_by_status);
        Intent intent = this.getIntent();
        userId = intent.getLongExtra("user_id", new Long(0));
        role = intent.getStringExtra("role");
        spStatus = findViewById(R.id.spinner_status_history);
        List<String> dataSrc = new ArrayList<>();
        dataSrc.add("SUCCEED");
        dataSrc.add("FAIL");
        dataSrc.add("REJECT");
        dataSrc.add("SUBMITTED");
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
                    if (response.body().size() != 0) {
                        FilterByStatusActivity.this.listHistoryByStatus = findViewById(R.id.listHistoryByStatus);
                        TaskAdapter adapter = new TaskAdapter();
                        adapter.setTaskList(response.body());
                        listHistoryByStatus.setAdapter(adapter);
                        listHistoryByStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Task dto = (Task) listHistoryByStatus.getItemAtPosition(position);
                                Intent intentDetail = new Intent(FilterByStatusActivity.this, HistoryTaskDetailActivity.class);
                                intentDetail.putExtra("DTO", dto);
                                intentDetail.putExtra("role", role);
                                startActivity(intentDetail);
                            }
                        });
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterByStatusActivity.this);
                        alertDialog.setTitle("Message");
                        alertDialog.setMessage("You don't have any history task with that status");
                        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterByStatusActivity.this);
                alertDialog.setTitle("Message");
                alertDialog.setMessage("You don't have any history task with that status");
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
