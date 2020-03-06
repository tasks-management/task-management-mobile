package duongll.succotask;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import duongll.succotask.config.APIConfig;
import duongll.succotask.entity.User;
import duongll.succotask.api.UserApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickToSubmit(View view) {
        Retrofit retrofit = APIConfig.createRetrofitForAPI();
        UserApi api = APIConfig.getAPIFromClass(retrofit, UserApi.class);
        User user = new User();
        edtUsername = findViewById(R.id.txtUsername);
        edtPassword = findViewById(R.id.txtPassword);
        user.setUsername(edtUsername.getText().toString());
        user.setPassword(edtPassword.getText().toString());
        Call<User> call = api.logInToApp(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    String role = response.body().getRole();
                    String name = response.body().getName();
                    Long teamId = response.body().getTeamId();
                    Intent intent = new Intent(MainActivity.this, IndexActivity.class);
                    intent.putExtra("name", name);
                    if (role.contains("manager")) {
                        intent.putExtra("TeamID", teamId);
                    } else {
                        intent.putExtra("TeamID", teamId);
                    }
                    startActivity(intent);
                } else {
                    System.out.println("Error msg: " + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
        finish();
    }

}
