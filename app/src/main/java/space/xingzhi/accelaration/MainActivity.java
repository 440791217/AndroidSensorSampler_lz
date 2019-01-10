package space.xingzhi.accelaration;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;

import org.zeromq.ZMQ;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager = null;
    private Sensor mAccelerometer = null;
    private Sensor mGyroscope = null;
    private TextView mTextViewX = null;
    private TextView mTextViewY = null;
    private TextView mTextViewZ = null;
    private TextView mTextViewRX = null;
    private TextView mTextViewRY = null;
    private TextView mTextViewRZ = null;
    TextView mTextViewStatus = null;
    private Logger mLoggerAccelerometer = new Logger(this, 2000, "acc");
    private Logger mLoggerGyroscope = new Logger(this, 2000, "rad");

//    private ZMQ.Context mContextAcc = null;
//    private ZMQ.Socket mPublisherAcc = null;
//    private ZMQ.Context mContextGyro = null;
//    private ZMQ.Socket mPublisherGyro = null;

    private MediaPlayer mediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewX = findViewById(R.id.textView_X);
        mTextViewY = findViewById(R.id.textView_Y);
        mTextViewZ = findViewById(R.id.textView_Z);
        mTextViewStatus = findViewById(R.id.textView_status);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (mAccelerometer == null) {
            mTextViewX.setText("No Sensor available");
            mTextViewY.setText("No Sensor available");
            mTextViewZ.setText("No Sensor available");
        }
        else {
            mTextViewX.setText("Sensor available");
            mTextViewY.setText("Sensor available");
            mTextViewZ.setText("Sensor available");
        }
        mSensorManager.registerListener(this, mAccelerometer, 10000);

        mTextViewRX = findViewById(R.id.textView_RX);
        mTextViewRY = findViewById(R.id.textView_RY);
        mTextViewRZ = findViewById(R.id.textView_RZ);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mGyroscope == null) {
            mTextViewRX.setText("No Sensor available");
            mTextViewRY.setText("No Sensor available");
            mTextViewRZ.setText("No Sensor available");
        }
        else {
            mTextViewRX.setText("Sensor available");
            mTextViewRY.setText("Sensor available");
            mTextViewRZ.setText("Sensor available");
        }
        mSensorManager.registerListener(this, mGyroscope, 10000);

//        if (mContextAcc == null) {
//            mContextAcc = ZMQ.context(1);
//            mPublisherAcc = mContextAcc.socket(ZMQ.PUB);
//            mPublisherAcc.bind("tcp://*:5556");
//        }

//        if (mContextGyro == null) {
//            mContextGyro = ZMQ.context(1);
//            mPublisherGyro = mContextGyro.socket(ZMQ.PUB);
//            mPublisherGyro.bind("tcp://*:5557");
//        }

        mediaPlayer = MediaPlayer.create(this, R.raw.ding);

    }

    public boolean isFall(float acc_x, float acc_y, float acc_z)
    {
        double length = Math.sqrt(acc_x*acc_x + acc_y*acc_y + acc_z*acc_z);
        double low_thread = 0.5;
        double high_thread = 1.7;
        if (length < low_thread * 9.8 || length > high_thread * 9.8)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
//        long currentTime = System.currentTimeMillis();
//        long currentTime = 1;

        if (type == Sensor.TYPE_ACCELEROMETER) {
            // return 3 values, one for each axis.
            float acc_x = event.values[0];
            float acc_y = event.values[1];
            float acc_z = event.values[2];

            mLoggerAccelerometer.log(acc_x, acc_y, acc_z);

            mTextViewX.setText(Float.toString(acc_x));
            mTextViewY.setText(Float.toString(acc_y));
            mTextViewZ.setText(Float.toString(acc_z));

//            byte[] buf_x = ByteBuffer.allocate(4).putFloat(acc_x).array();
//            byte[] buf_y = ByteBuffer.allocate(4).putFloat(acc_y).array();
//            byte[] buf_z = ByteBuffer.allocate(4).putFloat(acc_z).array();
//            byte[] buf_time = ByteBuffer.allocate(8).putLong(currentTime).array();
//            byte[] buffer = new byte[20];
//
//            System.arraycopy(buf_x, 0, buffer, 0, 4);
//            System.arraycopy(buf_y, 0, buffer, 4, 4);
//            System.arraycopy(buf_z, 0, buffer, 8, 4);
//            System.arraycopy(buf_time, 0, buffer, 12, 8);
//            mPublisherAcc.send(buffer);

            if (isFall(acc_x, acc_y, acc_z))
            {
                // play music
                mediaPlayer.start();
            }
        }
        else if (type == Sensor.TYPE_GYROSCOPE) {
            // return 3 values, one for each axis.
            float rads_x = event.values[0];
            float rads_y = event.values[1];
            float rads_z = event.values[2];

            mLoggerGyroscope.log(rads_x, rads_y, rads_z);

            mTextViewRX.setText(Float.toString(rads_x));
            mTextViewRY.setText(Float.toString(rads_y));
            mTextViewRZ.setText(Float.toString(rads_z));

//            byte[] buf_x = ByteBuffer.allocate(4).putFloat(rads_x).array();
//            byte[] buf_y = ByteBuffer.allocate(4).putFloat(rads_y).array();
//            byte[] buf_z = ByteBuffer.allocate(4).putFloat(rads_z).array();
//            byte[] buf_time = ByteBuffer.allocate(8).putLong(currentTime).array();
//            byte[] buffer = new byte[20];
//
//            System.arraycopy(buf_x, 0, buffer, 0, 4);
//            System.arraycopy(buf_y, 0, buffer, 4, 4);
//            System.arraycopy(buf_z, 0, buffer, 8, 4);
//            System.arraycopy(buf_time, 0, buffer, 12, 8);
//            mPublisherGyro.send(buffer);
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mAccelerometer != null) {
//            mSensorManager.registerListener(this, mAccelerometer, 10000);
//        }
//
//        if (mContextAcc == null) {
//            mContextAcc = ZMQ.context(1);
//            mPublisherAcc = mContextAcc.socket(ZMQ.PUB);
//            mPublisherAcc.bind("tcp://*:5556");
//        }
//
//        if (mGyroscope != null) {
//            mSensorManager.registerListener(this, mGyroscope, 10000);
//        }
//
//        if (mContextGyro == null) {
//            mContextGyro = ZMQ.context(1);
//            mPublisherGyro = mContextGyro.socket(ZMQ.PUB);
//            mPublisherGyro.bind("tcp://*:5557");
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mAccelerometer != null) {
//            mSensorManager.unregisterListener(this);
//        }
//        mPublisherAcc.close();
//        mContextAcc.term();
//        mPublisherAcc = null;
//        mContextAcc = null;
//
//        if (mGyroscope != null) {
//            mSensorManager.unregisterListener(this);
//        }
//        mPublisherGyro.close();
//        mContextGyro.term();
//        mPublisherGyro = null;
//        mContextAcc = null;
    }

}
