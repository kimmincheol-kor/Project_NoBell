package com.nobell.owner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nobell.owner.R;

public class OfficeActivity extends AppCompatActivity {

    int backButtonCount;

    private Button btn_logout;

    private SharedPreferences appData;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);

        // Get View Object
        btn_logout = (Button)findViewById(R.id.btn_logout);

        // Access to SharedPreference
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        editor = appData.edit();

        // Click Event : Logout Button
        btn_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Reset SharedPreference
                editor.putBoolean("AutoLogin", false);
                editor.apply();

                // Move to MainActivity
                Intent intent;
                intent = new Intent(OfficeActivity.this, com.nobell.owner.activity.MainActivity.class);
                startActivity(intent);

            }
        });
        // End of Logout
    }

    // Exit App Function by Press Back
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }


}
