package duongll.succotask.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import duongll.succotask.R;

public class UserHistoryTaskActivity extends AppCompatActivity {

    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history_task);
        Intent intent = this.getIntent();
        intent.putExtra("user_id", new Long(0));
        BottomNavigationView bottomNavigationView = findViewById(R.id.user_history_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_user_history_date:
                        clickToFilterByDate(null);
                        break;
                    case R.id.navigation_user_history_status:
                        clickToFilterByStatus(null);
                        break;
                }
                return true;
            }
        });
    }

    public void clickToFilterByDate(View view) {
        Intent intent = new Intent(this, FilterByDateActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

    public void clickToFilterByStatus(View view) {
        Intent intent = new Intent(this, FilterByStatusActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

}
