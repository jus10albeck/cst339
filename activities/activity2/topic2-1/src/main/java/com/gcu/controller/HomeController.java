package com.gcu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController
{
	/**
	 * Simple Hello World Controller that returns a View name along with a model Attribute named message.
	 * Invoke using /page2 URI
	 * @param model Model to bind to the view
	 * 
	 * @return View name hello
	 */
	@GetMapping("/page1")
	public String printHome(Model model)
	{
		// Simply return a model with an attribute named home
		// and return a View named hello using a passed in ModelMap
		model.addAttribute("message", " Welcome to CST-339 Topic 2 Activity");
		return "home";
	}
}
