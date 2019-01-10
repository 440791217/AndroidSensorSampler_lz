package space.xingzhi.accelaration;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class Writer implements Runnable {
    private List<Record> record_list;
    private Context context;
    private String tag;

    Writer( Context context, List<Record> records, String tag) {
        record_list = records;
        this.context = context;
        this.tag = tag;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getPrivateDocumentStorageDir(Context context, String documentName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), documentName);
//        if (!file.mkdirs()) {
//            Log.e("zhongc", "Directory not created");
//        }
        return file;
    }

    public void run()
    {
        Date current_date = Calendar.getInstance().getTime();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH").format(current_date);
        int minute = current_date.getMinutes();
        minute = minute / 20 * 20;
        timeStamp = timeStamp + String.format("%02d", minute);
        String filename = tag+timeStamp+".txt";

        if (isExternalStorageWritable()) {
            File file = getPrivateDocumentStorageDir(context, filename);
            Log.d("zhongc", file.getAbsolutePath());
            try {
                FileOutputStream outputStream = new FileOutputStream(file, true);
                for (Record record:record_list) {
                    outputStream.write(record.toString().getBytes());
                }
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Log.d("zhongc", "external storage is not writable!");
        }
//        FileOutputStream outputStream;
//
//        try {
//            outputStream = this.context.openFileOutput(filename, Context.MODE_PRIVATE | Context.MODE_APPEND);
//            Log.d("zhongc", "outputStream created, record_list.size()=" + record_list.size());
//            for (Record record:record_list) {
//                outputStream.write(record.toString().getBytes());
//            }
//
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
