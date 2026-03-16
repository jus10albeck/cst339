package com.gcu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController
{
	/**
	 * Displays the login form. It sets the title attribute in the 
	 * model to "Login Form" and returns the view name "login" to be rendered.
	 * @param model
	 * @return
	 */
	@GetMapping("/")
	public String display(Model model)
	{
		model.addAttribute("title", "Login Form");
		return "login";
	}
}