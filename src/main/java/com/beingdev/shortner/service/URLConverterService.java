package com.beingdev.shortner.service;

import java.net.MalformedURLException;
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
	private boolean isCustomUrl;

	@Autowired
	public URLConverterService(URLRepository urlRepository) {
		this.urlRepository = urlRepository;
	}

	public String shortenURL(String localURL, String longUrl, String customUrl) {
		LOGGER.info("Shortening {}", longUrl);
		Long id = urlRepository.incrementID();
		Long customUrlId = null;
		
		if (!customUrl.equals(null)) {
			customUrlId = IDConverter.convertCustomurltoBase10ID(customUrl);
			LOGGER.info("Custom Url ID : " + customUrlId + " Normal Url ID : " + id);
			isCustomUrl = true;
		}
		
		if (isCustomUrl && urlRepository.isCustomUrlAvailable(customUrlId)) {
			saveURL(customUrlId, longUrl);
		} else {
			saveURL(id, longUrl);
		}
		String uniqueID = IDConverter.createUniqueID(id);
		String baseString = formatLocalURLFromShortener(localURL);
		String shortenedURL = baseString + uniqueID;
		return shortenedURL;
	}

	public void saveURL(Long id, String longUrl) {
		String urlToSave;
		URL uri = null;
		String protocol;
		try {
			uri = new URL(longUrl);
			protocol = uri.getProtocol();
			urlToSave = protocol + "://" + uri.getAuthority();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			urlToSave = "http://" + longUrl;
		}
		LOGGER.debug("URL Saving in dB {}", urlToSave);
		urlRepository.saveUrl("url:" + id, urlToSave);
	}

	public String getLongURLFromID(String uniqueID) throws Exception {
		Long dictionaryKey = IDConverter.getDictionaryKeyFromUniqueID(uniqueID);
		String longUrl = urlRepository.getUrl(dictionaryKey);
		LOGGER.debug("Converting shortened URL back to {}", longUrl);
		return longUrl;
	}

	private String formatLocalURLFromShortener(String localURL) {
		LOGGER.debug("Local URL received: " + localURL);
		String domain = "";
		try {
			URL uri = new URL(localURL);
			domain = uri.getProtocol() + "://" + uri.getAuthority() + "/";
			LOGGER.info("Domain Name: " + domain);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return domain;
	}

}
