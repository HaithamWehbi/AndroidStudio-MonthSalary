package month.salary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class AddWorkDay extends AppCompatActivity {

    EditText salaryHour,workHours;
    Button save;

    String strTemp1,strTemp2;
    float temp1 = 0,temp2 = 0,tempToAdd1,tempToAdd2;

    DataBaseHelper dataBaseHelper;

    boolean flag = false;

    SharedPreferences sharedPreferences;

    MainActivity mainActivity;

    AdView adView,adView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work_day);

        dataBaseHelper = new DataBaseHelper(this);
        Cursor cursor = dataBaseHelper.getData();

        sharedPreferences = getSharedPreferences("sec", MODE_PRIVATE);



        if (sharedPreferences.getBoolean("logged", false) && MainActivity.sharedPreferences.getBoolean("logged",false)) {
            flag = true;
        }


        salaryHour = findViewById(R.id.fillHourSalaryTxt);
        workHours = findViewById(R.id.fillWorkingHoursTxt);
        save = findViewById(R.id.saveBtn);
        adView = findViewById(R.id.banner_view_id);
        adView2 = findViewById(R.id.banner_view_id_bottom);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView2.loadAd(adRequest);

        if(flag)
        {
            strTemp1 = cursor.getString(1);
            strTemp2 = cursor.getString(2);

            temp1 = Float.valueOf(strTemp1);
            temp2 = Float.valueOf(strTemp2);

            salaryHour.setText(strTemp2);
        }
        else
        {
            temp1 = 0;
            temp2 = 0;
            sharedPreferences.edit().putBoolean("logged", true).apply();
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fStr1,fStr2;
                float f1,f2;


                strTemp1 = workHours.getText().toString();
                strTemp2 = salaryHour.getText().toString();

                tempToAdd1 = Float.valueOf(strTemp1);
                tempToAdd2 = Float.valueOf(strTemp2);

                f1 = tempToAdd1 + temp1;
                f2 = tempToAdd2;

                fStr1 = String.valueOf(f1);
                fStr2 = String.valueOf(f2);

                dataBaseHelper.clearDatabase();
                long res = dataBaseHelper.addData(fStr1,fStr2);

                if(res > 0)
                {
                    Toast.makeText(AddWorkDay.this,"Info Updated",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddWorkDay.this,MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(AddWorkDay.this,"Error Updating Info",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
