package com.humaniq.apilib.react;

import android.text.TextUtils;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.google.gson.JsonObject;
import com.humaniq.apilib.Constants;
import com.humaniq.apilib.network.models.request.FcmCredentials;
import com.humaniq.apilib.network.models.response.BaseResponse;
import com.humaniq.apilib.network.service.providerApi.ServiceBuilder;
import com.humaniq.apilib.storage.Prefs;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ognev on 7/29/17.
 */

public class TokenModule extends ReactContextBaseJavaModule {

  public TokenModule(ReactApplicationContext reactContext) {
    super(reactContext);
    new Prefs(reactContext);
  }

  @Override public String getName() {
    return "HumaniqTokenApiLib";
  }

  /**
   token

   account_id

   facial_image_id

   password

   device_imei
   */

  @ReactMethod public void saveCredentials(ReadableMap credentials, Promise promise) {
    Prefs.saveJwtToken(credentials.getString("token"));
    Prefs.saveAccountId(credentials.getString("account_id"));
    Prefs.facialImageId(credentials.getString("facial_image_id"));
    Prefs.savePassword(credentials.getString("password"));
    Prefs.deviceImei(credentials.getString("device_imei"));

    WritableMap writableMap = new WritableNativeMap();
    writableMap.putString("status", "saved: " + Prefs.getJwtToken());
    try {
      sendRegistrationToServer();
    } catch (Exception e) {
      e.printStackTrace();
    }

    promise.resolve(writableMap);
  }


  private void sendRegistrationToServer() {
    if(!TextUtils.isEmpty(Prefs.getFCMToken())  && !Prefs.isFcmSent()) {
      ServiceBuilder.init(Constants.BASE_URL, getReactApplicationContext());

      FcmCredentials fcmCredentials = new FcmCredentials();
      fcmCredentials.setAccountId(Long.valueOf(Prefs.getAccountId()));
      fcmCredentials.setToken(Prefs.getFCMToken());

      ServiceBuilder.getFcmService().saveFcmToken(fcmCredentials).enqueue(new Callback<BaseResponse<Object>>() {
        @Override public void onResponse(Call<BaseResponse<Object>> call, Response<BaseResponse<Object>> response) {
          //if(response.code() == 200) {
          //  WritableMap writableMap = new WritableNativeMap();
          //  writableMap.putInt("code", 200);
          //  writableMap.putString("accountId", Prefs.getAccountId());
          //  writableMap.putString("fcm", Prefs.getFCMToken());
            Prefs.setFcmSent(true);
          //  promise.resolve(writableMap);
          //}
          //else {
          //  WritableMap writableMap = new WritableNativeMap();
          //  writableMap.putInt("code", response.code()  );
          //  writableMap.putString("accountId", Prefs.getAccountId());
          //  writableMap.putString("fcm", Prefs.getFCMToken());
          //  try {
          //    writableMap.putString("message", response.errorBody().string());
          //  } catch (Exception e) {
          //    e.printStackTrace();
          //  }
          //  promise.resolve(writableMap);
          //}
        }

        @Override public void onFailure(Call<BaseResponse<Object>> call, Throwable t) {
          //WritableMap writableMap = new WritableNativeMap();
          //writableMap.putString("status", "onFailure");
          //writableMap.putInt("code", 400);
          //writableMap.putString("accountId", Prefs.getAccountId());
          //writableMap.putString("fcm", Prefs.getFCMToken());
          //promise.resolve(writableMap);
        }
      });
    } else {
      //WritableMap writableMap = new WritableNativeMap();
      //writableMap.putString("fcm", Prefs.getFCMToken());
      //writableMap.putString("status", "already sent!");
      //promise.resolve(writableMap);
    }
  }

  @ReactMethod public void getFCMToken(Promise promise) {
    new Prefs(getReactApplicationContext());
    WritableMap writableMap = new WritableNativeMap();
    writableMap.putString("token", Prefs.getFCMToken());
    promise.resolve(writableMap);
  }

  @ReactMethod public void getJwtToken(Promise promise) {
    WritableMap jwtMap = new WritableNativeMap();
    jwtMap.putString("token", Prefs.getJwtToken());
    promise.resolve(jwtMap);
  }
  @ReactMethod public void getAccountId(Promise promise) {
    WritableMap jwtMap = new WritableNativeMap();
    jwtMap.putString("token", Prefs.getAccountId());
    promise.resolve(jwtMap);
  }

  @ReactMethod public void getPassword(Promise promise) {
    WritableMap passwordMap = new WritableNativeMap();
    passwordMap.putString("password", Prefs.getPassword());
    promise.resolve(passwordMap);
  }


  @ReactMethod public void savePassword(String password, Promise promise) {
    Prefs.savePassword(password);
    WritableMap passwordMap = new WritableNativeMap();
    passwordMap.putString("password", Prefs.getPassword());
    promise.resolve(passwordMap);
  }
}
