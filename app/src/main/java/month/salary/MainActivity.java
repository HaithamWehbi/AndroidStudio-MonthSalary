package month.salary;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MainActivity extends AppCompatActivity {

    TextView strToSalary,salary,strToHourSalary,hourSalary,strToTotalHours,totalHours;
    Button addDay,reset;

    DataBaseHelper dataBaseHelper;
    Cursor cursor;

    String temp1,temp2,temp3;
    float fTemp1,fTemp2,fSalary;

    public static SharedPreferences sharedPreferences;

    boolean flag = false;

    InterstitialAd ad;

    private int countAd = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBaseHelper = new DataBaseHelper(this);
        cursor = dataBaseHelper.getData();

        sharedPreferences = getSharedPreferences("check", MODE_PRIVATE);
        //sharedPreferences.edit().clear().commit();

        if (sharedPreferences.getBoolean("logged", false)) {
            flag = true;
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });


        ad = new InterstitialAd(this);
        ad.setAdUnitId(getString(R.string.fullScreenAd_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        ad.loadAd(adRequest);



        strToSalary = (TextView) findViewById(R.id.strToSalaryTxt);
        salary = (TextView) findViewById(R.id.salaryTxt);
        strToHourSalary = (TextView) findViewById(R.id.strToHourSalaryTxt);
        hourSalary = (TextView) findViewById(R.id.hourSalaryTxt);
        strToTotalHours = (TextView) findViewById(R.id.strToTotalHoursTxt);
        totalHours = (TextView) findViewById(R.id.totalHoursTxt);
        addDay = (Button) findViewById(R.id.addWorkDayBtn);
        reset = (Button) findViewById(R.id.resetBtn);



        ad.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {

                //the app will load the ad only once cuz its only on the onCreate method
                //if i want to add unlimited ads on this button i have to add the following line
                //ad.loadAd(new AdRequest.Builder().build());
                //to load another ad on each ad close
                //if i want to limit the ad numbers i need rnd flag and a condition (%2 == 0)
                //for example for doing 1 yes 2 no and so on
                //this condition will be written in our case right before the click on button to add day
                ad.loadAd(new AdRequest.Builder().build());
                Intent intent = new Intent(MainActivity.this,AddWorkDay.class);
                startActivity(intent);
            }
        });


        addDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ad.isLoaded())
                {
                    ad.show();
                }
                else
                {
                    Intent intent = new Intent(MainActivity.this,AddWorkDay.class);
                    startActivity(intent);
                }

                /*if(countAd % 2 == 0)
                {
                    if (ad.isLoaded())
                    {
                        ad.show();
                    }
                    else
                    {
                        Intent intent = new Intent(MainActivity.this,AddWorkDay.class);
                        startActivity(intent);
                    }
                }
                else
                {
                    Intent intent = new Intent(MainActivity.this,AddWorkDay.class);
                    startActivity(intent);
                }

                countAd++;*/
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseHelper.clearDatabase();
                salary.setText("");
                hourSalary.setText("");
                totalHours.setText("");
                sharedPreferences.edit().putBoolean("logged", false).apply();
                Toast.makeText(MainActivity.this,"Reset Done",Toast.LENGTH_SHORT).show();
                addDay.setClickable(false);
                addDay.setText("Restart Application");
            }
        });


        if(!(flag))
        {
            secFunc();
        }
        else
        {
            myFunc();
        }



    }

    private void secFunc()
    {

        String nS = "0",nS2 = "0";

        dataBaseHelper.clearDatabase();
        long res =  dataBaseHelper.addData(nS,nS2);
        if(res > 0)
        {
            sharedPreferences.edit().putBoolean("logged", true).apply();
            Toast.makeText(MainActivity.this,"Welcome!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(MainActivity.this,"Restart",Toast.LENGTH_SHORT).show();
        }



    }

    private void myFunc()
    {

        temp1 = cursor.getString(1);
        temp2 = cursor.getString(2);

        fTemp1 = Float.valueOf(temp1);
        fTemp2 = Float.valueOf(temp2);

        fSalary = fTemp1 * fTemp2;
        temp3 = String.valueOf(fSalary);

        salary.setText(temp3);
        hourSalary.setText(temp2);
        totalHours.setText(temp1);
    }
}