package com.vtence.molecule;

import com.vtence.molecule.helpers.Charsets;
import com.vtence.molecule.helpers.Headers;
import com.vtence.molecule.helpers.Streams;
import com.vtence.molecule.http.AcceptLanguage;
import com.vtence.molecule.http.ContentType;
import com.vtence.molecule.http.Cookie;
import com.vtence.molecule.http.HeaderNames;
import com.vtence.molecule.http.HttpMethod;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.vtence.molecule.http.HeaderNames.CONTENT_LENGTH;
import static java.lang.Long.parseLong;

/**
 * Holds client request information and maintains attributes during the request lifecycle.
 *
 * Information includes body, headers, parameters, cookies, locale.
 */
public class Request {

    private final Headers headers = new Headers();
    private final Map<String, Cookie> cookies = new LinkedHashMap<String, Cookie>();
    private final Map<String, List<String>> parameters = new LinkedHashMap<String, List<String>>();
    private final Map<Object, Object> attributes = new HashMap<Object, Object>();

    private String uri;
    private String path;
    private String ip;
    private int port;
    private String hostName;
    private String protocol;
    private InputStream input;
    private HttpMethod method;
    private boolean secure;
    private long timestamp;

    public Request() {}

    /**
     * Reads the uri of this request.
     *
     * @return the request uri
     */
    public String uri() {
        return uri;
    }

    /**
     * Changes the uri of this request.
     *
     * @param uri the new uri
     */
    public Request uri(String uri) {
        this.uri = uri;
        return this;
    }

    /**
     * Reads the path of this request.
     *
     * @return the request path
     */
    public String path() {
        return path;
    }

    /**
     * Changes the path of this request.
     *
     * @param path the new path
     */
    public Request path(String path) {
        this.path = path;
        return this;
    }

    /**
     * Reads the ip of the remote client of this request.
     *
     * @return the remote client ip
     */
    public String remoteIp() {
        return ip;
    }

    /**
     * Changes the ip of the remote client of this request.
     *
     * @param ip the new ip
     */
    public Request remoteIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * Reads the hostname of the remote client of this request.
     *
     * @return the remote client hostname
     */
    public String remoteHost() {
        return hostName;
    }

    /**
     * Changes the hostname of the remote client of this request.
     *
     * @param hostName the new hostname
     */
    public Request remoteHost(String hostName) {
        this.hostName = hostName;
        return this;
    }

    /**
     * Reads the port of the remote client of this request.
     *
     * @return the remote client port
     */
    public int remotePort() {
        return port;
    }

    /**
     * Changes the port of the remote client of this request.
     *
     * @param port the new port
     */
    public Request remotePort(int port) {
        this.port = port;
        return this;
    }

    /**
     * Reads the protocol of this request.
     *
     * @return the request protocol
     */
    public String protocol() {
        return protocol;
    }

    /**
     * Changes the protocol of this request.
     *
     * @param protocol the new protocol
     */
    public Request protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    /**
     * Indicates if this request was done over a secure connection, such as SSL.
     *
     * @return true if the request was transferred securely
     */
    public boolean secure() {
        return secure;
    }

    /**
     * Changes the secure state of this request, which indicates if the request was done over a secure channel.
     *
     * @param secure the new state
     */
    public Request secure(boolean secure) {
        this.secure = secure;
        return this;
    }

    /**
     * Indicates the time in milliseconds when this request was received.
     *
     * @return the time the request arrived at
     */
    public long timestamp() {
        return timestamp;
    }

    /**
     * Changes the timestamp of this request.
     *
     * @param time the new timestamp
     */
    public Request timestamp(long time) {
        timestamp = time;
        return this;
    }

    /**
     * Indicates the HTTP method with which this request was made (e.g.  GET, POST, PUT, etc.).
     *
     * @return the request HTTP method
     */
    public HttpMethod method() {
        return method;
    }

    /**
     * Changes the HTTP method of this request by name. Method name is case-insensitive and must refer to
     * one of the {@link com.vtence.molecule.http.HttpMethod}s.
     *
     * @param methodName the new method name
     */
    public Request method(String methodName) {
        return method(HttpMethod.valueOf(methodName));
    }

    /**
     * Changes the HTTP method of this request.
     * Method must refer to one of the {@link com.vtence.molecule.http.HttpMethod}s. Matching is done case insensitively.
     *
     * @param method the new method
     */
    public Request method(HttpMethod method) {
        this.method = method;
        return this;
    }

    /**
     * Provides the text body of this request.
     *
     * @return the text that makes up the body
     */
    public String body() throws IOException {
        return new String(bodyContent(), charset());
    }

