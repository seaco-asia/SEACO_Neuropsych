package com.seaco.seaconeuropsych;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Random;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class reaction_main extends ActionBarActivity {
    int count=0;
    int counter=0;
    long timestart=-1;
    long timefinish;
    String indexA="";
    String indexB="";
    double []times=new double[13];
    Button match;
    Button start;
    double rounded;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_main);

        start=(Button) findViewById(R.id.button1);
        final TextView reaction=(TextView) findViewById(R.id.textView2);
        reaction.setVisibility(View.INVISIBLE);
        final EditText text=(EditText) findViewById(R.id.editText2);
        text.setVisibility(View.INVISIBLE);
        ImageView imageview2=(ImageView) findViewById(R.id.imageView2);
        ImageView imageview1=(ImageView) findViewById(R.id.imageView1);
        //set an image to the image views
        imageview1.setImageResource(R.drawable.l2);
        imageview2.setImageResource(R.drawable.l1);
        match=(Button) findViewById(R.id.button2);
        match.setVisibility(View.INVISIBLE);
        match.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                counter++;
                timefinish = System.currentTimeMillis();


                times[count] = (timefinish - timestart)/1000;
                BigDecimal bde = new BigDecimal(times[count]);
                bde = bde.round(new MathContext(3));
                double time = bde.doubleValue();
                if (timestart == -1) {
                    reaction.setVisibility(View.INVISIBLE);
                    text.setVisibility(View.INVISIBLE);
                } else {
                    count++;
                    text.setVisibility(View.VISIBLE);
                    reaction.setVisibility(View.VISIBLE);
                    text.setText(String.valueOf(time) + " seconds!");
                    timestart = -1;
                    if (count == 12) {
                        double sum = 0;
                        for (int j = 3; j <= 12; j++) {
                            sum = sum + times[j];
                        }
                        double mean = sum / 10;
                        BigDecimal bd = new BigDecimal(mean);
                        bd = bd.round(new MathContext(3));
                        rounded = bd.doubleValue();
                        text.setText("Average reaction time is : " + String.valueOf(rounded));
                        match.setVisibility(View.INVISIBLE);

                    } else {
                        displayPictures();
                    }

                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //start the game
                match.setVisibility(View.VISIBLE);
                start.setVisibility(View.INVISIBLE);
                displayPictures();


                //match.setVisibility(View.INVISIBLE);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reaction_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("NewApi")
    public void displayPictures(){


        Random r=new Random();

        int chosen=r.nextInt(12-1)+1;
        final ImageView img1=(ImageView) findViewById(R.id.imageView1);
        final ImageView img2=(ImageView) findViewById(R.id.imageView2);
        switch (chosen){
            case 1 :
            {
                img1.setImageResource(R.drawable.l1);
                indexA=indexA+" L1,";
                img2.setImageResource(R.drawable.l2);
                indexB=indexB+" L2,";
                break;
            }

            case 2:
            {
                img1.setImageResource(R.drawable.l3);
                indexA=indexA+" L3,";
                img2.setImageResource(R.drawable.l2);
                indexB=indexB+" L2,";
                break;
            }

            case 3:
            {
                img1.setImageResource(R.drawable.l3);
                indexA=indexA+" L3,";
                img2.setImageResource(R.drawable.l4);
                indexB=indexB+" L4,";
                break;
            }
            case 4:
            {
                img1.setImageResource(R.drawable.l5);
                indexA=indexA+" L5,";
                img2.setImageResource(R.drawable.l4);
                indexB=indexB+" L4,";
                break;
            }
            case 5:
            {
                img1.setImageResource(R.drawable.l5);
                indexA=indexA+" L5,";
                img2.setImageResource(R.drawable.l6);
                indexB=indexB+" L6,";
                break;
            }
            case 6:
            {
                img1.setImageResource(R.drawable.l6);
                indexA=indexA+" L6,";
                img2.setImageResource(R.drawable.l11);
                indexB=indexB+" L11,";
                break;
            }
            case 7:
            {
                img1.setImageResource(R.drawable.l12);
                indexA=indexA+" L12,";
                img2.setImageResource(R.drawable.l7);
                indexB=indexB+" L7,";
                break;
            }
            case 8:
            {
                img1.setImageResource(R.drawable.l10);
                indexA=indexA+" L10,";
                img2.setImageResource(R.drawable.l8);
                indexB=indexB+" L8,";
                break;
            }
            case 9:
            {
                img1.setImageResource(R.drawable.l9);
                indexA=indexA+" L9,";
                img2.setImageResource(R.drawable.l1);
                indexB=indexB+" L1,";
                break;
            }
            case 10:
            {
                img1.setImageResource(R.drawable.l1);
                indexA=indexA+" L1,";
                img2.setImageResource(R.drawable.l10);
                indexB=indexB+" L10,";
                break;
            }
            case 11:
            {
                img1.setImageResource(R.drawable.l11);
                indexA=indexA+" L11,";
                img2.setImageResource(R.drawable.l8);
                indexB=indexB+" L8,";
                break;
            }
            case 12:
            {
                img1.setImageResource(R.drawable.l12);
                indexA=indexA+" L12,";
                img2.setImageResource(R.drawable.l3);
                indexB=indexB+" L3,";
                break;
            }
        }


        randomise();



    }
    public void randomise(){
        //randomises the amount of time before chaging the picture
        Random r=new Random();
        //get a random number between 10000 and 5000, 5000 included
        int end=r.nextInt(10000-5000)+5000;
        CountDownTimer timer= new CountDownTimer(end,1000) {

            public void onFinish() {
                //display a random pairs of cards
                randompic();
                //start counter for the number of seconds
                timestart=System.currentTimeMillis();
               try{
                String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "SEACO" + "/" + prospective_initial.filename;
                File file= new File(filepath);
                boolean fileCreated = file.createNewFile();

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse("file://"+filepath);
                Node xroot=doc.getFirstChild();
                Node pairlevel1_tag= doc.getElementsByTagName("reaction").item(0);
                Element root = doc.createElement("reaction");
                //doc.appendChild(root);
                Element indA = doc.createElement("indexA");
                root.appendChild(indA);
                indA.appendChild(doc.createTextNode(indexA));
                Element indB = doc.createElement("indexB");
                root.appendChild(indB);
                indB.appendChild(doc.createElement(indexB));
                Element nclick=doc.createElement("NumberofClick");
                root.appendChild(nclick);
                nclick.appendChild(doc.createTextNode(String.valueOf(counter)));
                Element time=doc.createElement("TimeFirst");
                root.appendChild(time);
                for (int i=0;i<=12;i++){
                    time.appendChild(doc.createTextNode(String.valueOf(times[i])));
                }

                Element meanTime=doc.createElement("mean");
                root.appendChild(meanTime);
                meanTime.appendChild(doc.createTextNode(String.valueOf(rounded)));
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


            String text="";
            public void onTick(long millisUntilFinished) {
                //count number of milliseconds
                text=text+ millisUntilFinished;
            }
        }.start();
        timer.start();



    }


    public void randompic(){
        Random r=new Random();
        //obtain a random number between 1 and 12,  1 included
        int chosen=r.nextInt(12-1)+1;
        final ImageView img1=(ImageView) findViewById(R.id.imageView1);
        final ImageView img2=(ImageView) findViewById(R.id.imageView2);
        //display the images depending on the random number
        switch (chosen){
            case 1 :
            {
                img1.setImageResource(R.drawable.l1);
                indexA=indexA+" L1,";
                indexB=indexB+" L1,";
                img2.setImageResource(R.drawable.l1);
                break;
            }

            case 2:
            {
                img1.setImageResource(R.drawable.l2);
                img2.setImageResource(R.drawable.l2);
                indexA=indexA+" L2,";
                indexB=indexB+" L2,";
                break;
            }

            case 3:
            {
                img1.setImageResource(R.drawable.l3);
                img2.setImageResource(R.drawable.l3);
                indexA=indexA+" L3,";
                indexB=indexB+" L3,";
                break;

            }
            case 4:
            {
                img1.setImageResource(R.drawable.l4);
                img2.setImageResource(R.drawable.l4);
                indexA=indexA+" L4,";
                indexB=indexB+" L4,";
                break;
            }
            case 5:
            {
                img1.setImageResource(R.drawable.l5);
                img2.setImageResource(R.drawable.l5);
                indexA=indexA+" L5,";
                indexB=indexB+" L5,";
                break;
            }
            case 6:
            {
                img1.setImageResource(R.drawable.l6);
                img2.setImageResource(R.drawable.l6);
                indexA=indexA+" L6,";
                indexB=indexB+" L6,";
                break;
            }
            case 7:
            {
                img1.setImageResource(R.drawable.l7);
                img2.setImageResource(R.drawable.l7);
                indexA=indexA+" L7,";
                indexB=indexB+" L7,";
                break;
            }
            case 8:
            {
                img1.setImageResource(R.drawable.l8);
                img2.setImageResource(R.drawable.l8);
                indexA=indexA+" L8,";
                indexB=indexB+" L8,";
                break;
            }
            case 9:
            {
                img1.setImageResource(R.drawable.l9);
                img2.setImageResource(R.drawable.l9);
                indexA=indexA+" L9,";
                indexB=indexB+" L9,";
                break;
            }
            case 10:
            {
                img1.setImageResource(R.drawable.l10);
                img2.setImageResource(R.drawable.l10);
                indexA=indexA+" L10,";
                indexB=indexB+" L10,";
                break;
            }
            case 11:
            {
                img1.setImageResource(R.drawable.l1);
                img2.setImageResource(R.drawable.l1);
                indexA=indexA+" L11,";
                indexB=indexB+" L11,";
                break;
            }
            case 12:
            {
                img1.setImageResource(R.drawable.l12);
                img2.setImageResource(R.drawable.l12);
                indexA=indexA+" L12,";
                indexB=indexB+" L12,";
                break;
            }
        }


    }

    public void endActivity(View view) {
        // when OK button is clicked, proceed
        Intent intent = new Intent(reaction_main.this, prospective_end.class);
        int shape = 1;
        intent.putExtra("Answer", shape);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() { // Disable hardware back button
    }
}