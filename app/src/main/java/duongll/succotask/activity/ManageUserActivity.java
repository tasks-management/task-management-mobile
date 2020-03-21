package duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import duongll.succotask.R;

public class ManageUserActivity extends AppCompatActivity {

    private Long userId;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);
        Intent intent = this.getIntent();
        userId = intent.getLongExtra("user_id", new Long(0));
        role = intent.getStringExtra("role");
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
        Intent intent = new Intent(this, ScanQRCodeActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("role", role);
        startActivity(intent);
    }
}
