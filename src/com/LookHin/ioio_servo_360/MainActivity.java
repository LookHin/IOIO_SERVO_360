package com.LookHin.ioio_servo_360;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;


/*
 * Pulse Control = PIN 3
 * VCC = 5V
 * GND = GND
 */


public class MainActivity extends IOIOActivity {
	
	private ToggleButton toggleButton1;
	private Button button1;
	private Button button2;
	private Button button3;
	
	private String left_or_right_or_stop = "Stop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.action_about:
        	//Toast.makeText(getApplicationContext(), "Show About", Toast.LENGTH_SHORT).show();
        	
        	Intent about = new Intent(this, AboutActivity.class);
    		startActivity(about);
    		
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    } 
    
    
    class Looper extends BaseIOIOLooper {
		
    	private DigitalOutput digital_led1;
    	
    	private PwmOutput servoPwmOutput1;	

		@Override
		protected void setup() throws ConnectionLostException {

			digital_led1 = ioio_.openDigitalOutput(0,true);
			
			servoPwmOutput1 = ioio_.openPwmOutput(3, 100);
			
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(), "IOIO Connect", Toast.LENGTH_SHORT).show();
				}
			});
			
			
			// Set To Left
			button1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					left_or_right_or_stop = "Left";
				}
			});
			
			// Set To Right
			button2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					left_or_right_or_stop = "Right";
				}
			});
			
			// Set To Stop
			button3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					left_or_right_or_stop = "Stop";
				}
			});
			
		}

		@Override
		public void loop() throws ConnectionLostException {
			
			try{
				digital_led1.write(!toggleButton1.isChecked());
				
				
				if(left_or_right_or_stop.equals("Right")){
					servoPwmOutput1.setPulseWidth(1300);
				}else if(left_or_right_or_stop.equals("Left")){
					servoPwmOutput1.setPulseWidth(1700);
				}else if(left_or_right_or_stop.equals("Stop")){
					servoPwmOutput1.setDutyCycle(0);
					//servoPwmOutput1.setPulseWidth(1500);
				}
			
				Thread.sleep(10);
			}catch(InterruptedException e){
				ioio_.disconnect();
			}
			
		}
	}

	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}
    
}
