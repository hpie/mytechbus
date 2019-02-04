package mytechbus.hpie.com.mytechbus;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FIleOperations {
    public FIleOperations() {

    }

    public void writeToFile(String filename, String data,Context context, String mode) {

        try {
            OutputStreamWriter outputStreamWriter;
            if(mode == "1") {
                outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_APPEND));
            } else {
                outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            }


            String file_data = readFromFile(filename, context);

            if(!file_data.equals("")) {
                data = "," + data;
            }

            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void writeErrorToFile(String filename, String data,Context context, String mode, String... foldername) {
        File mydir = context.getDir("crashReports", context.MODE_PRIVATE); //Creating an internal dir;
        File fileWithinMyDir = new File(mydir, filename); //Getting a file within the dir.
        try {
            FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file.

            OutputStreamWriter myOutWriter = new OutputStreamWriter(out);

            myOutWriter.write(data);
            myOutWriter.close();

            //out.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFromFile(String filename, Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            //Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            //Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
