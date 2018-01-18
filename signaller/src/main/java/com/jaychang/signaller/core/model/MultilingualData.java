package com.jaychang.signaller.core.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MultilingualData extends RealmObject {
  @PrimaryKey
  private long id;
  @SerializedName("en")
  public String en;
  @SerializedName("zh")
  public String zh;
}
