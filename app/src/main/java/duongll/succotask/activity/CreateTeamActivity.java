package duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import duongll.succotask.R;
import duongll.succotask.api.UserApi;
import duongll.succotask.config.APIConfig;
import duongll.succotask.dto.ManagerDto;
import duongll.succotask.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateTeamActivity extends AppCompatActivity {

    private EditText edtTeamName, edtUsername, edtPassword, edtPasswordConfirm, edtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
    }


    public void clickToCreateNewTeam(View view) {
        edtTeamName = findViewById(R.id.edtTeamNameCreate);
        if (edtTeamName.getText().toString() == null || edtTeamName.getText().toString().length() == 0) {
            Toast.makeText(this, "Team name can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        edtUsername = findViewById(R.id.edtUsernameCreateManager);
        if (edtUsername.getText().toString() == null || edtUsername.getText().toString().length() == 0) {
            Toast.makeText(this, "Username can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        edtPassword = findViewById(R.id.edtPasswordCreateManager);
        if (edtPassword.getText().toString() == null || edtPassword.getText().toString().length() == 0) {
            Toast.makeText(this, "Password can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        edtPasswordConfirm = findViewById(R.id.edtConfirmCreateManager);
        if (edtPasswordConfirm.getText().toString() == null || edtPasswordConfirm.getText().toString().length() == 0) {
            Toast.makeText(this, "Confirm password can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!edtPasswordConfirm.getText().toString().equals(edtPassword.getText().toString())){
            Toast.makeText(this, "Confirm password doesn't match password", Toast.LENGTH_SHORT).show();
            return;
        }
        edtName = findViewById(R.id.edtNameCreateManager);
        if (edtName.getText().toString() == null || edtName.getText().toString().length() == 0) {
            Toast.makeText(this, "Name can't be empty", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CreateTeamActivity.this, "Create successfully", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(CreateTeamActivity.this, "Create failed", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(CreateTeamActivity.this, "Have error when connect to server", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        finish();
    }
}
