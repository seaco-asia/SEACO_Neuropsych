package com.seaco.seaconeuropsych;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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


public class NumericEndReport extends ActionBarActivity {
    public static long duration;
    private long number;
    private int round_no;
    private int numberOfDigits;
    private int num_correct_so_far;
    private int num_errors;
    private boolean skipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numeric_end_report);

        // Extracting intent data from previous activity
        Intent intent = getIntent();
        number = intent.getLongExtra("numberGeneratedForCurrentRound", 0);
        round_no = intent.getIntExtra("roundNo",1);
        numberOfDigits = intent.getIntExtra("numberOfDigits", 0);
        num_correct_so_far = intent.getIntExtra("numCorrectSoFar",0);
        num_errors = intent.getIntExtra("numErrors",0);
        skipped = intent.getBooleanExtra("skipped",false);

        // getting total time taken
        duration = System.currentTimeMillis() - NumericStart.start_time;

        // Uppercasing the first char of the boolean value
        String skipped_str = Boolean.toString(skipped);
        String skipped_str_caps = Character.toUpperCase(skipped_str.charAt(0)) + skipped_str.substring(1);

        // Displaying the data
        TextView textview = (TextView)findViewById(R.id.report_textview);

        textview.setText(
                getString(R.string.numeric_last_number) + number + "\n" +
                        getString(R.string.numeric_rounds_answered) + round_no + "\n" +
                        getString(R.string.numeric_num_of_digits) + numberOfDigits + "\n" +
                        getString(R.string.numeric_num_correct) + num_correct_so_far + "\n" +
                        getString(R.string.numeric_num_wrong) + num_errors + "\n" +
                        getString(R.string.numeric_skipped) + skipped_str_caps + "\n" +
                        getString(R.string.numeric_duration) + duration/1000 + " " + getString(R.string.duration_unit)

        );
    }

    public void proceed(View view) {
        // placeholder for linking to another test game
        writeXML();
        Intent intent = new Intent(this, EndBranch.class);
        startActivity(intent);
        finish();
    }

    private void writeXML() {

        try {
            System.out.println("Start");
            String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "SEACO" + "/" + prospective_initial.filename;
            File file = new File(filepath);
            boolean fileCreated = file.createNewFile();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            //Document doc = docBuilder.newDocument();
            Document doc = docBuilder.parse("file://" + filepath);

            Node root = doc.getFirstChild();
            Node numeric_tag = doc.getElementsByTagName("numericMemory").item(0);
            Element isCompletedStoppedAbandoned = doc.createElement("isSkipped");
            isCompletedStoppedAbandoned.appendChild(doc.createTextNode(String.valueOf(skipped)));
            numeric_tag.appendChild(isCompletedStoppedAbandoned);

            Element totalTimeTaken = doc.createElement("totalTimeTaken");
            totalTimeTaken.appendChild(doc.createTextNode(String.valueOf(duration)));
            numeric_tag.appendChild(totalTimeTaken);

            long currentMax = 0;
            long current = 0;
            boolean correct = false;

            NodeList round_tags_list = numeric_tag.getChildNodes();
            for (int i=0; i < round_tags_list.getLength(); i++ ) {
                Node node = round_tags_list.item(i);

                if ("round".equals(node.getNodeName())) {
                    NodeList sublist = node.getChildNodes();

                    for (int j=0; j < sublist.getLength(); j++) {
                        Node subnode = sublist.item(j);

                        if ("noOfDigits".equals(subnode.getNodeName())) {
                            //System.out.println(subnode.getTextContent());
                            current = Long.parseLong(subnode.getTextContent(),10);
                        }

                        if ("isCorrectInEachRound".equals(subnode.getNodeName())) {
                            //System.out.println("X");
                            //System.out.println(subnode.getTextContent());
                            correct = Boolean.parseBoolean(subnode.getTextContent());
                        }
                    }

                    if (current > currentMax && correct) {
                        //System.out.println(currentMax);
                        currentMax = current;
                    }
                }
            }

            Element maxNoOfDigitRemembered = doc.createElement("maxNoOfDigitRemembered");
            maxNoOfDigitRemembered.appendChild(doc.createTextNode(String.valueOf(currentMax)));
            numeric_tag.appendChild(maxNoOfDigitRemembered);


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
        } catch (SAXException sae) {
            sae.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_numeric_end_report, menu);
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
}
