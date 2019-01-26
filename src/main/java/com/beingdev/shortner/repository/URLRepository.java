package com.beingdev.shortner.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.beingdev.shortner.config.SpringConfig;

import redis.clients.jedis.Jedis;

@Repository
public class URLRepository {

	private final Jedis reJedis;
	private final String idKey;
	private final String urlKey;
	private static final Logger LOGGER = LoggerFactory.getLogger(URLRepository.class);

	public URLRepository() {
		reJedis = new SpringConfig().getJedisPool().getResource();
		this.idKey = "id";
		this.urlKey = "url:";
	}

	public URLRepository(Jedis jedis, String idKey, String urlKey) {
		this.reJedis = jedis;
		this.idKey = idKey;
		this.urlKey = urlKey;
	}

	public Long incrementID() {
		Long id = reJedis.incr(idKey);
		LOGGER.debug("Incrementing ID: {}", id - 1);
		return id - 1;
	}

	public void saveUrl(String key, String longUrl) {
		LOGGER.debug("Saving: {} at {}", longUrl, key);
		reJedis.hset(urlKey, key, longUrl);
	}

	public String getUrl(Long id) throws Exception {
		LOGGER.info("Retrieving at {}", id);
		String url = reJedis.hget(urlKey, "url:" + id);
		LOGGER.debug("Retrieved {} at {}", url, id);
		if (url == null) {
			throw new Exception("URL at key" + id + " does not exist");
		}
		return url;
	}

	public boolean isCustomUrlAvailable(Long customUrlId) {
		LOGGER.info("Inside isCustomUrlAvailable" + customUrlId);
		if (reJedis.hget(urlKey, "url:" + customUrlId) == null) {
			LOGGER.info("Inside if of isCustomURLAvailable");
			return true;
		}
		return false;
	}
}
