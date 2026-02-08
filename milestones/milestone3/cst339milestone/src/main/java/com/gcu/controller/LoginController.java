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
		
		Optional<LoginModel> match = loginService.getLogin().stream()
				.filter(u -> u.getUsername().equals(loginModel.getUsername())
						&& u.getPassword().equals(loginModel.getPassword()))
				.findFirst();
		
		if (match.isPresent()) 
		{
			session.setAttribute("USER", match.get().getUsername());
			
			redirectAttributes.addFlashAttribute("message", "Welcome back!");

			return "redirect:/collections/";
		}
		
		model.addAttribute("title", "Login Form");
		model.addAttribute("authError", "Invalid Username or password.");
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes ra)
	{
		session.invalidate();
		ra.addFlashAttribute("message", "You have been logged out");
		return "redirect:/login/";
	}
}