package mytechbus.hpie.com.mytechbus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
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

    public void writeToLogFile(String filename, String data,Context context, String mode, String... foldername) {

        String file_data = readLogFile(filename, context, "daily_log");

        File mydir = context.getDir("daily_log", context.MODE_PRIVATE); //Creating an internal dir;

        File fileWithinMyDir = new File(mydir, filename); //Getting a file within the dir.

        try {
            FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file.

            OutputStreamWriter myOutWriter = new OutputStreamWriter(out);

            if(!file_data.equals("")) {
                data = file_data + "," + data;
            }
            myOutWriter.write(data);
            myOutWriter.close();

            //out.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToLocationLog(String filename, String data,Context context, String mode, String... foldername) {

        String file_data = readLogFile(filename, context, "location_log");

        File mydir = context.getDir("location_log", context.MODE_PRIVATE); //Creating an internal dir;

        File fileWithinMyDir = new File(mydir, filename); //Getting a file within the dir.

        try {
            FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file.

            OutputStreamWriter myOutWriter = new OutputStreamWriter(out);

            if(!file_data.equals("")) {
                data = file_data + "," + data;
            }
            myOutWriter.write(data);
            myOutWriter.close();

            //out.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLogFile(String filename, Context context, String folder_name) {

        String fileData = "";

        File mydir = context.getDir(folder_name, context.MODE_PRIVATE); //Creating an internal dir;
        File fileWithinMyDir = new File(mydir, filename); //Getting a file within the dir.
        try {
            FileInputStream fileInputStream = new FileInputStream(fileWithinMyDir);

            fileData = readFromFileInputStream(fileInputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData;
    }

    public void deleteLogFiles(String filename, Context context, String folder_name) {

        String fileData = "";

        File mydir = context.getDir(folder_name, context.MODE_PRIVATE); //Creating an internal dir;

        File file[] = mydir.listFiles();
        //Log.d("Files", "Size: " + file.length);
        for (int i = 0; i < file.length; i++) {
            //here populate your listview

            if(!filename.equals(file[i].getName())) {
                Log.d("myLogs", "if not equal FileName:" + file[i].getName());

                File fileWithinMyDir = new File(mydir, file[i].getName()); //Getting a file within the dir.

                fileWithinMyDir.delete();
            } else {
                Log.d("myLogs", "else equal FileName:" + file[i].getName());
            }

            //File fileWithinMyDir = new File(mydir, filename); //Getting a file within the dir.
        }

    }



    // This method will read data from FileInputStream.
    private String readFromFileInputStream(FileInputStream fileInputStream)
    {
        StringBuffer retBuf = new StringBuffer();

        try {
            if (fileInputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String lineData = bufferedReader.readLine();
                while (lineData != null) {
                    retBuf.append(lineData);
                    lineData = bufferedReader.readLine();
                }
            }
        }catch(IOException ex)
        {
            Log.e("myLogs", ex.getMessage(), ex);
        }finally
        {
            return retBuf.toString();
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

    public Integer getTicketCount(String filename, Context context) {
        Integer count = 0;
        //String file_data = readFromFile(filename, context);
        String file_data = readLogFile(filename, context, "daily_log");

        try {
            JSONArray values = new JSONArray("[" + file_data + "]");

            count = values.length();

            //Log.e("myLogs", "Ticket Count : " + String.valueOf(count));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return count;
    }

}
