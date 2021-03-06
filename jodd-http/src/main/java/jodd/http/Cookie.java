// Copyright (c) 2003-present, Jodd Team (jodd.org). All Rights Reserved.

package jodd.http;

/**
 * Cookie object. Simple cookie data holder, cookie header parser and generator.
 */
public class Cookie {

	// the value of the cookie itself

	private String name;
	private String value;

	// attributes encoded in the header's cookie fields

	private String comment;				// ;Comment=VALUE ... describes cookie's use
										// ;Discard ... implied by maxAge < 0
	private String domain;				// ;Domain=VALUE ... domain that sees cookie
	private Integer maxAge;				// ;Max-Age=VALUE ... cookies auto-expire
	private String expires;				// ;Expires= ... expires values
	private String path;				// ;Path=VALUE ... URLs that see the cookie
	private boolean secure;				// ;Secure ... e.g. use SSL
	private Integer version; 			// ;Version=1 ... means RFC 2109++ style
	private boolean httpOnly;			// ;HttpOnly


	/**
	 * Creates cookie with specified name and value.
	 * <p>
	 * The name must conform to RFC 2109. That means it can contain
	 * only ASCII alphanumeric characters and cannot contain commas,
	 * semicolons, or white space or begin with a $ character.
	 * <p>
	 * The value can be anything the server chooses to send.
	 */
	public Cookie(String name, String value) {
		setName(name);
		setValue(value);
	}

	/**
	 * Parses cookie data from given user-agent string.
	 */
	public Cookie(String cookie) {
		int from = 0;
		int ndx = 0;

		while (ndx < cookie.length()) {
			ndx = cookie.indexOf(';', from);

			if (ndx == -1) {
				// last chunk
				ndx = cookie.length();
			}

			int ndx2 = cookie.indexOf('=', from);

			String name;
			String value;
			if (ndx2 != -1 && ndx2 < ndx) {
				name = cookie.substring(from, ndx2).trim();
				value = cookie.substring(ndx2 + 1, ndx).trim();
			} else {
				name = cookie.substring(from, ndx).trim();
				value = null;
			}

			if (name.equalsIgnoreCase("Max-Age")) {
				setMaxAge(Integer.parseInt(value));
			} else if (name.equalsIgnoreCase("Comment")) {
				setComment(value);
			} else if (name.equalsIgnoreCase("Domain")) {
				setDomain(value);
			} else if (name.equalsIgnoreCase("Path")) {
				setPath(value);
			} else if (name.equalsIgnoreCase("Secure")) {
				setSecure(true);
			} else if (name.equalsIgnoreCase("Version")) {
				setVersion(Integer.parseInt(value));
			} else if (name.equalsIgnoreCase("HttpOnly")) {
				setHttpOnly(true);
			} else if (name.equalsIgnoreCase("Expires")) {
				setExpires(value);
			} else {
				setName(name);
				setValue(value);
			}

			// continue
			from = ndx + 1;
		}
	}

	/**
	 * Sets the cookie name and checks for validity.
	 */
	private void setName(String name) {
		if (name.contains(";") || name.contains(",") || name.startsWith("$")) {
			throw new IllegalArgumentException("Invalid cookie name:" + name);
		}

		for (int n = 0; n < name.length(); n++) {
			char c = name.charAt(n);
			if (c <= 0x20 || c >= 0x7f) {
				throw new IllegalArgumentException("Invalid cookie name:" + name);
			}
		}
		this.name = name;
	}

	/**
	 * Returns the comment describing the purpose of this cookie, or
	 * <code>null</code> if the cookie has no comment.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Specifies a comment that describes a cookie's purpose.
	 * The comment is useful if the browser presents the cookie
	 * to the user.
	 */
	public void setComment(String purpose) {
		comment = purpose;
	}

	/**
	 * Returns the domain name set for this cookie. The form of
	 * the domain name is set by RFC 2109.
	 */

	public String getDomain() {
		return domain;
	}

