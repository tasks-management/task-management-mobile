package duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import duongll.succotask.R;

public class ManageUserActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);
    }

    public void clickToCreateTeamPage(View view) {
        Intent intent = new Intent(this, CreateTeamActivity.class);
        startActivity(intent);
    }

    public void clickToCreateUserPage(View view) {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

    public void clickToFindUser(View view) {

    }
}
