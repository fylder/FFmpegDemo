package fylder.ffmpeg.lib;

/**
 * Created by fylder on 2017/9/22.
 */

public class FFmpegNative {

    public native int ffmpegcore(int argc, String[] argv);

    static {
        System.loadLibrary("avutil-54");
        System.loadLibrary("swresample-1");
        System.loadLibrary("avcodec-56");
        System.loadLibrary("avformat-56");
        System.loadLibrary("swscale-3");
        System.loadLibrary("postproc-53");
        System.loadLibrary("avfilter-5");
        System.loadLibrary("avdevice-56");
        System.loadLibrary("sfftranscoder");
    }
}