    /**
     * Provides the body of this request as an array of bytes.
     *
     * @return the bytes content of the body
     */
    public byte[] bodyContent() throws IOException {
        return Streams.toBytes(bodyStream());
    }

    /**
     * Provides the body of this request as an {@link java.io.InputStream}.
     *
     * @return the stream of bytes that make up the body
     */
    public InputStream bodyStream() {
        return input;
    }

    /**
     * Changes the body of this request.
     *
     * Note that this will not affect the list of parameters that might have been sent with the original
     * body as POST parameters.
     *
     * @param body the new body as a string
     */
    public Request body(String body) {
        return body(body.getBytes(charset()));
    }

    /**
     * Changes the body of this request.
     *
     * Note that this will not affect the list of parameters that might have been sent with the original
     * body as POST parameters.
     *
     * @param content the new body content as an array of bytes
     */
    public Request body(byte[] content) {
        this.input = new ByteArrayInputStream(content);
        return this;
    }

    /**
     * Changes the body of this request.
     *
     * Note that this will not affect the list of parameters that might have been sent with the original
     * body as POST parameters.
     *
     * @param input the new body content as an array of bytes
     */
    public Request body(InputStream input) {
        this.input = input;
        return this;
    }

    /**
     * Provides the charset used in the body of this request.
     * The charset is read from the <code>Content-Type</code> header.
     *
     * @return the charset of this request or null if <pre>Content-Type</pre> is missing.
     */
    public Charset charset() {
        ContentType contentType = ContentType.of(this);
        if (contentType == null || contentType.charset() == null) {
            return Charsets.ISO_8859_1;
        }
        return contentType.charset();
    }

    /**
     * Checks for the presence of a specific HTTP message header in this request.
     *
     * @param name the name of header to check
     * @return true if the header was set
     */
    public boolean hasHeader(String name) {
        return headers.has(name);
    }

    /**
     * Gets the value of the specified header sent with this request. The name is case insensitive.
     *
     * <p>
     * In case there are multiple headers with that name, a comma separated list of values is returned.
     * </p>
     *
     * This method returns a null value if the request does not include any header of the specified name.
     *
     * @param name the name of the header to retrieve
     * @return the value of the header
     */
    public String header(String name) {
        return headers.get(name);
    }

    /**
     * Gets all the header names sent with this request. If the request has no header, the set will be empty.
     * <p>
     * Note that the names are provided as originally set on the request.
     * Modifications to the provided set are safe and will not affect the request.
     * </p>
     *
     * @return a set containing all the header names sent, which might be empty
     */
    public Set<String> headerNames() {
        return headers.names();
    }

    /**
     * Gets the list of all the values sent with this request under the specified header.
     * The name is case insensitive.
     *
     * <p>
     * Some headers can be sent by clients as several headers - each with a different value - rather than sending
     * the header as a comma separated list.
     * </p>
     *
     * If the request does not include any header of the specified name, the list is empty.
     * Modifications to the provided list are safe and will not affect the request.
     *
     * @param name the name header of the header to retrieve
     * @return the list of values of the header
     */
    public List<String> headers(String name) {
        return headers.list(name);
    }

    /**
     * Adds an HTTP message header to this request. The new value will be added to the list
     * of existing values for that header name.
     *
     * @param name the name of the header to be added
     * @param value the value the header will have
     */
    public Request addHeader(String name, String value) {
        headers.add(name, value);
        return this;
    }

    /**
     * Sets an HTTP message header on this request. The new value will replace existing values for that header name.
     *
     * @param name the name of the header to set
     * @param value the value the header will have
     */
    public Request header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    /**
     * Removes the value of the specified header on this request. The name is case insensitive.
     *
     * <p>
     * In case there are multiple headers with that name, all values are removed.
     * </p>
     *
     * @param name the name of the header to remove
     */
    public Request removeHeader(String name) {
        headers.remove(name);
        return this;
    }

    /**
     * Checks if a cookie with a specific name was sent with this request.
     *
     * Note that changing the Cookie header of the request will not cause the cookies to change. To change the cookies,
     * use {@link Request#cookie(String, String)} and {@link Request#removeCookie(String)}.
     *
     * @see Request#cookie(com.vtence.molecule.http.Cookie)
     * @param name the name of the cookie to check
     * @return true if the cookie was sent
     */
    public boolean hasCookie(String name) {
        return cookies.containsKey(name);
    }

