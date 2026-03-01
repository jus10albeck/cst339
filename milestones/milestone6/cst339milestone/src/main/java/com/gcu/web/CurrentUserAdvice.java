package com.gcu.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@Component
public class CurrentUserAdvice 
{
    @ModelAttribute("currentUsername")
    public String currentUsername(Authentication auth) 
    {
        return (auth != null) ? auth.getName() : null;
    }
}