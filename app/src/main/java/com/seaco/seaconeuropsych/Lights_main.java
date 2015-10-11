package com.seaco.seaconeuropsych;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Lights_main extends Activity implements View.OnClickListener {
    /**
     *   Application name: SEACO Lights Pattern Memory Test
     *   Author: SEACO Student Partners (SSP-IT)
     *   Last modified: 17 December 2014
     */
    private ArrayList<ImageButton> windows; // An array which contains all the window objects
    private TextView message; // A text view for any message
    private Button next_button; // A button to go next step
    private CountDownTimer timer; // A timer
    private int step = 2; // An integer to keep track number of next_button clicked
    private int noOfCorrect = 0, noOfWrong = 0; // Set result to 0
    private boolean[] combination; // A boolean list to store what user clicked
    private boolean[] answer; // A boolean list to store the answer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights_main); // Load activity_main layout
        final int totalWindows = 16; // Set total windows to 16
        combination = new boolean[totalWindows]; // Initiate combination to a length of totalWindows
        answer = new boolean[totalWindows]; // Initiate answer to a length of totalWindows

        windows = new ArrayList<>(); // Initiate all the windows and add into windows list
        windows.add(initiateWindow(R.id.window01));
        windows.add(initiateWindow(R.id.window02));
        windows.add(initiateWindow(R.id.window03));
        windows.add(initiateWindow(R.id.window04));
        windows.add(initiateWindow(R.id.window05));
        windows.add(initiateWindow(R.id.window06));
        windows.add(initiateWindow(R.id.window07));
        windows.add(initiateWindow(R.id.window08));
        windows.add(initiateWindow(R.id.window09));
        windows.add(initiateWindow(R.id.window10));
        windows.add(initiateWindow(R.id.window11));
        windows.add(initiateWindow(R.id.window12));
        windows.add(initiateWindow(R.id.window13));
        windows.add(initiateWindow(R.id.window14));
        windows.add(initiateWindow(R.id.window15));
        windows.add(initiateWindow(R.id.window16));

        message = (TextView) findViewById(R.id.message); // Initiate text view for message
        message.setText(R.string.lights_start_message); // Print start_message

        next_button = (Button) findViewById(R.id.next_button); // Initiate next_button
        next_button.setOnClickListener(this); // Set onClickListener for next_button

        timer = new CountDownTimer(10000, 1000) { // Initiate timer
            public void onTick(long millisUntilFinished) { // Tick every one second
                message.setText(timeFormat(millisUntilFinished)); // Print current time left
            }
            public void onFinish() { // If time is up,
                message.setText(R.string.instruction); // Print instruction
                for (int i = 0; i < combination.length; i++) {
                    combination[i] = false; // Set all combination to false
                    windows.get(i).setClickable(true); // Set all windows to clickable
                }
                setWindowImage(combination); // Set all windows image based on the combination
                next_button.setVisibility(View.VISIBLE); // Set next_button to appear
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.window01:
            case R.id.window02:
            case R.id.window03:
            case R.id.window04:
            case R.id.window05:
            case R.id.window06:
            case R.id.window07:
            case R.id.window08:
            case R.id.window09:
            case R.id.window10:
            case R.id.window11:
            case R.id.window12:
            case R.id.window13:
            case R.id.window14:
            case R.id.window15:
            case R.id.window16:
                int clickedWindowID = v.getId(); // If a window is clicked, its ID will be collected,
                for (int i = 0; i < windows.size(); i++) { // The ID will then compare with all the windows
                    if (windows.get(i).getId() == clickedWindowID) { // If any match found,
                        combination = switchOnOff(combination, i); // That window will switch on if it is previously off and vice versa
                        setWindowImage(combination);
                    }
                }
                break;
            // If next_button is clicked,
            case R.id.next_button:
                int currentNoOfWindows = (int) Math.pow(step, 2); // Set current number of windows equal to step to the power of 2
                if (step >= 2 && step < 5) { // Step 2 is for 4 windows, step 3 is for 9 windows and step 4 is for 16 windows
                    if (step > 2) { // Only start check answer after step 2
                        if (Arrays.equals(combination, answer)) { // Return true if the combination is same as the answer, otherwise false
                            noOfCorrect++; // If condition is true, increase noOfCorrect by 1
                        } else {
                            noOfWrong++; // If condition is false, increase noOfWrong by 1
                        }
                    }
                    answer = randomLightOn(currentNoOfWindows); // Randomize the answer (some windows' light are on)
                    setWindowImage(answer); // Set all windows' image to the answer combination
                    for (int i = 0; i < currentNoOfWindows; i++) { // Set all current windows to appear but not clickable
                        windows.get(i).setVisibility(View.VISIBLE);
                        windows.get(i).setClickable(false);
                    }
                    next_button.setVisibility(View.GONE); // Set next_button disappeared
                    timer.start(); // Start timer
                    step++; // Increase step by 1
                } else if (step == 5) { // Step 5 is after 16 windows which will end the activity
                    if (Arrays.equals(combination, answer)) { // Return true if the combination is same as the answer, otherwise false
                        noOfCorrect++; // If condition is true, increase noOfCorrect by 1
                    } else {
                        noOfWrong++; // If condition is false, increase noOfWrong by 1
                    }
                    endActivity(noOfCorrect, noOfWrong); // End activity
                    step++; // Increase step by 1
                } else {
                    finish(); // Close application
                }
                break;
        }
    }

    private void endActivity(int noOfCorrect, int noOfWrong) { // End activity
        message.setText("Finish\nNumber of correct: " + noOfCorrect + "\nNumber of wrong: " + noOfWrong); // Print result
        storeDataInTree();
        for (int i = 0; i < windows.size(); i++) { // Set all windows disappeared
            windows.get(i).setVisibility(View.GONE);
        }
        next_button.setText("Finish"); // Set next_button text to Finish
    }

    private void setWindowImage(boolean[] combination) { // Set window image based on the combination
        for (int i = 0; i < combination.length; i++) {
            if (combination[i]) {
                windows.get(i).setImageResource(R.drawable.light_on); // Set the window to light_on image
            } else {
                windows.get(i).setImageResource(R.drawable.light_off); // Set the window to light_off image
            }
        }
    }

    private boolean[] randomLightOn(int numberOfWindows) { // To randomize which window's light is on
        Random randomInt = new Random();
        boolean[] randomCombination = new boolean[combination.length]; // Create an array with same length as combination
        int counter = 0;
        int limit = (int) Math.ceil(numberOfWindows/2); //  2 if numberOfWindows is 4, 5 if numberOfWindows is 9 and 8 if numberOfWindows is 16
        while (counter < limit) { // While counter is within the range
            int chosen = randomInt.nextInt(numberOfWindows); // Random any integer from 0 to numberOfWindows
            if (!randomCombination[chosen]) { // If the item chosen in the list is false,
                randomCombination[chosen] = true; // Set it to true
                counter++; // Go to the next window
            }
        }
        return randomCombination; // Return the combination which is the answer
    }

    private boolean[] switchOnOff(boolean[] anArray, int n) { // If light is off, set to on and vice versa
        anArray[n] = !anArray[n];
        return anArray;
    }

    private ImageButton initiateWindow(int windowID) { // Initiate all image buttons
        ImageButton theWindow = (ImageButton) findViewById(windowID); // Initiate theWindow as ImageButton
        theWindow.setOnClickListener(this); // Set onClickListener to theWindow
        theWindow.setClickable(false); // Set theWindow clickable
        theWindow.setVisibility(View.GONE); // Set theWindow to disappear
        return theWindow; // Return theWindow
    }

    public void storeDataInTree() {
        try{
            String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "SEACO" + "/" + prospective_initial.filename;
            File file= new File(filepath);
            boolean fileCreated = file.createNewFile();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("file://" + filepath);

            Node xroot = doc.getFirstChild();

            Element root = doc.createElement("Lights_main");

            Element correct = doc.createElement("noOfCorrect");
            root.appendChild(correct);
            correct.appendChild(doc.createTextNode(String.valueOf(noOfCorrect)));

            Element wrong = doc.createElement("noOfWrong");
            root.appendChild(wrong);
            wrong.appendChild(doc.createTextNode(String.valueOf(noOfWrong)));
            // write the content into xml file
            xroot.appendChild(root);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            StreamResult resultx = new StreamResult(System.out);
            transformer.transform(source, result);
            transformer.transform(source, resultx);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }
    }

    private static String timeFormat(long millisUntilFinished) { // Change millisUntilFinished to time format hh:mm:ss
        long seconds = millisUntilFinished / 1000; // Convert millisecond to second
        long s = seconds % 60; // Calculate second
        long m = (seconds / 60) % 60; // Calculate minute
        long h = (seconds / (60 * 60)) % 24; // Calculate hour
        return String.format("%d:%02d:%02d", h,m,s); // Return time with format hh:mm:ss
    }

    @Override
    public void onBackPressed() { // Disable hardware back button
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }
}
