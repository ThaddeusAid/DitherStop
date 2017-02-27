package com.ditherstop.app.scraper;

import com.ditherstop.app.utils.SteamGame;
import com.github.goive.steamapi.SteamApi;
import com.github.goive.steamapi.exceptions.SteamApiException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by aid on 2/12/2017.
 */
public class SteamScraper {

  public static void main(String[] args) {
    SteamScraper scraper = new SteamScraper();
    scraper.run();
  }

  public void run() {
    // Country codes are always 2 letter. Also possible to use the getCountry() method from Locale
    SteamApi steamApi = new SteamApi("US");
    Map<Integer, String> appList = new HashMap<Integer, String>();

    // get all apps
    try {
      // Retrieves a list of all possible steam appIds along with name, in case you want to pre-check
      appList = steamApi.listApps();
    } catch (SteamApiException e) {
      // Exception needs to be thrown here in case of invalid appId or service downtime
      e.printStackTrace();
    }

    // scrape information from store.steampowered.com
    Object[] keys = appList.keySet().toArray();
    SteamGame[] games = new SteamGame[keys.length];

    System.out.println(keys.length + " first " + (Integer) keys[0]);
    Map<String, String> cookies = new HashMap<String, String>();
    cookies.put("mature_content", "1");
    cookies.put("birthtime", "185961601");
    cookies.put("lastagecheckage", "23-November-1975");

    try {
//      for (int i = 0; i < keys.length; i++){
      SteamGame newGame = new SteamGame((Integer) keys[0], appList.get(keys[0]));
      System.out.println("" + newGame.name);
      Document gamePage = Jsoup.connect("http://store.steampowered.com/app/" + (Integer) keys[0] + "/").userAgent("Mozilla").cookies(cookies).get();
      Elements tags = gamePage.getElementsByClass("app_tag");

      // extract tags
      newGame.tags = new ArrayList<String>();
      for (int j = 0; j < tags.size(); j++) {
        if (tags.get(j).tag().toString().equalsIgnoreCase("a")) {
          newGame.tags.add(tags.get(j).text());
        }
      }

      // extract genres
      Elements details = gamePage.getElementsByClass("details_block");
      newGame.genres = new ArrayList<String>();
      for (int j = 0; j < details.size(); j++){
        Elements aTags = details.get(j).getElementsByTag("a");
        for(int k = 0; k < aTags.size(); k++){
          if(aTags.get(k).toString().contains("genre")){
            newGame.genres.add(aTags.get(k).text());
          }
        }
      }

      // extract reviews
      Elements positiveReviews = gamePage.getElementsByAttributeValue("for", "review_type_positive");
      String temp = positiveReviews.get(0).getElementsByClass("user_reviews_count").text().toString().replaceAll(",", "");
      temp = temp.substring(1, temp.length() - 1);
      newGame.alltimePositive = Integer.parseInt(temp);

      Elements negativeReviews = gamePage.getElementsByAttributeValue("for", "review_type_negative");
      temp = negativeReviews.get(0).getElementsByClass("user_reviews_count").text().toString().replaceAll(",", "");
      temp = temp.substring(1, temp.length() - 1);
      newGame.alltimeNegative = Integer.parseInt(temp);

      newGame.makeAlltimeRatio();

      System.out.println("" + newGame.alltimeRatio);

//      System.out.println(details);
//      System.out.println(reviews);

//      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
