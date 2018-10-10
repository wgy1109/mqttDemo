package com.sound.exceptionResolver;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class WebExceptionResolver implements HandlerExceptionResolver {
	
	private static Logger log = Logger.getLogger(WebExceptionResolver.class);

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		String exceptionMessage = getErrorInfoFromException(ex);
		log.error(exceptionMessage);
		ModelAndView mv = new ModelAndView("common/error");
		mv.addObject("exception", ex.toString().replaceAll("\n", "<br/>"));
		mv.addObject("exceptionMessage",exceptionMessage);
		return mv;
	}

	private static String getErrorInfoFromException(Exception e) {  
        try {  
            StringWriter sw = new StringWriter();  
            PrintWriter pw = new PrintWriter(sw);  
            e.printStackTrace(pw);  
            return "\r\n" + sw.toString() + "\r\n";  
        } catch (Exception e2) {  
            return "bad getErrorInfoFromException";  
        }  
    }  
}
