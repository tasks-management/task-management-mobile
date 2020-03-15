package duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import duongll.succotask.R;
import duongll.succotask.api.TaskApi;
import duongll.succotask.api.UserApi;
import duongll.succotask.config.APIConfig;
import duongll.succotask.dto.CreateTaskDto;
import duongll.succotask.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateTaskActivity extends AppCompatActivity {

    private EditText edtTaskName, edtTaskDescription,
            edtTaskProcessContent, edtCreator;

    private TextView txtTaskEndDate, txtTaskStartDate;
    private Date searchTo, searchFrom;
    private String role, selectedHandlerId, status;
    private Long userId;
    private Spinner spHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        edtTaskName = findViewById(R.id.txtTaskNameCreate);
        edtTaskDescription = findViewById(R.id.txtTaskDescriptionCreate);
        edtTaskProcessContent = findViewById(R.id.txtProcessContentCreate);
        txtTaskStartDate = findViewById(R.id.txtTaskStartDateCreate);
        txtTaskEndDate = findViewById(R.id.txtTaskEndDateCreate);
        edtCreator = findViewById(R.id.txtTaskCreatorCreate);
        spHandler = findViewById(R.id.spinner_handler_id_create);
        final List<String> dataSrc = new ArrayList<>();
        Intent intent = this.getIntent();
        role = intent.getStringExtra("role");
        userId = intent.getLongExtra("user_id", new Long(0));
        edtCreator.setText(userId + "");
        if (role.equals("user")) {
            status = "PENDING";
            dataSrc.add("Yourself - " + userId);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(CreateTaskActivity.this, android.R.layout.simple_spinner_item, dataSrc);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spHandler.setAdapter(dataAdapter);
            spHandler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    CreateTaskActivity.this.selectedHandlerId = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            status = "IN PROGRESS";
            Retrofit retrofit = APIConfig.createRetrofitForAPI();
            UserApi userApi = APIConfig.getAPIFromClass(retrofit, UserApi.class);
            Call<List<User>> callList = userApi.getAllUserInTeam(userId);
            callList.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (response.code() == 200) {
                        for(User result: response.body()) {
                            dataSrc.add(result.getName() + " - " + result.getId());
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(CreateTaskActivity.this, android.R.layout.simple_spinner_item, dataSrc);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spHandler.setAdapter(dataAdapter);
                        spHandler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                CreateTaskActivity.this.selectedHandlerId = parent.getItemAtPosition(position).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Toast.makeText(CreateTaskActivity.this, "Have error when connect to server", Toast.LENGTH_SHORT).show();
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
                txtTaskStartDate.setText(year +"/" + (month+1) + "/" + day);
                cal.set(year, month, day);
                searchFrom = cal.getTime();
            }
        };
        String s = txtTaskStartDate.getText() + "";
        Calendar cal = Calendar.getInstance();
        int day, month, year;
        if (!txtTaskStartDate.getText().toString().contains("DD")) {
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
                txtTaskEndDate.setText(year +"/" + (month+1) + "/" + day);
                cal.set(year, month, day);
                searchTo = cal.getTime();
            }
        };
        String s = txtTaskEndDate.getText() + "";
        Calendar cal = Calendar.getInstance();
        int day, month, year;
        if (!txtTaskEndDate.getText().toString().contains("DD")) {
            String strArrtmp[] = s.split("/");
            day = Integer.parseInt(strArrtmp[2]);
            month = Integer.parseInt(strArrtmp[1]) - 1;
            year = Integer.parseInt(strArrtmp[0]);
        }else {
            day = cal.get(Calendar.DAY_OF_MONTH);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);
        }
        DatePickerDialog pic = new DatePickerDialog(this, callback, year, month, day);
        pic.setTitle("End Date");
        pic.show();
    }

    public void clickToCreateTask(View view) {
        if (edtTaskName.getText().toString().length() == 0) {
            Toast.makeText(this, "Task name cannot be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edtTaskDescription.getText().toString().length() == 0) {
            Toast.makeText(this, "Task description cannot be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edtTaskProcessContent.getText().toString().length() == 0) {
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
        String name = edtTaskName.getText().toString();
        String description = edtTaskDescription.getText().toString();
        String process = edtTaskProcessContent.getText().toString();
        Long creator = Long.parseLong(edtCreator.getText().toString());
        String[] strTmp = selectedHandlerId.split(" - ");
        Long handler = Long.parseLong(strTmp[1]);
        CreateTaskDto taskDto = new CreateTaskDto(name, description, process,status, searchFrom.toString(), searchTo.toString(), creator, handler);
        System.out.println("DTO From: " + taskDto.getStartDate());
        System.out.println("DTO To: " + taskDto.getEndDate());
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TaskApi taskApi = APIConfig.getAPIFromClass(retrofit, TaskApi.class);
        Call<CreateTaskDto> callTask = taskApi.createNewTask(taskDto);
        callTask.enqueue(new Callback<CreateTaskDto>() {
            @Override
            public void onResponse(Call<CreateTaskDto> call, Response<CreateTaskDto> response) {
                if (response.code() == 200) {
                    Toast.makeText(CreateTaskActivity.this, "Create successfully", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(CreateTaskActivity.this, "Create failed", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<CreateTaskDto> call, Throwable t) {
                Toast.makeText(CreateTaskActivity.this, "Have error when connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}
