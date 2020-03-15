package duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import duongll.succotask.R;

public class ManageTaskActivity extends AppCompatActivity {

    private Button btnFindUser;
    private Long userId;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_task);
        Intent intent = this.getIntent();
        role = intent.getStringExtra("role");
        if (role.equals("admin")){
            btnFindUser = findViewById(R.id.btnFindUser);
            btnFindUser.setVisibility(View.INVISIBLE);
            btnFindUser.setEnabled(false);
        }
        userId = intent.getLongExtra("user_id", new Long(0));
    }

    public void clickToApproveTaskForUserPage(View view) {
        Intent intent = new Intent(this, ListApproveTaskRequestActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

    public void clickToCreateTaskPage(View view) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("role", role);
        startActivity(intent);
    }

    public void clickToFindUserPage(View view) {
    }
}
