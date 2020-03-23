package com.duongll.succotask.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.duongll.succotask.R;
import com.duongll.succotask.api.TeamApi;
import com.duongll.succotask.api.UserApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.config.AppConfig;
import com.duongll.succotask.dto.UserDto;
import com.duongll.succotask.entity.Team;
import com.duongll.succotask.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateUserActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword, edtConfirmPassword, edtName;
    private Spinner spTeamName;
    private String selectedTeamName;
    private TextView txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TeamApi teamApi = APIConfig.getAPIFromClass(retrofit, TeamApi.class);
        Call<List<Team>> teamCall = teamApi.listAllTeam();
        txtError = findViewById(R.id.txtError);
        teamCall.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                List<Team> result = response.body();
                CreateUserActivity.this.spTeamName = findViewById(R.id.spinner_team_create);
                List<String> dataSrc = new ArrayList<>();
                for (Team t: result) {
                    dataSrc.add(t.getId() + " - " + t.getName());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(CreateUserActivity.this, android.R.layout.simple_spinner_item, dataSrc);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTeamName.setAdapter(dataAdapter);
                spTeamName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        CreateUserActivity.this.selectedTeamName = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(CreateUserActivity.this, "Must choose team for user", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {

            }
        });
    }

    public void clickToCreateNewUser(View view) {
        edtUsername = findViewById(R.id.edtUsernameUserCreate);
        if (edtUsername.getText().toString() == null || edtUsername.getText().toString().length() == 0) {
            txtError.setText("Username can't be empty");
            return;
        }
        edtPassword = findViewById(R.id.edtPasswordUserCreate);
        if (edtPassword.getText().toString() == null || edtPassword.getText().toString().length() == 0) {
            txtError.setText("Password can't be empty");
            return;
        }
        edtConfirmPassword = findViewById(R.id.edtConfirmUserCreate);
        if (edtConfirmPassword.getText().toString() == null || edtConfirmPassword.getText().toString().length() == 0) {
            txtError.setText("Confirm password can't be empty");
            return;
        }
        if (!edtConfirmPassword.getText().toString().equals(edtPassword.getText().toString())){
            txtError.setText("Confirm password doesn't match password");
            return;
        }
        edtName = findViewById(R.id.edtNameUserCreate);
        if (edtName.getText().toString() == null || edtName.getText().toString().length() == 0) {
            txtError.setText("Name can't be empty");
            return;
        }
        String id = selectedTeamName.split(" - ")[0];
        String fullName = edtName.getText().toString();
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        UserDto user = new UserDto(username, password, fullName, id);
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        UserApi userApi = APIConfig.getAPIFromClass(retrofit, UserApi.class);
        Call<User> userCall = userApi.createNewUser(user);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateUserActivity.this);
                    alertDialog.setTitle("Message");
                    alertDialog.setMessage("You have create user successfully");
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CreateUserActivity.this.finish();
                        }
                    });
                    alertDialog.show();
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateUserActivity.this);
                    alertDialog.setTitle("Error Message");
                    alertDialog.setMessage("You have create user failed");
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateUserActivity.this);
                alertDialog.setTitle("Error Message");
                alertDialog.setMessage("You have create user failed");
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
        finish();
    }
}
