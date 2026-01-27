package com.gcu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.model.LoginModel;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController
{
	
	@GetMapping("/")
	public String display(Model model)
	{
		model.addAttribute("title", "Login Form");
		model.addAttribute("loginModel", new LoginModel());
		return "login";
	}
	

	@PostMapping("/doLogin")
	public String doLogin(
		@Valid @ModelAttribute("loginModel") 
		LoginModel loginModel,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model) 
	{
		if (bindingResult.hasErrors()) 
    	{
        	model.addAttribute("title", "Login Form");
        	return "login";
    	}
    
    	redirectAttributes.addFlashAttribute("message", "Welcome back!");

    	return "redirect:/home/";
	}

}