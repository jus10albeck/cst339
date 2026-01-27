package com.gcu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.model.UserRegistrationModel;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController
{
	@GetMapping("/")
	public String display(Model model)
	{
		model.addAttribute("title", "Registration Form");

		if (!model.containsAttribute("registrationModel")) 
		{
            model.addAttribute("registrationModel", new UserRegistrationModel());
        }

		return "registration";
	}
	
	@PostMapping("/doRegistration")
	public String doRegistration(
			@Valid @ModelAttribute("registrationModel")
			UserRegistrationModel registrationModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model)
	{
		if (bindingResult.hasErrors())
		{
			model.addAttribute("title", "Registration Form");
			return "registration";
		}
		
		redirectAttributes.addFlashAttribute("message", "Registration successful! Please log in.");
		return "redirect:/login/";
	}
}
