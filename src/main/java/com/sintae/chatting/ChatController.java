package com.sintae.chatting;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sintae.vo.UserVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ChatController {
	
	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
	private UserVO user;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String login() {
		logger.info("Welcome login!.");
		return "login";
	}
	
	/*
	 * chatting page
	 */
	@RequestMapping(value = "/chatting", method = RequestMethod.GET)
	public String chatting(Model model) {
		logger.info("Welcome chatting!.");
		model.addAttribute("user",user);
		return "chatting";
	}
	

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginCheck(@RequestParam("uname") String name, @RequestParam("pswd") String pswd) {
		logger.info("login");
		if(name.equals("kimsintae")&&pswd.equals("1234")) {
			user = new UserVO(name,pswd);
		}else if(name.equals("ramos")&&pswd.equals("54321")) {
			user = new UserVO(name,pswd);
		}else if(name.equals("sony")&&pswd.equals("qwer")){
			user = new UserVO(name,pswd);
		}else {
			return "login";
		}
		return "redirect:chatting";
	}
}
