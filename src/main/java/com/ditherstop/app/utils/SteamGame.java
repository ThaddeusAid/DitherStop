package com.ditherstop.app.utils;

import java.util.ArrayList;

/**
 * Created by aid on 2/12/2017.
 */
public class SteamGame {
  public int id; // reusing Steam Ids
  public String name; // Using Steam names
  public ArrayList<String> genres; // Using Steam genres
  public ArrayList<String> tags; // Using Steam tags
  public int alltimePositive; // Using Steam ratings
  public int alltimeNegative; // Using Steam ratings
  public int recentPositive; // Using Steam ratings
  public int recentNegative; // Using Steam ratings
  public double alltimeRatio; // Using Steam ratings
  public double recentRatio; // Using Steam ratings
  public double derivedRatio; // Using Steam ratings

  public SteamGame (int id, String name) {
    this.id = id;
    this.name = name;
  }

  public void makeAlltimeRatio() {
    this.alltimeRatio = (double) this.alltimePositive / ((double) this.alltimeNegative + 10.0);
  }

  public void makeRecentRatio() {
    this.recentRatio = (double) this.recentPositive / ((double) this.recentNegative + 10.0);
  }

  public void makeDerivedRatio() {
    this.derivedRatio = this.alltimeRatio / (this.recentRatio + 1.0);
  }

  public void makeRatios() {
    this.makeAlltimeRatio();
    this.makeRecentRatio();
    this.makeDerivedRatio();
  }
}
