package vw.scanit;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
Button scan,generate;

    TextView welcometv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/helvetica.ttf");

        welcometv = (TextView)findViewById(R.id.welcometv);
        welcometv.setTypeface(font);


        scan = (Button)findViewById(R.id.scanbutton);
        scan.setOnClickListener(this);
        scan.setTypeface(font);


        generate = (Button)findViewById(R.id.generatebutton);
        generate.setOnClickListener(this);
        generate.setTypeface(font);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.scanbutton:
                Intent toscan = new Intent(this,ScanActivity.class);
                startActivity(toscan);
                break;
            case R.id.generatebutton:
                Intent togenerate = new Intent(this,SelectActivity.class);
                startActivity(togenerate);

        }

    }
}