    /**
     * Retrieves the value of a cookie sent with this request under a specific name.
     *
     * If the cookie exists as an HTTP header then it's value is returned, otherwise a null value is returned.
     *
     * @param name name of the cookie to acquire
     * @return the value of the cookie with that name or null
     */
    public String cookieValue(String name) {
        Cookie cookie = cookie(name);
        return cookie != null ? cookie.value() : null;
    }

    /**
     * Retrieves a cookie sent with this request under a specific name.
     *
     * If the cookie exists as an HTTP header then it is returned
     * as a <code>Cookie</code> object - with the name, value, path as well as the optional domain part.
     *
     * @param name the name of the cookie to acquire
     * @return the cookie with that name or null
     */
    public Cookie cookie(String name) {
        return cookies.get(name);
    }

    /**
     * Retrieves the list of all cookies sent with this request.
     *
     * If the cookie exists as an HTTP header then it is returned
     * as a <code>Cookie</code> object - with the name, value, path as well as the optional domain part.
     *
     * Note that the list is safe for modification, it will not affect the request.
     *
     * @return the list of cookies sent
     */
    public List<Cookie> cookies() {
        return new ArrayList<Cookie>(cookies.values());
    }

    /**
     * Sets a cookie with a specific name and value. If a cookie with that name already exists,
     * it is replaced by the new value.
     *
     * Note that this will not affect the Cookie header that has been set with the request.
     *
     * @param name the name of the cookie to set
     * @param value the value of the cookie to set
     */
    public Request cookie(String name, String value) {
        return cookie(new Cookie(name, value));
    }

    /**
     * Sets a cookie on this request. If a cookie with the same name already exists, it is replaced by the new cookie.
     *
     * Note that this will not affect the Cookie header that has been set with the request.
     *
     * @param cookie the cookie to set
     */
    public Request cookie(Cookie cookie) {
        cookies.put(cookie.name(), cookie);
        return this;
    }

    /**
     * Removes a cookie with a specific name from this request. If no cookie with that name exists, the operation
     * does nothing.
     *
     * Note that this will not affect the Cookie header that has been set with the request.
     *
     * @param name the name of the cookie to remove
     */
    public Request removeCookie(String name) {
        cookies.remove(name);
        return this;
    }

    /**
     * Reads the value of the Content-Length header sent as part of this request. If the Content-Length
     * header is missing, the value -1 is returned.
     *
     * @return the Content-Length header value as a long or -1
     */
    public long contentLength() {
        String value = header(CONTENT_LENGTH);
        return value != null ? parseLong(value) : -1;
    }

    /**
     * Reads the value of the Content-Type header sent as part of this request. If the Content-Type
     * header is missing, a null value is returned.
     *
     * @return the Content-Type header or null
     */
    public String contentType() {
        ContentType contentType = ContentType.of(this);
        return contentType != null ? contentType.mediaType() : null;
    }

    /**
     * Gets the value of a parameter of this request, or null if the parameter does not exist.
     *
     * If the parameter has more than one value, the first one is returned.
     *
     * <p>Request parameters are contained in the query string or posted form data.</p>
     *
     * Note that changing the body of the request will not cause the parameters to change. To change the request
     * parameters, use {@link Request#addParameter(String, String)} and {@link Request#removeParameter(String)}.
     *
     * @param name the name of the parameter
     * @return the parameter value or null
     */
    public String parameter(String name) {
        List<String> values = parameters(name);
        return values.isEmpty() ?  null : values.get(values.size() - 1);
    }

    public List<String> parameters(String name) {
        return parameters.containsKey(name) ? new ArrayList<String>(parameters.get(name)) : new ArrayList<String>();
    }

    public Set<String> parameterNames() {
        return new LinkedHashSet<String>(parameters.keySet());
    }

    public Map<String, List<String>> allParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public Request addParameter(String name, String value) {
        if (!parameters.containsKey(name)) {
            parameters.put(name, new ArrayList<String>());
        }
        parameters.get(name).add(value);
        return this;
    }

    public Request removeParameter(String name) {
        parameters.remove(name);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T attribute(Object key) {
        return (T) attributes.get(key);
    }

    public Request attribute(Object key, Object value) {
        attributes.put(key, value);
        return this;
    }

    public Set<Object> attributeNames() {
        return new HashSet<Object>(attributes.keySet());
    }

    public Object removeAttribute(Object key) {
        return attributes.remove(key);
    }

    public Map<Object, Object> attributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public Locale locale() {
        List<Locale> locales = locales();
        return locales.isEmpty() ? null : locales.get(0);
    }

    public List<Locale> locales() {
        return AcceptLanguage.of(this).locales();
    }
}