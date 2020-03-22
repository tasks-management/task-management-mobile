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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.duongll.succotask.R;
import com.duongll.succotask.api.TaskApi;
import com.duongll.succotask.api.UserApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.dto.CreateTaskDto;
import com.duongll.succotask.dto.ModifyTaskDto;
import com.duongll.succotask.entity.Task;
import com.duongll.succotask.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecreateTaskActivity extends AppCompatActivity {

    private Date searchFrom, searchTo;
    private Button btnRecreateOrMofidy;
    private TextView txtTilte, txtStartDate,
            txtEndDate, txtCreator;
    private EditText edtTaskName, edtTaskDescription, edtProcess;
    private Spinner spHandler;
    private Long userId;
    private Task dto;
    private String message, selectedHandler, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recreate_task);
        Intent intent = this.getIntent();
        dto = (Task) intent.getSerializableExtra("DTO");
        role = intent.getStringExtra("role");
        btnRecreateOrMofidy = findViewById(R.id.btnFunction);
        txtTilte = findViewById(R.id.txtTilteActivity);
        message = intent.getStringExtra("message");
        userId = intent.getLongExtra("user_id", new Long(0));
        edtTaskName = findViewById(R.id.txtTaskNameReassign);
        edtTaskName.setText(dto.getName());
        edtTaskDescription = findViewById(R.id.txtTaskDescriptionReassign);
        edtTaskDescription.setText(dto.getDescription());
        edtProcess = findViewById(R.id.txtTaskProcessReassgin);
        edtProcess.setText(dto.getContentProcess());
        txtStartDate = findViewById(R.id.txtTaskStartDateReassign);
        txtEndDate = findViewById(R.id.txtTaskEndDateReassign);
        txtCreator = findViewById(R.id.txtTaskCreatorReassign);
        String[] strTmpStart = dto.getStartDate().toString().split(" ");
        String dayFrom = strTmpStart[2];
        String monthFrom = strTmpStart[1];
        String yearFrom = strTmpStart[5];
        String[] strTmpEnd = dto.getEndDate().toString().split(" ");
        String dayEnd = strTmpEnd[2];
        String monthEnd = strTmpEnd[1];
        String yearEnd = strTmpEnd[5];
        String start = yearFrom + "/" +monthFrom + "/" + dayFrom;
        txtStartDate.setText(start);
        String end = yearEnd + "/" + monthEnd + "/" + dayEnd;
        txtEndDate.setText(end);
        spHandler = findViewById(R.id.spinner_handler_id_reassign);
        if (message.equals("modify")) {
            txtTilte.setText("Modify Task For User");
            btnRecreateOrMofidy.setText("Modify Task");
            txtCreator.setText(dto.getCreatorId().getId() + "");
            List<String> dataSrcHandler = new ArrayList<>();
            dataSrcHandler.add(dto.getHandlerId().getName() + " - " + dto.getHandlerId().getId());
            spHandler.setEnabled(false);
            ArrayAdapter<String> dataAdapter =
                    new ArrayAdapter<>(RecreateTaskActivity.this, android.R.layout.simple_spinner_item, dataSrcHandler);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spHandler.setAdapter(dataAdapter);
            spHandler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    RecreateTaskActivity.this.selectedHandler = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(RecreateTaskActivity.this, "Must user id for searching", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            txtCreator.setText(userId + "");
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
                    if (response.code() == 200) {
                        List<String> dataSrcHandler = new ArrayList<>();
                        for(User result: response.body()) {
                            dataSrcHandler.add(result.getName() + " - " + result.getId());
                        }
                        ArrayAdapter<String> dataAdapter =
                                new ArrayAdapter<>(RecreateTaskActivity.this, android.R.layout.simple_spinner_item, dataSrcHandler);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spHandler.setAdapter(dataAdapter);
                        spHandler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                RecreateTaskActivity.this.selectedHandler = parent.getItemAtPosition(position).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Toast.makeText(RecreateTaskActivity.this, "Must user id for searching", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Toast.makeText(RecreateTaskActivity.this, "Have error when connect to server", Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        }
    }

    public void clickToGetFromDate(View view) {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                txtStartDate.setText(year +"/" + (month+1) + "/" + day);
                cal.set(year, month, day);
                searchFrom = cal.getTime();
            }
        };
        Calendar cal = Calendar.getInstance();
        int day, month, year;
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        cal.set(year, month, day);
        searchFrom = cal.getTime();
        DatePickerDialog pic = new DatePickerDialog(this, callback, year, month, day);
        pic.setTitle("Start Date");
        pic.show();
    }

    public void clickToGetToDate(View view) {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                txtEndDate.setText(year +"/" + (month+1) + "/" + day);
                cal.set(year, month, day);
                searchTo = cal.getTime();
            }
        };
        Calendar cal = Calendar.getInstance();
        int day, month, year;
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        cal.set(year, month, day);
        searchTo = cal.getTime();
        DatePickerDialog pic = new DatePickerDialog(this, callback, year, month, day);
        pic.setTitle("End Date");
        pic.show();
    }

    public void clickToRecreateOrModifyTask(View view) {
        if (edtTaskName.getText().toString().length() == 0) {
            Toast.makeText(this, "Task name cannot be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edtTaskDescription.getText().toString().length() == 0) {
            Toast.makeText(this, "Task description cannot be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edtProcess.getText().toString().length() == 0) {
            Toast.makeText(this, "Task process content cannot be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (searchFrom == null) {
            Toast.makeText(this, "Start date cannot be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (searchTo == null) {
            Toast.makeText(this, "End date cannot be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (searchTo.before(searchFrom)) {
            Toast.makeText(this, "End date cannot before start date", Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        String name = edtTaskName.getText().toString();
        String description = edtTaskDescription.getText().toString();
        String process = edtProcess.getText().toString();
        Long creator = Long.parseLong(txtCreator.getText().toString());
        String[] strTmp = selectedHandler.split(" - ");
        Long handler = Long.parseLong(strTmp[1]);
        if (message.equals("modify")) {
            ModifyTaskDto dto = new ModifyTaskDto();
            dto.setName(name);
            dto.setDescription(description);
            dto.setProcess(process);
            dto.setStartDate(searchFrom.toString());
            dto.setEndDate(searchTo.toString());
            dto.setStatus("IN PROGRESS");
            Call<Task> taskCall = taskApi.modifyUserPendingTask(userId, dto);
            taskCall.enqueue(new Callback<Task>() {
                @Override
                public void onResponse(Call<Task> call, Response<Task> response) {
                    if (response.code() == 200) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecreateTaskActivity.this);
                        alertDialog.setTitle("Message");
                        alertDialog.setMessage("Modified task successfully");
                        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<Task> call, Throwable t) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecreateTaskActivity.this);
                    alertDialog.setTitle("Error Message");
                    alertDialog.setMessage("Modified task failed");
                    alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
            });
        } else {
            CreateTaskDto dto;
            if (role.equals("user")) {
                dto = new CreateTaskDto(name, description, process, "PENDING", searchFrom.toString(), searchTo.toString(), creator, handler);
            } else {
                dto = new CreateTaskDto(name, description, process, "IN PROGRESS", searchFrom.toString(), searchTo.toString(), creator, handler);
            }
            Call<CreateTaskDto> taskCall = taskApi.createNewTask(dto);
            taskCall.enqueue(new Callback<CreateTaskDto>() {
                @Override
                public void onResponse(Call<CreateTaskDto> call, Response<CreateTaskDto> response) {
                    if (response.code() == 200) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecreateTaskActivity.this);
                        alertDialog.setTitle("Message");
                        alertDialog.setMessage("Recreate task successfully");
                        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<CreateTaskDto> call, Throwable t) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecreateTaskActivity.this);
                    alertDialog.setTitle("Error Message");
                    alertDialog.setMessage("Recreate task failed");
                    alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
            });
        }
        finish();
    }
}
