/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ditherstop.app.config;

/**
 * Created with IntelliJ IDEA.
 * User: Takashi Matsuo <tmatsuo@google.com>
 * Date: 4/5/13
 * Time: 2:57 AM
 */

import com.ditherstop.app.rest.DitherStopServlet;
import com.ditherstop.app.scraper.SteamScraper;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.core.Application;

public class Resources extends Application {
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  @Override
  public Set<Class<?>> getClasses() {
    // start the scrape and let it run once a day
    final SteamScraper scraper = new SteamScraper();
    final ScheduledFuture<?> scraperHandle = scheduler.scheduleAtFixedRate(scraper, 0, 1, TimeUnit.DAYS);
    Set<Class<?>> s = new HashSet<>();
    s.add(DitherStopServlet.class);
    return s;
  }
}
