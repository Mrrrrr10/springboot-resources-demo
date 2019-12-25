package com.nolookblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Mrrrrr10
 * @github https://github.com/Mrrrrr10
 * @blog https://nolookblog.com/
 * @description
 */

@Controller
public class IndexController {

	@GetMapping("/static")
	public String getStaticIndex(){
		return "index.html";
	}

	@GetMapping("/templates")
	public String getTemplatesIndex(){
		return "index";
	}

	@GetMapping("/get")
	public String get(){
		return "index.html";
	}

}
