package com.beingdev.shortner.repository;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.beingdev.shortner.config.SpringConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Repository
public class URLRepository {
	
	private JedisPool reJedis;
    private final String idKey;
    private final String urlKey;
    private static final Logger LOGGER = LoggerFactory.getLogger(URLRepository.class);

    public URLRepository() {
    	reJedis = new SpringConfig().getJedisPool();
        this.idKey = "id";
        this.urlKey = "url:";
    }
    
    private Jedis getJedisInstance() {
    	return new SpringConfig().getJedisPool().getResource();
    }

    public URLRepository(Jedis jedis, String idKey, String urlKey) {
        this.idKey = idKey;
        this.urlKey = urlKey;
    }

    public Long incrementID() {
        Long id = reJedis.getResource().incr(idKey);
        LOGGER.info("Incrementing ID: {}", id-1);
        return id - 1;
    }

    public void saveUrl(String key, String longUrl) {
        LOGGER.info("Saving: {} at {}", longUrl, key);
        reJedis.getResource().hset(urlKey, key, longUrl);
    }

    public String getUrl(Long id) throws Exception {
        LOGGER.info("Retrieving at {}", id);
        String url = reJedis.getResource().hget(urlKey, "url:"+id);
        LOGGER.info("Retrieved {} at {}", url ,id);
        if (url == null) {
            throw new Exception("URL at key" + id + " does not exist");
        }
        return url;
    }
   
}
