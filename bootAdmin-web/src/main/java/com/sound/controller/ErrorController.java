package com.sound.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ErrorController {

	@RequestMapping(value="error404")
	public String error404 () {
		return "common/error404";
	}
	@RequestMapping(value="error500")
	public String error500 () {
		return "common/error";
	}
	@RequestMapping(value="default")
	public String defaultPage () {
		return "default";
	}
}
