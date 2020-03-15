package duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import duongll.succotask.R;
import duongll.succotask.api.TeamApi;
import duongll.succotask.api.UserApi;
import duongll.succotask.config.APIConfig;
import duongll.succotask.dto.ManagerDto;
import duongll.succotask.dto.UserDto;
import duongll.succotask.entity.Team;
import duongll.succotask.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateUserActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword, edtConfirmPassword, edtName;
    private Spinner spTeamName;
    private String selectedTeamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        TeamApi teamApi = APIConfig.getAPIFromClass(retrofit, TeamApi.class);
        Call<List<Team>> teamCall = teamApi.listAllTeam();
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
            Toast.makeText(this, "Username can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        edtPassword = findViewById(R.id.edtPasswordUserCreate);
        if (edtPassword.getText().toString() == null || edtPassword.getText().toString().length() == 0) {
            Toast.makeText(this, "Password can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        edtConfirmPassword = findViewById(R.id.edtConfirmUserCreate);
        if (edtConfirmPassword.getText().toString() == null || edtConfirmPassword.getText().toString().length() == 0) {
            Toast.makeText(this, "Confirm password can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!edtConfirmPassword.getText().toString().equals(edtPassword.getText().toString())){
            Toast.makeText(this, "Confirm password doesn't match password", Toast.LENGTH_SHORT).show();
            return;
        }
        edtName = findViewById(R.id.edtNameUserCreate);
        if (edtName.getText().toString() == null || edtName.getText().toString().length() == 0) {
            Toast.makeText(this, "Name can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String id = selectedTeamName.split(" - ")[0];
//        Team teamOwner = new Team();
//        teamOwner.setId(id);
//        teamOwner.setName(selectedTeamName.split("-")[1]);
//        User user = new User();
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
                    Toast.makeText(CreateUserActivity.this, "Create successfully", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(CreateUserActivity.this, "Create failed", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(CreateUserActivity.this, "Have error when connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        finish();
    }
}
