package com.duongll.succotask.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.duongll.succotask.R;

public class ManageTaskActivity extends AppCompatActivity {

    private Long userId;
    private String role;
    private Button btnApprove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_task);
        Intent intent = this.getIntent();
        userId = intent.getLongExtra("user_id", new Long(0));
        role = intent.getStringExtra("role");
        btnApprove = findViewById(R.id.btnApproveTask);
        if (role.equals("admin")) {
            btnApprove.setEnabled(false);
            btnApprove.setVisibility(View.GONE);
        }
    }

    public void clickToApproveTaskForUserPage(View view) {
        Intent intent;
        if (role.equals("admin")) {
            intent = new Intent(this, ListSubmitTaskForAdminActivity.class);
        } else {
            intent = new Intent(this, ListApproveTaskRequestActivity.class);
        }
        intent.putExtra("user_id", userId);
        intent.putExtra("role", role);
        startActivity(intent);
    }

    public void clickToCreateTaskPage(View view) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("role", role);
        startActivity(intent);
    }

    public void clickToGetPendingTask(View view) {
        Intent intent = new Intent(this, YourTaskRequestActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }
}
