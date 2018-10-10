package com.sound.controller.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sound.controller.base.BaseController;

@Controller
public class IndexController extends BaseController {

	@RequestMapping("/")
	public String main() throws Exception {
		return "index";
	}
	
}
