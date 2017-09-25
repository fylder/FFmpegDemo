package fylder.ffmpeg.lib;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.util.Date;

/**
 * Created by fylder on 2017/9/22.
 */

public class FFmpegManage {

    AsyncTask<Void, Void, String> task;

    public void trans(final String path, final TransListener listener) {

        task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                FFmpegNative ffmpegNative = new FFmpegNative();
                String transCodingPath = getTransCodingPath();
                String cmdline = getLine(path, transCodingPath);
                String[] argv = cmdline.split(" ");
                Integer argc = argv.length;
                try {
                    int code = ffmpegNative.ffmpegcore(argc, argv);
                    if (code == 0) {
                        return transCodingPath;
                    } else {
                        return "error";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (listener != null) {
                    if (s != null) {
                        if (TextUtils.equals(s, "error")) {
                            listener.error();
                        } else {
                            listener.success(s);
                        }
                    } else {
                        listener.error();
                    }
                }
            }
        };
        task.execute();
        if (listener != null) {
            listener.start();
        }
    }

    private String getLine(String sourcePath, String transCodingPath) {
        return "ffmpeg -i " + sourcePath + " -threads:1 4 -r 24 -s 480x480 " + transCodingPath;

    }

    private String getTransCodingPath() {
        String path = "/storage/emulated/0/";
        return path + "trans_" + new Date().getTime() + ".mp4";
    }

    public interface TransListener {

        void start();

        void success(String transCoding);

        void error();
    }
}
