package com.beingdev.shortner.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.beingdev.shortner.pojo.ShortenRequest;
import com.beingdev.shortner.service.URLConverterService;
import com.beingdev.shortner.utils.URLValidator;

@RestController
public class URLController {
	private static final Logger LOGGER = LoggerFactory.getLogger(URLController.class);
	private final URLConverterService urlConverterService;

	public URLController(URLConverterService urlConverterService) {
		this.urlConverterService = urlConverterService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String initREST() {
		return "<center><h2>Welcome to CHOOTU REST API</h2></center>";
	}

	@RequestMapping(value = "/shortener", method = RequestMethod.POST, consumes = { "application/json" })
	public String shortenUrl(@RequestBody @Valid final ShortenRequest shortenRequest, HttpServletRequest request)
			throws Exception {

		String longUrl = shortenRequest.getUrl();
		String customUrl = shortenRequest.getCustomUrl();

		LOGGER.info("Received urls to shorten : " + longUrl + " and Custom Url : " + customUrl);

		if (URLValidator.INSTANCE.validateURL(longUrl)) {
			String localURL = request.getRequestURL().toString();
			LOGGER.debug("Local URL: " + localURL);
			String shortenedUrl = urlConverterService.shortenURL(localURL, longUrl, customUrl);
			LOGGER.debug("Shortened url to: " + shortenedUrl);
			return shortenedUrl;
		}
		throw new Exception("Please enter a valid URL");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public RedirectView redirectUrl(@PathVariable String id, HttpServletRequest request, HttpServletResponse response)
			throws IOException, URISyntaxException, Exception {
		LOGGER.info("Received shortened url to redirect: " + id);
		String redirectUrlString = urlConverterService.getLongURLFromID(id);
		LOGGER.debug("Original URL: " + redirectUrlString);
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(redirectUrlString);
		return redirectView;
	}
	
	@RequestMapping(value = "/hits", method = RequestMethod.GET)
	public String getTotalHits(){
		LOGGER.info("Get total Number of hits");
		return urlConverterService.getHits();
	}
}
