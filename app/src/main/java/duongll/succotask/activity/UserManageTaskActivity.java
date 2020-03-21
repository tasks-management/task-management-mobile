package duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import duongll.succotask.R;

public class UserManageTaskActivity extends AppCompatActivity {

    private String role;
    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage_task);
        Intent intent = this.getIntent();
        role = intent.getStringExtra("role");
        userId = intent.getLongExtra("user_id", new Long(0));
    }

    public void clickToCreateTaskPage(View view) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("role", role);
        startActivity(intent);
    }

    public void clickToTaskRequestPage(View view) {
        Intent intent = new Intent(this, YourTaskRequestActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

}
