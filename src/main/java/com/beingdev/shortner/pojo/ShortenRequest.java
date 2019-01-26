package com.beingdev.shortner.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShortenRequest {
	private String url;
	private String customUrl;

	@JsonCreator
	public ShortenRequest() {

	}

	public String getCustomUrl() {
		return customUrl;
	}

	public void setCustomUrl(String customUrl) {
		this.customUrl = customUrl;
	}

	@JsonCreator
	public ShortenRequest(@JsonProperty("url") String url, @JsonProperty("customUrl") String customUrl) {
		this.url = url;
		this.customUrl = customUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
