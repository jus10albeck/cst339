package com.gcu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gcu.business.RegistrationBusinessService;
import com.gcu.model.UserRegistrationModel;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController
{
	private final RegistrationBusinessService registrationService;
	
	/**
	 * Constructor for RegistrationController. It initializes the 
	 * controller with a RegistrationBusinessService which is used to handle the registration logic.
	 * @param registrationService
	 */
	public RegistrationController (RegistrationBusinessService registrationService)
	{
		this.registrationService = registrationService;
	}
	
	/**
	 * Displays the registration form. It sets the title attribute in the model to 
	 * "Registration Form" and returns the view name "registration" to be rendered. If the model does 
	 * not already contain a "registrationModel" attribute, it adds a new UserRegistrationModel 
	 * to the model to be used in the registration form.	
	 * @param model
	 * @return
	 */
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
	
	/**
	 * Handles the registration process. It validates the input data from the registration
	 *  form and checks for any errors. If there are validation errors, it prepares the model 
	 * to display the registration form again with the appropriate error messages. If the 
	 * input is valid, it attempts to register the user using the RegistrationBusinessService. If the 
	 * registration is successful, it redirects to the login page with a success message. If the 
	 * registration fails, it prepares the model to display the registration form again with an error message.	
	 * @param registrationModel
	 * @param bindingResult
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
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
		boolean ok = registrationService.register(registrationModel);
		if (ok)
		{
			redirectAttributes.addFlashAttribute("message", "Registration successful! Please Log in");
			return "redirect:/login/";
		}
		model.addAttribute("title", "Registration Form");
		model.addAttribute("authError", "Registration failed");
		return "registration";

	}
}
