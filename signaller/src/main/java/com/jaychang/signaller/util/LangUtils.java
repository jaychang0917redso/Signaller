package com.jaychang.signaller.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;

import java.util.Locale;

public class LangUtils {

  public static Context wrap(Context context) {
    Context ctx = context;
    Locale locale = getCurLang(context);
    Locale.setDefault(locale);

    Resources res = ctx.getResources();
    Configuration config = new Configuration(res.getConfiguration());
    if (Build.VERSION.SDK_INT >= 17) {
      config.setLocale(locale);
      ctx = context.createConfigurationContext(config);
    } else {
      config.locale = locale;
      res.updateConfiguration(config, res.getDisplayMetrics());
    }

    return ctx;
  }

  public static Locale getCurLang(Context context) {
    String lang = context.getSharedPreferences("Preference", 0).getString("lang", "");
    if (!TextUtils.isEmpty(lang)) {
      return new Locale(lang);
    } else {
      return Locale.getDefault();
    }
  }

}
