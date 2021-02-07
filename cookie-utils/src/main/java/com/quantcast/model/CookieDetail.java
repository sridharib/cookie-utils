package com.quantcast.model;

import java.time.LocalDateTime;

public final class CookieDetail {

	private CookieDetail() {
	}

	private CookieDetail(String cookieName, LocalDateTime cookieTime) {
		this.cookieName = cookieName;
		this.cookieTime = cookieTime;
	}

	private String cookieName;
	private LocalDateTime cookieTime;

	public LocalDateTime getCookieTime() {
		return cookieTime;
	}

	public String getCookieName() {
		return cookieName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cookieName == null) ? 0 : cookieName.hashCode());
		result = prime * result + ((cookieTime == null) ? 0 : cookieTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CookieDetail other = (CookieDetail) obj;
		if (cookieName == null) {
			if (other.cookieName != null)
				return false;
		} else if (!cookieName.equals(other.cookieName))
			return false;
		if (cookieTime == null) {
			if (other.cookieTime != null)
				return false;
		} else if (!cookieTime.equals(other.cookieTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CookieDetail [cookieName=" + cookieName + ", cookieTime=" + cookieTime + "]";
	}

	public static class CookieDetailBuilder {

		private String cookieName;
		private LocalDateTime cookieTime;

		public CookieDetailBuilder cookieName(String cookieName) {
			this.cookieName = cookieName;
			return this;
		}

		public CookieDetailBuilder cookieTime(LocalDateTime cookieTime) {
			this.cookieTime = cookieTime;
			return this;
		}

		public CookieDetail build() {
			return new CookieDetail(cookieName, cookieTime);
		}
	}

}
