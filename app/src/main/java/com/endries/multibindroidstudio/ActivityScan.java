package com.endries.multibindroidstudio;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;


public class ActivityScan extends Activity {

    //<editor-fold desc="DataWedge Declarations">
    // Tag used for logging errors
    private static final String TAG = ActivityScan.class.getSimpleName();

    // Let's define some intent strings
    // This intent string contains the source of the data as a string
    private static final String SOURCE_TAG = "com.motorolasolutions.emdk.datawedge.source";
    // This intent string contains the barcode symbology as a string
    private static final String LABEL_TYPE_TAG = "com.motorolasolutions.emdk.datawedge.label_type";
    // This intent string contains the barcode data as a byte array list
    private static final String DECODE_DATA_TAG = "com.motorolasolutions.emdk.datawedge.decode_data";

    // This intent string contains the captured data as a string
    // (in the case of MSR this data string contains a concatenation of the track data)
    private static final String DATA_STRING_TAG = "com.motorolasolutions.emdk.datawedge.data_string";

    // Let's define the MSR intent strings (in case we want to use these in the future)
    private static final String MSR_DATA_TAG = "com.motorolasolutions.emdk.datawedge.msr_data";
    private static final String MSR_TRACK1_TAG = "com.motorolasolutions.emdk.datawedge.msr_track1";
    private static final String MSR_TRACK2_TAG = "com.motorolasolutions.emdk.datawedge.msr_track2";
    private static final String MSR_TRACK3_TAG = "com.motorolasolutions.emdk.datawedge.msr_track3";
    private static final String MSR_TRACK1_STATUS_TAG = "com.motorolasolutions.emdk.datawedge.msr_track1_status";
    private static final String MSR_TRACK2_STATUS_TAG = "com.motorolasolutions.emdk.datawedge.msr_track2_status";
    private static final String MSR_TRACK3_STATUS_TAG = "com.motorolasolutions.emdk.datawedge.msr_track3_status";

    // Let's define the API intent strings for the soft scan trigger
    private static final String ACTION_SOFTSCANTRIGGER = "com.motorolasolutions.emdk.datawedge.api.ACTION_SOFTSCANTRIGGER";
    private static final String EXTRA_PARAM = "com.motorolasolutions.emdk.datawedge.api.EXTRA_PARAMETER";
    private static final String DWAPI_START_SCANNING = "START_SCANNING";
    private static final String DWAPI_STOP_SCANNING = "STOP_SCANNING";
    private static final String DWAPI_TOGGLE_SCANNING = "TOGGLE_SCANNING";

    private static String ourIntentAction = "";
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("ScanActivity");

        // Set the text view as the activity layout
        setContentView(textView);

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


    // We need to handle any incoming intents, so let override the onNewIntent method
    @Override
    public void onNewIntent(Intent i) {
        handleDecodeData(i);
    }

    private void handleDecodeData(Intent i) {
        // check the intent action is for us
        if (i.getAction().contentEquals(ourIntentAction)) {
            // define a string that will hold our output
            String out = "";
            // get the source of the data
            String source = i.getStringExtra(SOURCE_TAG);
            // save it to use later
            if (source == null) source = "scanner";
            // get the data from the intent
            String data = i.getStringExtra(DATA_STRING_TAG);
            // let's define a variable for the data length
            Integer data_len = 0;
            // and set it to the length of the data
            if (data != null) data_len = data.length();

            // check if the data has come from the barcode scanner
            if (source.equalsIgnoreCase("scanner")) {
                // check if there is anything in the data
                if (data != null && data.length() > 0) {
                    // we have some data, so let's get it's symbology
                    String sLabelType = i.getStringExtra(LABEL_TYPE_TAG);
                    // check if the string is empty
                    if (sLabelType != null && sLabelType.length() > 0) {
                        // format of the label type string is LABEL-TYPE-SYMBOLOGY
                        // so let's skip the LABEL-TYPE- portion to get just the symbology
                        sLabelType = sLabelType.substring(11);
                    } else {
                        // the string was empty so let's set it to "Unknown"
                        sLabelType = "Unknown";
                    }
                    // let's construct the beginning of our output string
                    out = "Source: Scanner, " + "Symbology: " + sLabelType + ", Length: " + data_len.toString() + ", Data: ...\r\n";
                }
            }

            // check if the data has come from the MSR
            if (source.equalsIgnoreCase("msr")) {
                // construct the beginning of our output string
                out = "Source: MSR, Length: " + data_len.toString() + ", Data: ...\r\n";
            }

            // let's get our edit box view
            //EditText et = (EditText) findViewById(R.id.editbox);
            // and get it's text into an editable string
            //Editable txt = et.getText();
            // now because we want format our output
            // we need to put the edit box text into a spannable string builder
            String txt = "";
            SpannableStringBuilder stringbuilder = new SpannableStringBuilder(txt);
            // add the output string we constructed earlier
            stringbuilder.append(out);
            // now let's highlight our output string in bold type
            stringbuilder.setSpan(new StyleSpan(Typeface.BOLD), txt.length(), stringbuilder.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            // then add the barcode or msr data, plus a new line, and add it to the string builder
            stringbuilder.append(data + "\r\n");
            // now let's update the text in the edit box
            //et.setText(stringbuilder);
            // we want the text cursor to be at the end of the edit box
            // so let's get the edit box text again
            //txt = et.getText();
            // and set the cursor position at the end of the text
            //et.setSelection(txt.length());
            // and we are done!
        }

    }
}
