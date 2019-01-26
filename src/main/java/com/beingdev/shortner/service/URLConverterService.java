package com.beingdev.shortner.service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beingdev.shortner.repository.URLRepository;
import com.beingdev.shortner.utils.IDConverter;

@Service
public class URLConverterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(URLConverterService.class);
    private final URLRepository urlRepository;

    @Autowired
    public URLConverterService(URLRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String shortenURL(String localURL, String longUrl) {
        LOGGER.info("Shortening {}", longUrl);
        Long id = urlRepository.incrementID();
        String uniqueID = IDConverter.createUniqueID(id);
        urlRepository.saveUrl("url:"+id, longUrl);
        String baseString = formatLocalURLFromShortener(localURL);
        String shortenedURL = baseString + uniqueID;
        return shortenedURL;
    }

    public String getLongURLFromID(String uniqueID) throws Exception {
        Long dictionaryKey = IDConverter.getDictionaryKeyFromUniqueID(uniqueID);
        String longUrl = urlRepository.getUrl(dictionaryKey);
        LOGGER.info("Converting shortened URL back to {}", longUrl);
        return longUrl;
    }

    private String formatLocalURLFromShortener(String localURL) {
    	LOGGER.info("Local URL received: " + localURL);
    	String domain = "";
    	try {
			URL uri = new URL(localURL);
			String protocol = uri.getProtocol();
			if(protocol.equals(null)) {
				domain = "http://" + uri.getAuthority() + "/";
			}else {
				domain = uri.getProtocol() + "://" + uri.getAuthority() + "/";
			}
			LOGGER.info("Domain Name: " + domain);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        return domain;
    }

}
