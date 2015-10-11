package com.seaco.seaconeuropsych;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NumericMainActivity extends ActionBarActivity {
    final Context context = this;
    private long number;
    private int round_no;
    private int num_correct_so_far;
    private int num_errors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numeric_main);

        // Extract intent from previous Activity, aka extract current round number
        Intent intent = getIntent();
        round_no = intent.getIntExtra("roundNo",1);
        num_correct_so_far = intent.getIntExtra("numCorrectSoFar",0);
        num_errors = intent.getIntExtra("numErrors",0);

        // Update round number display
        TextView tv_round = (TextView)findViewById(R.id.roundView);
        tv_round.setText(getString(R.string.numeric_main_round, round_no));

        // Calculate 10 to the power of round_no
        // This is to generate a random number of n+1 digits for round n
        // nextInt(900) will yield random number between 0 to 899 for example
        // Hence, randnum = random(0...9*10^i) + 10^i will yield an i+1 digits number
        // e.g for round 4, 5 digit numbers.
        // >> randnum = random(0...89999) + 10000 will yield numbers between 10000 and 99999
        double i = (long) Math.pow(10, round_no);

        Random r = new Random();
        //number = r.nextInt( 9 * i ) + i;
        number = (long) ( (r.nextDouble()*9*i) + i );

        // Displaying the generated random number
        TextView tv_number = (TextView)findViewById(R.id.numeric_num);
        tv_number.setText(getString(R.string.numeric_main_number, number));

        // Start timer
        numericMainTimer(round_no);
    }



    private void numericMainTimer(int round_no) {
        // Calculating the time allowance, as stated in the requirement
        // Time Allowance formula = 2s + Number of digits * 500ms
        final int timeAllowance = 2000 + ( (round_no + 1) * 500);
        final int displayTime = (int) timeAllowance/1000;

        // Initialise countdowntimer, 950 tick interval because I find the system clock a bit inaccurate
        CountDownTimer cdt = new CountDownTimer(timeAllowance, 950) {

            // Getting the countdown number Views and the ImageView for the pie chart progress
            TextView tv_countdown = (TextView)findViewById(R.id.numeric_countdown_initial);
            ImageView iv = (ImageView)findViewById(R.id.numeric_progress);

            int i = displayTime;

            public void onTick(long millisUntilFinished) {
                // Update the pie progress every tick
                float angle = ((float) millisUntilFinished)/timeAllowance*100;
                circularCountDown(iv, (int) angle);

                // Update the text display of time remaining every tick
                tv_countdown.setText(""+ i);
                tv_countdown.bringToFront();
                i--;

            }

            public void onFinish() {
                // finish, proceed to input
                tv_countdown.setText("0");
                circularCountDown(iv, 0);
                numericMainGotoInput();
            }
        };

        // Start timer
        cdt.start();

    }

    private void circularCountDown(ImageView iv, int i) {
        //Initialise the canvas and drawings
        Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        Paint paint = new Paint();

        // Set color to a light grey
        paint.setColor(Color.parseColor("#CDCDCD"));
        paint.setStyle(Paint.Style.FILL);

        // Draw the pie chart using drawArc()
        final RectF oval = new RectF();
        oval.set(10,10,90,90);
        canvas.drawArc(oval, 270, ((i*360)/100), true, paint);

        iv.setImageBitmap(b);
    }

    private void numericMainGotoInput() {
        // Preparing to go to Input View
        // Passing data over
        int duration_displayed = 2000 + ( (round_no + 1) * 500);
        Intent intent = new Intent(this, NumericMainInput.class);
        intent.putExtra("durationDisplayed", duration_displayed);
        intent.putExtra("numberGeneratedForCurrentRound", number);
        intent.putExtra("roundNo", round_no);
        intent.putExtra("numberOfDigits", round_no+1);
        intent.putExtra("numCorrectSoFar", num_correct_so_far);
        intent.putExtra("numErrors", num_errors);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_numeric_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() { // Disable hardware back button
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }
}
