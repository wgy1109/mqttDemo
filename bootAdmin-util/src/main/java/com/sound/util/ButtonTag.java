package com.sound.util;


import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class ButtonTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String url ;
	
	@Override
	public int doStartTag() throws JspException {
		HttpSession session = this.pageContext.getSession();
		String menuStr =session.getAttribute(Const.SESSION_AUTH_MENU_STR) == null ? "" :  session.getAttribute(Const.SESSION_AUTH_MENU_STR).toString();
		if(url != null && !url.equals("")){
			if(menuStr.contains(url)){
				return TagSupport.EVAL_BODY_AGAIN;
			}
		}
		return TagSupport.SKIP_BODY;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