	/**
	 * Specifies the domain within which this cookie should be presented.
	 * <p>
	 * The form of the domain name is specified by RFC 2109. A domain
	 * name begins with a dot (<code>.foo.com</code>) and means that
	 * the cookie is visible to servers in a specified Domain Name System
	 * (DNS) zone (for example, <code>www.foo.com</code>, but not
	 * <code>a.b.foo.com</code>). By default, cookies are only returned
	 * to the server that sent them.
	 */

	public void setDomain(String pattern) {
		domain = pattern.toLowerCase();    // IE allegedly needs this
	}

	/**
	 * Returns the maximum age of the cookie, specified in seconds,
	 * By default, <code>-1</code> indicating the cookie will persist
	 * until browser shutdown.
	 */

	public Integer getMaxAge() {
		return maxAge;
	}

	/**
	 * Sets the maximum age of the cookie in seconds.
	 * <p>
	 * A positive value indicates that the cookie will expire
	 * after that many seconds have passed. Note that the value is
	 * the <i>maximum</i> age when the cookie will expire, not the cookie's
	 * current age.
	 * <p>
	 * A negative value means
	 * that the cookie is not stored persistently and will be deleted
	 * when the Web browser exits. A zero value causes the cookie
	 * to be deleted.
	 */

	public void setMaxAge(int expiry) {
		maxAge = Integer.valueOf(expiry);
	}

	/**
	 * Returns the path on the server
	 * to which the browser returns this cookie. The
	 * cookie is visible to all subpaths on the server.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Specifies a path for the cookie
	 * to which the client should return the cookie.
	 * <p>
	 * The cookie is visible to all the pages in the directory
	 * you specify, and all the pages in that directory's subdirectories.
	 * A cookie's path must include the servlet that set the cookie,
	 * for example, <i>/catalog</i>, which makes the cookie
	 * visible to all directories on the server under <i>/catalog</i>.
	 *
	 * <p>Consult RFC 2109 (available on the Internet) for more
	 * information on setting path names for cookies.
	 */
	public void setPath(String uri) {
		path = uri;
	}

	/**
	 * Returns <code>true</code> if the browser is sending cookies
	 * only over a secure protocol, or <code>false</code> if the
	 * browser can send cookies using any protocol.
	 */
	public boolean isSecure() {
		return secure;
	}

	/**
	 * Indicates to the browser whether the cookie should only be sent
	 * using a secure protocol, such as HTTPS or SSL.
	 */
	public void setSecure(boolean flag) {
		secure = flag;
	}

	/**
	 * Returns the name of the cookie. The name cannot be changed after
	 * creation.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the value of the cookie.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Assigns a new value to a cookie after the cookie is created.
	 * If you use a binary value, you may want to use BASE64 encoding.
	 */
	public void setValue(String newValue) {
		value = newValue;
	}

	/**
	 * Returns the version of the protocol this cookie complies
	 * with. Version 1 complies with RFC 2109,
	 * and version 0 complies with the original
	 * cookie specification drafted by Netscape. Cookies provided
	 * by a browser use and identify the browser's cookie version.
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * Sets the version of the cookie protocol this cookie complies
	 * with. Version 0 complies with the original Netscape cookie
	 * specification. Version 1 complies with RFC 2109.
	 */
	public void setVersion(int version) {
		this.version = Integer.valueOf(version);
	}

	public boolean isHttpOnly() {
		return httpOnly;
	}

	public void setHttpOnly(boolean httpOnly) {
		this.httpOnly = httpOnly;
	}

	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}

	public String toString() {
		StringBuilder cookie = new StringBuilder();

		cookie.append(name).append('=').append(value);

		if (maxAge != null) {
			cookie.append("; Max-Age=").append(maxAge);
		}
		if (expires != null) {
			cookie.append("; Expires=").append(expires);
		}
		if (comment != null) {
			cookie.append("; Comment=").append(comment);
		}
		if (domain != null) {
			cookie.append("; Domain=").append(domain);
		}
		if (path != null) {
			cookie.append("; Path=").append(path);
		}
		if (secure == true) {
			cookie.append("; Secure");
		}
		if (version != null) {
			cookie.append("; Version=").append(version);
		}
		if (httpOnly) {
			cookie.append("; HttpOnly");
		}

		return cookie.toString();
	}

}
