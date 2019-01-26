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
		String uniqueID;
		String baseString = formatLocalURLFromShortener(localURL);

		isCustomUrl = isCustomUrlSent(customUrl, customUrlId, id);
		uniqueID = createUniqueIDStoreUrl(customUrl, longUrl, customUrlId, id);

		return baseString + uniqueID;
	}

	private String createUniqueIDStoreUrl(String customUrl, String longUrl, Long customUrlId, Long id) {
		LOGGER.info("Inside createUniqueIDStoreUrl");
		LOGGER.info(customUrl + " : " + longUrl + " : " + customUrlId + " : " + id);
		if (isCustomUrl && urlRepository.isCustomUrlAvailable(customUrlId)) {
			saveURL(customUrlId, longUrl);
			return IDConverter.createUniqueID(customUrlId);
		} else {
			saveURL(id, longUrl);
			return IDConverter.createUniqueID(id);
		}
	}

	private boolean isCustomUrlSent(String customUrl, Long customUrlId, Long id) {
		if (!customUrl.equals(null)) {
			customUrlId = IDConverter.convertCustomurltoBase10ID(customUrl);
			LOGGER.info("Custom Url ID : " + customUrlId + " Normal Url ID : " + id);
			return true;
		}
		return false;
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
			LOGGER.debug("Domain Name: " + domain);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return domain;
	}

}
