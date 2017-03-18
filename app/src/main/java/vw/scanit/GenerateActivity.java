package vw.scanit;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

public class GenerateActivity extends AppCompatActivity implements LocationListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient client;
    Location location;
    ImageView qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        client.connect();


         qrImage = (ImageView) findViewById(R.id.imageView1);
            qrImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = findViewById(R.id.imageView1);
                    view.setDrawingCacheEnabled(true);
                    Bitmap qrbitmap = view.getDrawingCache();

                    if(qrbitmap == null)
                    {
                        Toast.makeText(getApplicationContext(),"Something went wrong! Try Again", Toast.LENGTH_LONG).show();

                    }

                    else {
                        File r = Environment.getExternalStorageDirectory();
                        File savepath = new File(r.getAbsolutePath(),"/DCIM/Camera/"+ System.currentTimeMillis()+".jpg");
                        try {
                            savepath.createNewFile();
                            FileOutputStream fos = new FileOutputStream(savepath);
                            qrbitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("image/*");

                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(savepath));
                        startActivity(Intent.createChooser(shareIntent,"Share via"));

                    }


                }
            });


        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);

    }

    public void onClick(View v) {

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        QRCodeEncoder qrCodeEncoder;

        switch (v.getId()) {
            case R.id.button1:
                EditText qrInput = (EditText) findViewById(R.id.qrInput);
                String qrInputText = qrInput.getText().toString();
                Log.v("Tag", qrInputText);


                qrCodeEncoder = new QRCodeEncoder(qrInputText,
                        null,
                        Contents.Type.TEXT,
                        BarcodeFormat.QR_CODE.toString(),
                        smallerDimension);
                try {
                    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();

                    qrImage.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }


                break;

            // More buttons go here (if any) ...
            case R.id.button2:
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permissions not granted!", Toast.LENGTH_LONG).show();
                    return;
                }

                location= LocationServices.FusedLocationApi.getLastLocation(client);

                if (location == null) {
                    Toast.makeText(getApplicationContext(), "Location not Available!", Toast.LENGTH_LONG).show();
                } else {
                    Bundle b = new Bundle();
                    b.putFloat("LAT", (float) location.getLatitude());
                    b.putFloat("LONG", (float) location.getLongitude());


                    //Encode with a QR Code image
                    qrCodeEncoder = new QRCodeEncoder(null,
                            b,
                            Contents.Type.LOCATION,
                            BarcodeFormat.QR_CODE.toString(),
                            smallerDimension);
                    try {
                        Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();

                        qrImage.setImageBitmap(bitmap);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }


                break;

        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(getApplicationContext(),"Connected!",Toast.LENGTH_LONG).show();
        LocationRequest request = LocationRequest.create();
        request.setNumUpdates(2).setInterval(1000);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

      //  this.location = location;

    }
}

