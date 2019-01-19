package com.beingdev.shortner.repository;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

@Repository
public class URLRepository {
    private final Jedis jedis;
    private final String idKey;
    private final String urlKey;
    private static final Logger LOGGER = LoggerFactory.getLogger(URLRepository.class);

    public URLRepository() throws URISyntaxException {
    	
    	JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(1);
    	
    	String redistogoUrl = System.getenv("REDISTOGO_URL");
    	URI uri = new URI(redistogoUrl);
    	
    	this.jedis = new Jedis(uri, 0, 0);
    	
        this.idKey = "id";
        this.urlKey = "url:";
    }

    public URLRepository(Jedis jedis, String idKey, String urlKey) {
        this.jedis = jedis;
        this.idKey = idKey;
        this.urlKey = urlKey;
    }

    public Long incrementID() {
        Long id = jedis.incr(idKey);
        LOGGER.info("Incrementing ID: {}", id-1);
        return id - 1;
    }

    public void saveUrl(String key, String longUrl) {
        LOGGER.info("Saving: {} at {}", longUrl, key);
        jedis.hset(urlKey, key, longUrl);
    }

    public String getUrl(Long id) throws Exception {
        LOGGER.info("Retrieving at {}", id);
        String url = jedis.hget(urlKey, "url:"+id);
        LOGGER.info("Retrieved {} at {}", url ,id);
        if (url == null) {
            throw new Exception("URL at key" + id + " does not exist");
        }
        return url;
    }
   
}
