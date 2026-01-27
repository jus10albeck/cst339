package com.gcu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController
{
	@GetMapping("/")
	public String display(Model model)
	{
		model.addAttribute("title", "Home Page");
		return "home";
	}
	
	
}