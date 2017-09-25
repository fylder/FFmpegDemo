# FFmpegDemo

视频转码的处理

手动打包so

```
# 进入 main 目录
cd lib/src/main
# 生成 .so 文件
ndk-build NDK_PROJECT_PATH=. NDK_APPLICATION_MK=./cpp/Application.mk NDK_LIBS_OUT=./jniLibs
```

> 引用的项目 https://github.com/leixiaohua1020/simplest_ffmpeg_mobile 
