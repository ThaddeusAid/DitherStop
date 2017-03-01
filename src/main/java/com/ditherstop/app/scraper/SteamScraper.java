package com.ditherstop.app.scraper;

import com.ditherstop.app.utils.SteamGame;
import com.github.goive.steamapi.SteamApi;
import com.github.goive.steamapi.exceptions.SteamApiException;
import org.apache.log4j.Level;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

/**
 * Created by aid on 2/12/2017.
 */
public class SteamScraper implements Runnable{
  private static final Logger log = Logger.getLogger(SteamScraper.class.getName());

  public static void main(String[] args) {
    SteamScraper scraper = new SteamScraper();
    scraper.run();
  }

  public void run() {
    log.info("Starting Scraping Service");
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
    Collections.shuffle(Arrays.asList(keys));

    Map<String, String> cookies = new HashMap<String, String>();
    cookies.put("mature_content", "1");
    cookies.put("birthtime", "185961601");
    cookies.put("lastagecheckage", "23-November-1975");

    try {
      Connection con = null;
      Statement statement = null;
      PreparedStatement preparedStatement = null;

      Class.forName("org.sqlite.JDBC");
      con = DriverManager.getConnection("jdbc:sqlite:ditherstop.db");
      log.info("Opened database successfully");

      statement = con.createStatement();

      String query = "create table if not exists Games (id INT PRIMARY KEY NOT NULL, name TEXT NOT NULL, positive INT, negative INT, ratio REAL);";
      statement.executeUpdate(query);
      query = "create table if not exists Tags (id INT NOT NULL, tag TEXT NOT NULL);";
      statement.executeUpdate(query);
      query = "create table if not exists Genres (id INT NOT NULL, genre TEXT NOT NULL);";
      statement.executeUpdate(query);

      for (int i = 0; i < keys.length; i++) {
        if(i % 1000 == 0){ // track the progress in the logs
          log.info("Scraped: " + i + " of " + keys.length);
        }

        try {
          Document gamePage;
          try {
            gamePage = Jsoup.connect("http://store.steampowered.com/app/" + (Integer) keys[i] + "/").userAgent("Mozilla").cookies(cookies).get();
          } catch (Exception e) {
            e.printStackTrace();
            continue;
          }
          Elements tags = gamePage.getElementsByClass("app_tag");

          // extract tags
          query = "delete from Tags where id = " + (Integer) keys[i] + ";";
          statement.executeUpdate(query);

          query = "INSERT INTO Tags (id, tag) values (?, ?);";
          preparedStatement = con.prepareStatement(query);
          for (int j = 0; j < tags.size(); j++) {
            if (tags.get(j).tag().toString().equalsIgnoreCase("a")) {
              preparedStatement.setInt(1, (Integer) keys[i]);
              preparedStatement.setString(2, tags.get(j).text());
              preparedStatement.addBatch();
            }
          }
          int[] updateCounts = preparedStatement.executeBatch();
          for (int j = 0; j < updateCounts.length; j++) {
            if (!(updateCounts[j] >= 0)) {
              log.info("Error! Tags not entered correctly!");
            }
          }

          // extract genres
          query = "delete from Genres where id = " + (Integer) keys[i] + ";";
          statement.executeUpdate(query);

          query = "INSERT INTO Genres (id, genre) values (?, ?);";
          preparedStatement = con.prepareStatement(query);

          Elements details = gamePage.getElementsByClass("details_block");
          for (int j = 0; j < details.size(); j++) {
            Elements aTags = details.get(j).getElementsByTag("a");
            for (int k = 0; k < aTags.size(); k++) {
              if (aTags.get(k).toString().contains("genre")) {
                preparedStatement.setInt(1, (Integer) keys[i]);
                preparedStatement.setString(2, aTags.get(k).text());
                preparedStatement.addBatch();
              }
            }
          }
          updateCounts = preparedStatement.executeBatch();
          for (int j = 0; j < updateCounts.length; j++) {
            if (!(updateCounts[j] >= 0)) {
              log.info("Error! Genres not entered correctly!");
            }
          }

          // extract reviews
          int alltimePositive = 0;
          Elements positiveReviews = gamePage.getElementsByAttributeValue("for", "review_type_positive");
          if (positiveReviews.size() > 0) {
            String temp = positiveReviews.get(0).getElementsByClass("user_reviews_count").text().toString().replaceAll(",", "");
            temp = temp.substring(1, temp.length() - 1);
            alltimePositive = Integer.parseInt(temp);
          }

          int alltimeNegative = 0;
          Elements negativeReviews = gamePage.getElementsByAttributeValue("for", "review_type_negative");
          if (negativeReviews.size() > 0) {
            String temp = negativeReviews.get(0).getElementsByClass("user_reviews_count").text().toString().replaceAll(",", "");
            temp = temp.substring(1, temp.length() - 1);
            alltimeNegative = Integer.parseInt(temp);
          }

          double alltimeRatio = (double) alltimePositive / ((double) alltimeNegative + 10);

          query = "REPLACE INTO Games (id, name, positive, negative, ratio) values (?, ?, ?, ?, ?);";
          preparedStatement = con.prepareStatement(query);
          preparedStatement.setInt(1, (Integer) keys[i]);
          preparedStatement.setString(2, appList.get(keys[i]));
          preparedStatement.setInt(3, alltimePositive);
          preparedStatement.setInt(4, alltimeNegative);
          preparedStatement.setDouble(5, alltimeRatio);
          updateCounts = preparedStatement.executeBatch();
          for (int j = 0; j < updateCounts.length; j++) {
            if (!(updateCounts[j] >= 0)) {
              log.info("Error! Genres not entered correctly!");
            }
          }
          Thread.sleep(1000);
        }catch(Exception e){
          e.printStackTrace();
        }
      }

      preparedStatement.close();
      statement.close();
      con.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
