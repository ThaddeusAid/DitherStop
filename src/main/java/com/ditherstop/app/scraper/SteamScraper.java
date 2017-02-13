package com.ditherstop.app.scraper;

import com.github.goive.steamapi.SteamApi;
import com.github.goive.steamapi.exceptions.SteamApiException;

import java.util.Map;

/**
 * Created by aid on 2/12/2017.
 */
public class SteamScraper {

  public static void main(String [] args) {
    SteamScraper scraper = new SteamScraper();
    scraper.run();
  }

  public void run() {
    // Country codes are always 2 letter. Also possible to use the getCountry() method from Locale
    SteamApi steamApi = new SteamApi("US");
    Map<Integer, String> appList;

    // get all apps
    try {
      // Retrieves a list of all possible steam appIds along with name, in case you want to pre-check
      appList = steamApi.listApps();
    } catch (SteamApiException e) {
      // Exception needs to be thrown here in case of invalid appId or service downtime
      e.printStackTrace();
    }

    // scrape information from store.steampowered.com
    try {

    } catch (Exception e){
      e.printStackTrace();
    }
  }
}
