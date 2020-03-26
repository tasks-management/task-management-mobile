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

import java.text.SimpleDateFormat;
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
            txtEndDate, txtCreator, txtError;
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
        txtError = findViewById(R.id.txtError);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String start = "YYYY/MM/DD", end = "YYYY/MM/DD";
        try {
            start = sdf.format(dto.getStartDate());
            end = sdf.format(dto.getEndDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtStartDate.setText(start);
        txtEndDate.setText(end);
        spHandler = findViewById(R.id.spinner_handler_id_reassign);
        if (message.equals("modify")) {
            if (!start.contains("DD")) {
                try {
                    searchFrom = sdf.parse(start);
                } catch (Exception e) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dto.getStartDate());
                    searchFrom = calendar.getTime();
                }
            }
            if (!end.contains("DD")) {
                try {
                    searchTo = sdf.parse(start);
                } catch (Exception e) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dto.getEndDate());
                    searchTo = calendar.getTime();
                }
            }
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
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecreateTaskActivity.this);
                        alertDialog.setTitle("Error Message");
                        alertDialog.setMessage("Can not get user in handler");
                        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                    }
                });
            } else if (role.equals("admin")){
                callList = userApi.getAllUserForAdmin();
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
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecreateTaskActivity.this);
                        alertDialog.setTitle("Error Message");
                        alertDialog.setMessage("Can not get user in handler");
                        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                    }
                });
            } else {
                List<String> dataSrcHandler = new ArrayList<>();
                dataSrcHandler.add("Yourself - " + userId);
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
        String s = txtStartDate.getText() + "";
        Calendar cal = Calendar.getInstance();
        int day, month, year;
        if (!txtStartDate.getText().toString().contains("DD")) {
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
                txtEndDate.setText(year +"/" + (month+1) + "/" + day);
                cal.set(year, month, day);
                searchTo = cal.getTime();
            }
        };
        String s = txtEndDate.getText() + "";
        Calendar cal = Calendar.getInstance();
        int day, month, year;
        if (!txtEndDate.getText().toString().contains("DD")) {
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

    public void clickToRecreateOrModifyTask(View view) {
        if (edtTaskName.getText().toString().length() == 0) {
            txtError.setText("Task name cannot be null");
            return;
        }
        if (edtTaskDescription.getText().toString().length() == 0) {
            txtError.setText("Task description cannot be null");
            return;
        }
        if (edtProcess.getText().toString().length() == 0) {
            txtError.setText("Task process content cannot be null");
            return;
        }
        if (searchFrom == null) {
            txtError.setText("This start date is old, please choose new one");
            return;
        }
        if (searchTo == null) {
            txtError.setText("This end date is old, please choose new one");
            return;
        }
        if (searchTo.before(searchFrom)) {
            txtError.setText("End date cannot before start date");
            return;
        }
        if (searchFrom.before(new Date())) {
            txtError.setText("Start date cannot before from today");
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
            ModifyTaskDto modifyTaskDto = new ModifyTaskDto();
            Long taskId = dto.getId();
            modifyTaskDto.setName(name);
            modifyTaskDto.setDescription(description);
            modifyTaskDto.setProcess(process);
            modifyTaskDto.setStartDate(searchFrom.toString());
            modifyTaskDto.setEndDate(searchTo.toString());
            modifyTaskDto.setStatus("IN PROGRESS");
            Call<Task> taskCall = taskApi.modifyUserPendingTask(taskId, modifyTaskDto);
            taskCall.enqueue(new Callback<Task>() {
                @Override
                public void onResponse(Call<Task> call, Response<Task> response) {
                    if (response.code() == 200) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecreateTaskActivity.this);
                        alertDialog.setTitle("Message");
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage("Modified task successfully");
                        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RecreateTaskActivity.this.finish();
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
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage("Recreate task successfully");
                        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RecreateTaskActivity.this.finish();
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
    }
}
