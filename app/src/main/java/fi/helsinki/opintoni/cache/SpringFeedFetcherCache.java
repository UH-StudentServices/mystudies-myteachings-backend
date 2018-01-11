/*
 * This file is part of MystudiesMyteaching application.
 *
 * MystudiesMyteaching application is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MystudiesMyteaching application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MystudiesMyteaching application.  If not, see <http://www.gnu.org/licenses/>.
 */

package fi.helsinki.opintoni.cache;

import com.rometools.fetcher.impl.FeedFetcherCache;
import com.rometools.fetcher.impl.SyndFeedInfo;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import java.net.URL;
import java.util.Optional;

public class SpringFeedFetcherCache implements FeedFetcherCache {

    private final String cacheName;

    private final CacheManager inMemoryCacheManager;

    private final Cache cache;

    public SpringFeedFetcherCache(String cacheName, CacheManager inMemoryCacheManager) {
        this.cacheName = cacheName;
        this.inMemoryCacheManager = inMemoryCacheManager;
        this.cache = inMemoryCacheManager.getCache(cacheName);
    }

    @Override
    public SyndFeedInfo getFeedInfo(URL feedUrl) {
        return get(feedUrl);
    }

    @Override
    public void setFeedInfo(URL feedUrl, SyndFeedInfo syndFeedInfo) {
        cache.put(feedUrl, syndFeedInfo);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public SyndFeedInfo remove(URL feedUrl) {
        SyndFeedInfo syndFeedInfo = get(feedUrl);
        cache.evict(feedUrl);
        return syndFeedInfo;
    }

    private SyndFeedInfo get(URL feedUrl) {
        Optional<ValueWrapper> valueWrapper = Optional.ofNullable(cache.get(feedUrl));

        return valueWrapper
            .map(w -> (SyndFeedInfo) w.get())
            .orElse(null);
    }
}
