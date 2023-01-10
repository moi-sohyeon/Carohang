package com.example.cafetotravel;

import static com.kakao.sdk.common.KakaoSdk.init;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //호출 초기화 - 네이티브 앱키
        init(this,"a893f3deb3c9e9e578cac5848dc4f155");

    }
}
