package com.gcu.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.business.LoginBusinessService;
import com.gcu.model.LoginModel;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController
{
	private final LoginBusinessService loginService;
	
	public LoginController(LoginBusinessService loginService)
	{
		this.loginService = loginService;
	}
	
	@GetMapping("/")
	public String display(Model model)
	{
		model.addAttribute("title", "Login Form");
		model.addAttribute("loginModel", new LoginModel(null, null));
		return "login";
	}
	

	@PostMapping("/doLogin")
	public String doLogin(
		@Valid @ModelAttribute("loginModel") 
		LoginModel loginModel,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model, HttpSession session) 
	{
		if (bindingResult.hasErrors()) 
    	{
        	model.addAttribute("title", "Login Form");
        	return "login";
    	}
		
		return loginService.authenticate(loginModel.getUsername(), loginModel.getPassword())
				.map(user -> {
					session.setAttribute("USER", user);
					redirectAttributes.addFlashAttribute("message", "Welcome Back!");
					return "redirect:/collections/";
				})
				.orElseGet(() -> {
					model.addAttribute("title", "Login Form");
					model.addAttribute("authError", "Invalid Username or password.");
					return "login";
				});
				
		
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes ra)
	{
		session.invalidate();
		ra.addFlashAttribute("message", "You have been logged out");
		return "redirect:/login/";
	}
}