package com.seaco.seaconeuropsych;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class pair_level1 extends Activity {
    public String tag = "gme";
    public int activecounter=0;							//counts the number of active cards
    public int cardsactive[]=new int[2];
    public ImageButton b[]= new ImageButton[7];
    public int values[] = new int[7];   			//Stores the random numbers
    int first =0;
    int correct=0;
    int wrong=0;
 	int clicked=-1;
     long timestart;
    long timefinish;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_level1);
        for (int i=1;i<=6;i++){
            if (i>3){
                values[i]=i-3;
            }
            else {
                values[i] = i;
            }
        }
        shufflearray(values);

        Log.d(tag, "values1: " + values[1] + " values 5: " + values[5]);

        b[1]=(ImageButton)findViewById(R.id.imageButton1);
        b[2]=(ImageButton) findViewById(R.id.imageButton2);
        b[3]=(ImageButton) findViewById(R.id.imageButton3);
        b[4]=(ImageButton) findViewById(R.id.imageButton4);
        b[5]=(ImageButton) findViewById(R.id.imageButton5);
        b[6]=(ImageButton) findViewById(R.id.imageButton6);
        next=(Button) findViewById(R.id.next);
        next.setVisibility(View.INVISIBLE);
        b[1].setOnClickListener(new cln(1));
        b[2].setOnClickListener(new cln(2));
        b[3].setOnClickListener(new cln(3));
        b[4].setOnClickListener(new cln(4));
        b[5].setOnClickListener(new cln(5));
        b[6].setOnClickListener(new cln(6));
        next.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                Log.d(tag, "Inside Onclick of the Next Button");

                Intent intent = new Intent(pair_level1.this, pair_level2.class);
                startActivity(intent);
                Log.d(tag, "Finished calling the intent - now finish() going to run");
                finish();


            }
        });


    }

/*    protected void onSaveInstanceState(Bundle outState)
    {
        //Save all the values required for the game
        //Save the button states and any information displayed
        super.onSaveInstanceState(outState);
        Log.d(tag, "onSavedInstanceState");

        outState.putInt("values1", values[1]);
        outState.putInt("values1", values[5]);


    }*/

    public void shufflearray(int [] values){

        //int n = values.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 1; i < 6; i++) {
            int change = i + random.nextInt(6 - i);
            swap(values, i, change);

        }
    }

    private void swap(int[] a, int i, int change) {
        int helper = a[i];
        a[i] = a[change];
        a[change] = helper;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pair_level1, menu);
        return true;
    }
    public void generateNoteOnSD(String sFileName, String sBody){
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            //importError = e.getMessage();
            //iError();
        }
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

    public class cln implements View.OnClickListener{
        int index;
        public cln(int button_index)
        {
            index = button_index;
        }

        @Override
        public void onClick(View v) {

            if (first == 0) {
                first = 1;
                timestart = System.currentTimeMillis();
            }
            activecounter = activecounter + 1;
            if (activecounter == 1) {
                cardsactive[0] = index;
                int value = values[index];
                switch (value) {
                    case 1:
                        b[index].setBackgroundResource(R.drawable.l1);
                        Log.d(tag, "case1");
                        break;
                    case 2:
                        b[index].setBackgroundResource(R.drawable.l2);
                        Log.d(tag, "case2");
                        break;
                    case 3:
                        b[index].setBackgroundResource(R.drawable.l3);
                        Log.d(tag, "case3");
                        break;
                }

            } else if (activecounter == 2) {
                cardsactive[1] = index;
                //disables all other cards
                for (int j = 1; j <= 6; j++) {
                    if (j != cardsactive[0] || j != cardsactive[1]) {
                        b[j].setClickable(false);
                    }
                }

                switch (values[index]) {
                    case 1:
                        b[index].setBackgroundResource(R.drawable.l1);
                        Log.d(tag, "case1");
                        break;
                    case 2:
                        b[index].setBackgroundResource(R.drawable.l2);
                        Log.d(tag, "case1");
                        break;

                    case 3:
                        b[index].setBackgroundResource(R.drawable.l3);
                        break;

                }
                int value1 = values[cardsactive[0]];
                int value2 = values[cardsactive[1]];
                if (value1 != value2) {
                    CountDownTimer timer = new CountDownTimer(1500, 1000) {

                        public void onFinish() {
                            b[cardsactive[0]].setBackgroundResource(R.drawable.blue_card);
                            b[cardsactive[1]].setBackgroundResource(R.drawable.blue_card);
                            wrong = +1;
                            for (int j = 1; j <= 6; j++) {

                                b[j].setClickable(true);


                            }

                        }

                        String text = "";

                        public void onTick(long millisUntilFinished) {
                            // TODO Auto-generated method stub
                            text = text + millisUntilFinished;

                        }
                    }.start();
                    timer.start();

                } else {
                    correct = correct + 1;
                    for (int j = 1; j <= 6; j++) {
                        if (j != cardsactive[0] || j != cardsactive[1]) {
                            b[j].setClickable(true);
                        }
                    }
                }
                value1 = 0;
                value2 = 0;
                activecounter = 0;
            }
            if (correct == 3) {
                timefinish = System.currentTimeMillis();
                long timetaken = (timefinish - timestart) / 1000;
                try {
                    //generateNoteOnSD("Results", "<xml><NumberofColums>3</NumberofColums><NumberofRows>2</NumberofRows><NumberofCorrect>"+correct+"</NumberofCorrect><NumberofIncorrect>"+wrong+"</NumberofIncorrect><Timetaken>"+timetaken+"</Timetaken></xml>");
                    String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "SEACO" + "/" + prospective_initial.filename;
                    File file= new File(filepath);
                    boolean fileCreated = file.createNewFile();

                    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                    Document doc = docBuilder.parse("file://"+filepath);
                    //Node root=doc.getFirstChild();

                    Node xroot = doc.getFirstChild();

                    Element root = doc.createElement("pairlevel1");
                    //Node pairlevel1_tag= doc.getElementsByTagName("pairLevel1").item(0);
                    //doc.appendChild(root);
                    Element columns = doc.createElement("noofcolumns");
                    root.appendChild(columns);
                    columns.appendChild(doc.createTextNode("3"));
                    Element rows = doc.createElement("noofrows");
                    root.appendChild(rows);
                    rows.appendChild(doc.createTextNode("2"));
                    Element ncorrect=doc.createElement("NumberofCorrect");
                    root.appendChild(ncorrect);
                    ncorrect.appendChild(doc.createTextNode(String.valueOf(correct)));
                    Element nwrong=doc.createElement("NumberofIncorrect");
                    root.appendChild(nwrong);
                    nwrong.appendChild(doc.createTextNode(String.valueOf(wrong)));
                    Element time=doc.createElement("TimeTaken");
                    root.appendChild(time);
                    time.appendChild(doc.createTextNode(String.valueOf(timetaken)));
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

                next.setVisibility(View.VISIBLE);


            }

        }
    }


    @Override
    public void onBackPressed() { // Disable hardware back button
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }


}
