package fylder.ffmpeg.demo;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import fylder.ffmpeg.demo.tools.FileTools;
import fylder.ffmpeg.lib.FFmpegManage;

public class DemoActivity extends AppCompatActivity {

    final int SELECT_CODE = 100;

    private TextView fileText;
    private TextView transFileText;
    ProgressBar loadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initView();
    }

    void initView() {
        fileText = (TextView) findViewById(R.id.demo_file_text);
        transFileText = (TextView) findViewById(R.id.demo_play_text);
        loadView = (ProgressBar) findViewById(R.id.demo_loading);

        findViewById(R.id.demo_select).setOnClickListener(view -> {
            //选择文件
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SELECT_CODE);

//            Matisse.from(DemoActivity.this)
//                    .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
//                    .countable(true)
//                    .maxSelectable(1)
////                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
////                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
//                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                    .thumbnailScale(0.85f)
//                    .imageEngine(new GlideEngine())
//                    .forResult(SELECT_CODE);
        });
        findViewById(R.id.demo_trans).setOnClickListener(view -> {
            //转码
            RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instance
            // Must be done during an initialization phase like onCreate
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) { // Always true pre-M
                            // I can control the camera now
                            trans();
                        } else {
                            // Oups permission denied
                            Log.w("123", "没有权限");
                        }
                    });

        });
        findViewById(R.id.demo_play).setOnClickListener(v -> {
            String path = transFileText.getText().toString().trim();
            if (TextUtils.isEmpty(path)) {
                Toast.makeText(this, "please to trans code", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri = Uri.parse(path);
            //调用系统自带的播放器
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "video/mp4");
            startActivity(intent);
        });
    }

    void trans() {
        String path = fileText.getText().toString().trim();
        File file = new File(path);
        if (file.isFile()) {
            FFmpegManage fFmpegManage = new FFmpegManage();
            fFmpegManage.trans(path, new FFmpegManage.TransListener() {
                @Override
                public void start() {
                    Log.w("123", "start");
                    loadView.setVisibility(View.VISIBLE);
                }

                @Override
                public void success(String transCoding) {
                    Log.w("123", "success transCoding:" + transCoding);
                    loadView.setVisibility(View.GONE);
                    transFileText.setText(transCoding);
                }

                @Override
                public void error() {
                    Log.w("123", "error");
                    loadView.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(this, "path error", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = FileTools.getImageAbsolutePath(this, uri);
            Log.w("123", "path:" + path);
            fileText.setText(path);
//            List<Uri> uris = Matisse.obtainResult(data);
//            List<String> paths = Matisse.obtainPathResult(data);
//            for (String s : paths) {
//                Log.w("test", s);
//            }
        }

    }
}
