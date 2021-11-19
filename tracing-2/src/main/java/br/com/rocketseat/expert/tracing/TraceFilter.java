package br.com.rocketseat.expert.tracing;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.autoconfig.instrument.web.SleuthWebProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Order(SleuthWebProperties.TRACING_FILTER_ORDER + 5)
class TraceFilter extends GenericFilterBean {
	
	private final Tracer tracer;
	
	TraceFilter(Tracer tracer) {
        this.tracer = tracer;
    }
	
	@Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            this.tagBasicInfo(servletRequest, servletResponse);
            filterChain.doFilter(servletRequest, servletResponse);
        }catch (Exception exception) {
            Span currentSpan = this.tracer.currentSpan();
            currentSpan.tag("error.message", exception.getMessage());
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
	
	 private void tagBasicInfo(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
	        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
	        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);
	        Span currentSpan = this.tracer.currentSpan();

	        currentSpan.tag("result.status", String.valueOf(responseWrapper.getStatus()));
	        currentSpan.tag("url", new URL(httpServletRequest.getRequestURL().toString()).toString());
	        currentSpan.tag("uuid.request", "teste123");
	        currentSpan.tag("environment", "dev");
	        currentSpan.tag("host", InetAddress.getLocalHost().getHostName());
	        currentSpan.tag("received.from.address", "");
	        currentSpan.tag("request.header", getRequestHeaders(httpServletRequest));

	        if(httpServletRequest.getQueryString() != null) {
	            currentSpan.tag("query.params", httpServletRequest.getQueryString());
	        }
	    }
	 
	 	private String getRequestHeaders(HttpServletRequest request) {
	        HashMap<String, String> map = new HashMap<>();
	        Enumeration<String> headerNames = request.getHeaderNames();
	        while (headerNames.hasMoreElements()) {
	            String key = headerNames.nextElement();
	            map.put(key, request.getHeader(key));
	        }
	        return map.toString();
	    }


}
