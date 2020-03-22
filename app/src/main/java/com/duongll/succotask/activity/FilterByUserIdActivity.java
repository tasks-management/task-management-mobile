package com.duongll.succotask.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.duongll.succotask.R;
import com.duongll.succotask.adapter.TaskAdapter;
import com.duongll.succotask.api.TaskApi;
import com.duongll.succotask.api.UserApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.entity.Task;
import com.duongll.succotask.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FilterByUserIdActivity extends AppCompatActivity {

    private Spinner spStatus, spUserId;
    private String selectedStatus, selectedUserId;
    private String role;
    private TextView txtFrom, txtTo;
    private Date searchFrom, searchTo;
    private ListView listHistory;
    private Long userId;
    private Long managerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_by_user_id);
        Intent intent = this.getIntent();
        role = intent.getStringExtra("role");
        spStatus = findViewById(R.id.spinner_status_history);
        spUserId = findViewById(R.id.spinner_user_id_history);
        userId = intent.getLongExtra("user_id", new Long(0));
        managerId = userId;
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
                FilterByUserIdActivity.this.selectedStatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(FilterByUserIdActivity.this, "Must choose team for user", Toast.LENGTH_SHORT).show();
            }
        });
        final List<String> dataUser = new ArrayList<>();
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        UserApi userApi = APIConfig.getAPIFromClass(retrofit, UserApi.class);
        Call<List<User>> callList;
        if (role.equals("manager")) {
            callList = userApi.getAllUserInTeam(userId);
        } else {
            callList = userApi.getAllUserForAdmin();
        }
        callList.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.code() == 200) {
                    for (User result: response.body()) {
                        dataUser.add(result.getName() + " - " + result.getId());
                    }
                    ArrayAdapter<String> userAdapter =
                            new ArrayAdapter<>(FilterByUserIdActivity.this, android.R.layout.simple_spinner_item, dataUser);
                    userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spUserId.setAdapter(userAdapter);
                    spUserId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            FilterByUserIdActivity.this.selectedUserId = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Toast.makeText(FilterByUserIdActivity.this, "Must user id for searching", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(FilterByUserIdActivity.this, "Cannot get list user name", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(FilterByUserIdActivity.this, "Have error when connect to backend", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        txtFrom = findViewById(R.id.txtUserHistoryFrom);
        txtTo = findViewById(R.id.txtUserHistoryTo);
        listHistory = findViewById(R.id.listHistoryByUserId);
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

    public void clickToSearchByUserId(View view) {
        if (searchFrom == null) {
            Toast.makeText(this, "You must choose search date from", Toast.LENGTH_SHORT).show();
            return;
        }
        if (searchTo == null) {
            Toast.makeText(this, "You must choose search date to", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedUserId == null) {
            Toast.makeText(this, "You must choose user to search", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedStatus == null) {
            Toast.makeText(this, "You must choose status to search", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] tmp = selectedUserId.split(" - ");
        final Long userId = Long.parseLong(tmp[1]);
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<List<Task>> call = taskApi.getUserHistory(userId, searchFrom, searchTo, selectedStatus);
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.code() == 200) {
                    if (response.body().size() != 0) {
                        TaskAdapter adapter = new TaskAdapter();
                        adapter.setTaskList(response.body());
                        listHistory.setAdapter(adapter);
                        listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Task dto = (Task) listHistory.getItemAtPosition(position);
                                Intent intentDetail = new Intent(FilterByUserIdActivity.this, HistoryTaskDetailActivity.class);
                                intentDetail.putExtra("DTO", dto);
                                intentDetail.putExtra("role", role);
                                intentDetail.putExtra("user_id", managerId);
                                startActivity(intentDetail);
                            }
                        });
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterByUserIdActivity.this);
                        alertDialog.setTitle("Message");
                        alertDialog.setMessage("You don't have any history task yet");
                        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                    }
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterByUserIdActivity.this);
                    alertDialog.setTitle("Message");
                    alertDialog.setMessage("You don't have any history task yet");
                    alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(FilterByUserIdActivity.this);
                alertDialog.setTitle("Message");
                alertDialog.setMessage("You don't have any history task yet");
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
