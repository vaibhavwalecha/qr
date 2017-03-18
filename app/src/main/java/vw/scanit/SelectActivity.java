package vw.scanit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SelectActivity extends AppCompatActivity {

    Toolbar tbar ;
    ListView selectorlv;
    String[] categories = {"Text","URL","Phone","SMS","Location"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        tbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tbar);
        getSupportActionBar().setTitle("Select Category :");

        selectorlv = (ListView)findViewById(R.id.selectorlv);
        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,categories);

        selectorlv.setAdapter(adapter);

        selectorlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),categories[position],Toast.LENGTH_LONG).show();
            }
        });

    }
}
