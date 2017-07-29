package com.humaniq.apilib.react;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.humaniq.apilib.storage.Prefs;

/**
 * Created by ognev on 7/29/17.
 */

public class TokenModule extends ReactContextBaseJavaModule {

  public TokenModule(ReactApplicationContext reactContext) {
    super(reactContext);
    new Prefs(reactContext);
  }

  @Override public String getName() {
    return "TokenModule";
  }

  @ReactMethod public void saveJwtToken(String jwtToken, Promise promise) {
    Prefs.saveJwtToken(jwtToken);
    WritableMap writableMap = new WritableNativeMap();
    writableMap.putString("status", "saved");
    promise.resolve(writableMap);
  }
}
