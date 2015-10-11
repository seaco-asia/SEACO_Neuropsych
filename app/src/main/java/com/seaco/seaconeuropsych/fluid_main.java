package com.seaco.seaconeuropsych;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class fluid_main extends Activity implements View.OnClickListener{
    /**
     *   Application name: SEACO Fluid Intelligence Test
     *   Author: SEACO Student Partners (SSP-IT)
     *   Last modified: 15 December 2014
     */
    private Boolean startAsk = false;
    private TextView textViewTime, textViewMessage;
    private Button button1, button2, button3, button4, button5, button6, do_not_know, not_answer;
    private int questionNum = 0, correctNum = 0, wrongNum = 0, doNotKnowNum = 0, notAnswerNum = 0;
    private CountDownTimer timer;
    private long timeLeft = 120000;
    private String[] questionList, button1List, button2List, button3List, button4List, button5List, button6List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load activity_main layout
        setContentView(R.layout.activity_fluid_main);
        // Initiate all the lists
        questionList = getResources().getStringArray(R.array.questionList);
        button1List = getResources().getStringArray(R.array.button1List);
        button2List = getResources().getStringArray(R.array.button2List);
        button3List = getResources().getStringArray(R.array.button3List);
        button4List = getResources().getStringArray(R.array.button4List);
        button5List = getResources().getStringArray(R.array.button5List);
        button6List = getResources().getStringArray(R.array.button6List);
        // Initiate all the buttons
        button1 = initiateButton(R.id.button1);
        button2 = initiateButton(R.id.button2);
        button3 = initiateButton(R.id.button3);
        button4 = initiateButton(R.id.button4);
        button5 = initiateButton(R.id.button5);
        button6 = initiateButton(R.id.button6);
        do_not_know = initiateButton(R.id.do_not_know);
        not_answer = initiateButton(R.id.not_answer);
        // Set button1 and button2 message
        button1.setText(R.string.start_button);
        button1.setVisibility(View.VISIBLE);
        button2.setText(R.string.unable_try_button);
        button2.setVisibility(View.VISIBLE);
        // Initiate timer and text view for timer
        textViewTime = (TextView) findViewById(R.id.timer);
        timer = new CountDownTimer(timeLeft, 1000) {
            // Tick every one second
            public void onTick(long millisUntilFinished) {
                // Print current time left
                textViewTime.setText(timeFormat(millisUntilFinished));
                // Update time left
                timeLeft = millisUntilFinished;
            }
            // If time is up,
            public void onFinish() {
                // Set timeLeft to 0
                timeLeft = 0;
                // Move to endActivity() function
                endActivity();
            }
        };
        // Initiate text view for message or questions
        textViewMessage = (TextView) findViewById(R.id.message);
        textViewMessage.setText(R.string.start_message);
    }

    // If any button is clicked,
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // If button1 is clicked,
            case R.id.button1:
                if (!startAsk) {
                    // Set do_not_know and not_answer to appear
                    do_not_know.setVisibility(View.VISIBLE);
                    not_answer.setVisibility(View.VISIBLE);
                    // Start timer
                    timer.start();
                    // Enable askQuestion() function
                    startAsk = true;
                } else {
                    if (questionNum == 6) {
                        correctNum++;
                    } else {
                        wrongNum++;
                    }
                }
                break;
            // If button2 is clicked,
            case R.id.button2:
                if (!startAsk) {
                    // button1 and button2  disappeared
                    button1.setVisibility(View.GONE);
                    button2.setVisibility(View.GONE);
                    // Move to endActivity() function
                    endActivity();
                } else {
                    if (questionNum == 3 || questionNum == 5 || questionNum == 7 || questionNum == 9) {
                        correctNum++;
                    } else {
                        wrongNum++;
                    }
                }
                break;
            // If button3 is clicked,
            case R.id.button3:
                if (!startAsk) {
                    finish();
                } else {
                    if (questionNum == 0 || questionNum == 1 || questionNum == 12) {
                        correctNum++;
                    } else {
                        wrongNum++;
                    }
                }
                break;
            // If button4 is clicked,
            case R.id.button4:
                if (questionNum == 2 || questionNum == 4 || questionNum == 8) {
                    correctNum++;
                } else {
                    wrongNum++;
                }
                break;
            // If button5 is clicked,
            case R.id.button5:
                if (questionNum == 10) {
                    correctNum++;
                } else {
                    wrongNum++;
                }
                break;
            // If button6 is clicked,
            case R.id.button6:
                if (questionNum == 11) {
                    correctNum++;
                } else {
                    wrongNum++;
                }
                break;
            // If do_not_know is clicked,
            case R.id.do_not_know:
                doNotKnowNum++;
                break;
            // If not_answer is clicked,
            case R.id.not_answer:
                notAnswerNum++;
                break;
        }
        if (startAsk && questionNum < questionList.length) {
            // Ask a question and increase questionNum by 1
            askQuestion();
        } else {
            // End the timer and move to endActivity() function
            timer.cancel();
            endActivity();
        }
    }

    // Initiate all buttons (Add onClickListener and make it disappeared)
    private Button initiateButton(int button_ID) {
        Button theButton = (Button) findViewById(button_ID);
        theButton.setOnClickListener(this);
        theButton.setVisibility(View.GONE);
        return theButton;
    }

    // Change all the texts in layout based on question number
    private void askQuestion() {
        // Set text view for message to question
        textViewMessage.setText(questionList[questionNum]);
        // Set up all buttons
        setButton(button1List, questionNum, button1);
        setButton(button2List, questionNum, button2);
        setButton(button3List, questionNum, button3);
        setButton(button4List, questionNum, button4);
        setButton(button5List, questionNum, button5);
        setButton(button6List, questionNum, button6);
        // Question number increased by 1
        questionNum++;
    }

    // To set up an button based on the question number
    private void setButton(String[] theList, int index, Button theButton) {
        // If the string in theList at particular index is empty,
        if (theList[index].equals("")) {
            // the button disappeared
            theButton.setVisibility(View.GONE);
        } else {
            // otherwise, the button appeared and the text of the button is set to the string
            theButton.setVisibility(View.VISIBLE);
            theButton.setText(theList[index]);
        }
    }

    // If all questions are asked or time is up,
    private void endActivity() {
        // Disable askQuestion() function
        startAsk = false;
        // Print message depends on performance
        textViewTime.setGravity(Gravity.START);
        if (correctNum > questionNum / 2) {
            textViewTime.setText(R.string.score_well);
        } else {
            textViewTime.setText(R.string.score_bad);
        }
        // Make all buttons disappeared
        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);
        button4.setVisibility(View.GONE);
        button5.setVisibility(View.GONE);
        button6.setVisibility(View.GONE);
        do_not_know.setVisibility(View.GONE);
        not_answer.setVisibility(View.GONE);
        // Print out result
        if (questionNum > 0) {
            textViewMessage.setText("Total time left: " + timeFormat(timeLeft) + "\n" +
                    "Total questions attempted: " + questionNum + "\n" +
                    "Total correct answer: " + correctNum + "\n" +
                    "Total wrong answer: " + wrongNum + "\n" +
                    "Total don't know: " + doNotKnowNum + "\n" +
                    "Total not answer: " + notAnswerNum);
        } else {
            textViewMessage.setText(R.string.try_next_time);
        }
        storeDataInTree();
        // Make exit_button appeared
        button3.setVisibility(View.VISIBLE);
        button3.setText(R.string.exit_button);

        Intent intent = new Intent(fluid_main.this, reaction_intro.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
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

            Element root = doc.createElement("fluid_main");

            Element time = doc.createElement("timeLeft");
            root.appendChild(time);
            time.appendChild(doc.createTextNode(timeFormat(timeLeft)));

            Element questions = doc.createElement("questionNum");
            root.appendChild(questions);
            questions.appendChild(doc.createTextNode(String.valueOf(questionNum)));

            Element correct = doc.createElement("correctNum");
            root.appendChild(correct);
            correct.appendChild(doc.createTextNode(String.valueOf(correctNum)));

            Element wrong = doc.createElement("wrongNum");
            root.appendChild(wrong);
            wrong.appendChild(doc.createTextNode(String.valueOf(wrongNum)));

            Element dontKnow = doc.createElement("doNotKnowNum");
            root.appendChild(dontKnow);
            dontKnow.appendChild(doc.createTextNode(String.valueOf(doNotKnowNum)));

            Element notAnswer = doc.createElement("notAnswerNum");
            root.appendChild(notAnswer);
            notAnswer.appendChild(doc.createTextNode(String.valueOf(notAnswerNum)));

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

    // Change millisUntilFinished to time format 00:00:00
    private static String timeFormat(long millisUntilFinished) {
        long seconds = millisUntilFinished / 1000;
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d:%02d:%02d", h,m,s);
    }

    // Prevent using back button in phone
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }
}