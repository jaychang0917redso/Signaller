package com.jaychang.signaller.ui;

import android.content.Context;

import com.jaychang.signaller.util.LangUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public class BaseActivity extends RxAppCompatActivity {

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(LangUtils.wrap(newBase));
  }
}
