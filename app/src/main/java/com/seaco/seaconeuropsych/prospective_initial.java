package com.seaco.seaconeuropsych;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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


public class prospective_initial extends ActionBarActivity {
    public static int shape;        //store shape for this session
    public static int fakeShape;    //the decoy shape for this session
    public static long start_time;  //start timer
    public final static String filename =  String.valueOf(System.currentTimeMillis()) + ".xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide title bar
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.prospective_initial);

        //initialise start timer
        start_time = System.currentTimeMillis();

        // Generate random shape from 1-4
        Random r = new Random();
        shape = r.nextInt(3) + 1;
        String shapeName = null;

        // get shape name from string.xml, and apply bold+underline
        // also generates a decoy shape
        switch (shape) {
            case 1:
                shapeName = "<b><u>" + getResources().getString(R.string.shape1) + "</u></b>";
                fakeShape = 2;
                break;
            case 2:
                shapeName = "<b><u>" + getResources().getString(R.string.shape2) + "</u></b>";
                fakeShape = 3;
                break;
            case 3:
                shapeName = "<b><u>" + getResources().getString(R.string.shape3) + "</u></b>";
                fakeShape = 4;
                break;
            case 4:
                shapeName = "<b><u>" + getResources().getString(R.string.shape4) + "</u></b>";
                fakeShape = 1;
                break;
        }


        // Initialise textview
        TextView txtview = (TextView)findViewById(R.id.prospective_initial_text);
        txtview.setText(Html.fromHtml(getString(R.string.prospective_initial_text, "<br>", shapeName)));

        // setup Images
        ImageView img1= (ImageView) findViewById(R.id.imageView_1);
        img1.setImageResource(R.drawable.octagon_red);

        ImageView img2= (ImageView) findViewById(R.id.imageView_2);
        img2.setImageResource(R.drawable.circle_orange);

        ImageView img3= (ImageView) findViewById(R.id.imageView_3);
        img3.setImageResource(R.drawable.square_blue);

        ImageView img4= (ImageView) findViewById(R.id.imageView_4);
        img4.setImageResource(R.drawable.triangle_green);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prospective_initial, menu);
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

    public void nextActivity(View view) {
        // when Next is clicked, start next activity
        writeXML();
        Intent intent = new Intent(this, pair_intro.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //intent.putExtra("Answer", shape);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    private void writeXML() {

        try {
            System.out.println("Start");
            String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "SEACO" + "/" + filename;
            File file = new File(filepath);
            boolean fileCreated = file.createNewFile();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            //Document doc = docBuilder.parse("file://" + filepath);

            Element root = doc.createElement("seaco");
            doc.appendChild(root);
            Element shape_tag = doc.createElement("shape");
            root.appendChild(shape_tag);
            Element initialAnswer = doc.createElement("initialAnswer");
            shape_tag.appendChild(initialAnswer);
            initialAnswer.appendChild(doc.createTextNode(String.valueOf(shape)));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            StreamResult resultx = new StreamResult(System.out);
            transformer.transform(source, result);
            transformer.transform(source, resultx);

            System.out.println("Done");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() { // Disable hardware back button
    }
}

