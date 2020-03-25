package com.duongll.succotask.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.duongll.succotask.R;
import com.duongll.succotask.adapter.EmptyAdapter;
import com.duongll.succotask.adapter.TaskAdapter;
import com.duongll.succotask.api.TaskApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.entity.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FilterByDateActivity extends AppCompatActivity {

    private Long userId;
    private TextView txtFrom, txtTo;
    private Date searchFrom, searchTo;
    private ListView listHistory;
    private String role;
    private TextView txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_by_date);
        txtFrom = findViewById(R.id.txtHistoryDateFrom);
        txtTo = findViewById(R.id.txtHistoryDateTo);
        Intent intent = this.getIntent();
        userId = intent.getLongExtra("user_id", new Long(0));
        role = intent.getStringExtra("role");
        txtError = findViewById(R.id.txtError);
    }

    public void clickToGetFromDate(View view) {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                txtFrom.setText(year +"/" + (month+1) + "/" + day);
                cal.set(year, month, day);
                searchFrom = cal.getTime();
            }
        };
        String s = txtFrom.getText() + "";
        Calendar cal = Calendar.getInstance();
        int day, month, year;
        if (!txtFrom.getText().toString().contains("DD")) {
            String strArrtmp[] = s.split("/");
            day = Integer.parseInt(strArrtmp[2]);
            month = Integer.parseInt(strArrtmp[1]) - 1;
            year = Integer.parseInt(strArrtmp[0]);
        } else {
            day = cal.get(Calendar.DAY_OF_MONTH);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);
        }
        DatePickerDialog pic = new DatePickerDialog(this, callback, year, month, day);
        pic.setTitle("Start Date");
        pic.show();
    }

    public void clickToGetToDate(View view) {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                txtTo.setText(year +"/" + (month+1) + "/" + day);
                cal.set(year, month, day);
                searchTo = cal.getTime();
            }
        };
        String s = txtTo.getText() + "";
        Calendar cal = Calendar.getInstance();
        int day, month, year;
        if (!txtTo.getText().toString().contains("DD")) {
            String strArrtmp[] = s.split("/");
            day = Integer.parseInt(strArrtmp[2]);
            month = Integer.parseInt(strArrtmp[1]) - 1;
            year = Integer.parseInt(strArrtmp[0]);
        } else {
            day = cal.get(Calendar.DAY_OF_MONTH);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);
        }
        DatePickerDialog pic = new DatePickerDialog(this, callback, year, month, day);
        pic.setTitle("End Date");
        pic.show();
    }

    public void clickToSearchByDate(View view) {
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        if (searchFrom == null) {
            txtError.setText("From date is empty");
            return;
        }
        if (searchTo == null) {
            txtError.setText("End date is empty");
            return;
        }
        Call<List<Task>> callList = taskApi.getUserHistory(userId, searchFrom, searchTo, null);
        callList.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.code() == 200) {
                    if (response.body().size() != 0) {
                        FilterByDateActivity.this.listHistory = findViewById(R.id.listHistoryByDate);
                        TaskAdapter adapter = new TaskAdapter();
                        adapter.setTaskList(response.body());
                        listHistory.setAdapter(adapter);
                        listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Task dto = (Task) listHistory.getItemAtPosition(position);
                                Intent intentDetail = new Intent(FilterByDateActivity.this, HistoryTaskDetailActivity.class);
                                intentDetail.putExtra("DTO", dto);
                                intentDetail.putExtra("role", role);
                                startActivity(intentDetail);
                            }
                        });
                    } else {
                        EmptyAdapter emptyAdapter = new EmptyAdapter();
                        emptyAdapter.setTaskList(new ArrayList<Task>());
                        listHistory.setAdapter(emptyAdapter);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterByDateActivity.this);
                        alertDialog.setTitle("Message");
                        alertDialog.setMessage("You don't have any history task in that from-to date");
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
                EmptyAdapter emptyAdapter = new EmptyAdapter();
                emptyAdapter.setTaskList(new ArrayList<Task>());
                listHistory.setAdapter(emptyAdapter);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterByDateActivity.this);
                alertDialog.setTitle("Message");
                alertDialog.setMessage("You don't have any history task in that from-to date");
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
