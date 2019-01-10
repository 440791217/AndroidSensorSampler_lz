package space.xingzhi.accelaration;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


class Logger {

    private List<Record> record_ping = new ArrayList<>();
    private List<Record> record_pong = new ArrayList<>();
    private int flush_num;
    private String tag;
    private Context context;
    private boolean use_ping = true;

    Logger(Context context, int flush_num, String tag) {
        this.context = context;
        this.flush_num = flush_num;
        this.tag = tag;
    }

    void log(float x, float y, float z) {
//        Log.d("zhongc", "entered log");
        if (use_ping)
        {
            record_ping.add(new Record(x, y, z));
            if (record_ping.size() == flush_num) {
                // output
                Log.d("zhongc", "ping start write");
                Writer writer = new Writer(this.context, this.record_ping, this.tag);
                Thread t = new Thread(writer, "write ping thread");
                t.start();
                // use pong
                use_ping = false;
                // clear
                record_pong.clear();
            }
        }
        else
        {
            record_pong.add(new Record(x, y, z));
            if (record_pong.size() == flush_num) {
                // output
                Log.d("zhongc", "pong start write");
                Writer writer = new Writer(this.context, this.record_pong, this.tag);
                Thread t = new Thread(writer, "write pong thread");
                t.start();
                // use ping
                use_ping = true;
                // clear
                record_ping.clear();
            }
        }

    }

}
