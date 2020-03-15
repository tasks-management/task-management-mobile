package duongll.succotask.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import duongll.succotask.R;
import duongll.succotask.config.APIConfig;
import duongll.succotask.entity.User;
import duongll.succotask.api.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickToSubmit(View view) {
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        UserApi api = APIConfig.getAPIFromClass(retrofit, UserApi.class);
        User user = new User();
        edtUsername = findViewById(R.id.edtUsername);
        if (edtUsername.getText().toString().length() == 0) {
            Toast.makeText(this, "Username cannot be null", Toast.LENGTH_SHORT).show();
            return;
        }
        edtPassword = findViewById(R.id.edtPassword);
        if (edtPassword.getText().toString().length() == 0) {
            Toast.makeText(this, "Password cannot be null", Toast.LENGTH_SHORT).show();
            return;
        }
        user.setUsername(edtUsername.getText().toString());
        user.setPassword(edtPassword.getText().toString());
        Call<User> call = api.logInToApp(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    String role = response.body().getRole();
                    String name = response.body().getName();
                    Long userId = response.body().getId();
                    Intent intent;
                    if(!role.contains("admin")) {
                        intent = new Intent(MainActivity.this, IndexActivity.class);
                        intent.putExtra("team_id",response.body().getTeamId().getId());
                    } else {
                        intent = new Intent(MainActivity.this, AdminIndexActivity.class);
                    }
                    intent.putExtra("name", name);
                    intent.putExtra("role", role);
                    intent.putExtra("user_id", userId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
