package com.gcu.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/hello")
public class HelloWorldController
{
	/**
	 * Simple Hello World Controller that returns a string in the response body.
	 * Invoke using /test1 URI
	 * 
	 * @return Hello World test string
	 */
	@GetMapping("/test1")
	@ResponseBody
	public String printHello()
	{
		// Return String in the response body (Must use @ResponseBody annotation)
		return "Hello World!";
	}
	
	/**
	 * Simple Hello World Controller that returns a View name along with a model Attribute named message.
	 * Invoke using /test2 URI
	 * @param model Model to bind to the view
	 * 
	 * @return View name hello
	 */
	@GetMapping("/test2")
	public String printHello(Model model)
	{
		// Simply return a model with an attribute named message
		// and return a View named hello using a passed in ModelMap
		model.addAttribute("message", "Hello Spring MVC Framework!");
		return "hello";
	}
	 
	/**
	 * Simple Hello World Controller that returns a View name along with a model Attribute named message.
	 * Invoke using /test3 URI
	 *
	 * @return ModelAndView with both the Model and the View to render
	 */
	@GetMapping("/test3")
	public ModelAndView printHello1()
	{
		// Create a ModelAndView and then set an attribute named message and with a View named hello
		ModelAndView mv = new ModelAndView();
		mv.addObject("message", new String("Hello World from ModelAndView!"));
		mv.addObject("message2", new String("Another hello world from ModelAndView!"));
		mv.setViewName("hello");
		return mv;
	}
	
	/**
	 * Simple Hello World Controller that returns a View name along with a model Attribute named message.
	 * Invoke using /test4 URI
	 * @param message HTTP Parameter named message to add to the Model for Rendering
	 * @param model Model to bind to the view
	 * 
	 * @return View name hello
	 */
	@GetMapping("/test4")
	public String printHello(@RequestParam("message") String message, Model model)
	{
		// Simply return a model with an attribute named message and return a View named hello
		model.addAttribute("message", message);
		return "hello";
	}
}