package com.simplecoding.cheforest.jpa.event.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EventController {

	@GetMapping("/event/events")
	public String showEventTest() {
		
		return "event/events";
	}
}