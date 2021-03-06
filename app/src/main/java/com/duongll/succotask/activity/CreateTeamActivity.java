package com.duongll.succotask.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.duongll.succotask.R;
import com.duongll.succotask.api.UserApi;
import com.duongll.succotask.config.APIConfig;
import com.duongll.succotask.config.AppConfig;
import com.duongll.succotask.dto.ManagerDto;
import com.duongll.succotask.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateTeamActivity extends AppCompatActivity {

    private EditText edtTeamName, edtUsername, edtPassword, edtPasswordConfirm, edtName;
    private TextView txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        txtError = findViewById(R.id.txtError);
    }


    public void clickToCreateNewTeam(View view) {
        edtTeamName = findViewById(R.id.edtTeamNameCreate);
        if (edtTeamName.getText().toString() == null || edtTeamName.getText().toString().length() == 0) {
            txtError.setText("Team name can't be empty");
            return;
        }
        edtUsername = findViewById(R.id.edtUsernameCreateManager);
        if (edtUsername.getText().toString() == null || edtUsername.getText().toString().length() == 0) {
            txtError.setText("Username can't be empty");
            return;
        }
        edtPassword = findViewById(R.id.edtPasswordCreateManager);
        if (edtPassword.getText().toString() == null || edtPassword.getText().toString().length() == 0) {
            txtError.setText("Password can't be empty");
            return;
        }
        edtPasswordConfirm = findViewById(R.id.edtConfirmCreateManager);
        if (edtPasswordConfirm.getText().toString() == null || edtPasswordConfirm.getText().toString().length() == 0) {
            txtError.setText("Confirm password can't be empty");
            return;
        }
        if (!edtPasswordConfirm.getText().toString().equals(edtPassword.getText().toString())){
            txtError.setText("Confirm password doesn't match password");
            return;
        }
        edtName = findViewById(R.id.edtNameCreateManager);
        if (edtName.getText().toString() == null || edtName.getText().toString().length() == 0) {
            txtError.setText("Name can't be empty");
            return;
        }
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        UserApi userApi = APIConfig.getAPIFromClass(retrofit, UserApi.class);
        String teamName = edtTeamName.getText().toString();
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        String fullName = edtName.getText().toString();
        ManagerDto user = new ManagerDto(username, password, fullName, teamName);
        Call<User> userCall = userApi.createNewManager(user);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTeamActivity.this);
                    alertDialog.setTitle("Message");
                    alertDialog.setMessage("You have create team and manager of team successfully");
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CreateTeamActivity.this.finish();
                        }
                    });
                    alertDialog.show();
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTeamActivity.this);
                    alertDialog.setTitle("Error Message");
                    alertDialog.setMessage("You have create user failed");
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CreateTeamActivity.this.finish();
                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTeamActivity.this);
                alertDialog.setTitle("Error Message");
                alertDialog.setMessage("You have create team and manager of team failed");
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
    }
}
