package br.com.defaultpack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vrutil.service.QRCodeReadService;

import br.com.tfleet.tfleetqrcode.R;


public class MainActivity extends Activity{

    private Button btnStart, btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeButtons();
    }


    private void initializeButtons() {

        /*******************************/
        /*******************************/
        /*********BUTTON STOP***********/
        /*******************************/
        /*******************************/
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, QRCodeReadService.class));
            }
        });

        /*******************************/
        /*******************************/
        /*********BUTTON START***********/
        /*******************************/
        /*******************************/
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PARAMETERS
                /*
                Set this to CHANGE CAMERA FACING
                Default is CAMERA_FACING_BACK
                Options: CameraSource.CAMERA_FACING_FRONT - CameraSource.CAMERA_FACING_BACK
                QRCodeReadService.CAMERA_FACING_BACK = CameraSource.CAMERA_FACING_FRONT;
                */

                /*
                Set this to never stop the CAMERA
                Default is FALSE. If True, camera's nevers stops. If false instead
                QRCodeReadService.SERVICE_NEVER_STOP = Boolean.TRUE;
                */

                /*
                * Delay to read - Default is 0 = anytime
                * */
                QRCodeReadService.READ_TIME = 500;

                Intent intent = new Intent(MainActivity.this, QRCodeReadService.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);

                //TO HIDE actvity
                //finish();
            }
        });
    }

}
