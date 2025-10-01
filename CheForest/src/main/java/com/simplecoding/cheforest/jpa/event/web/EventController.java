package com.simplecoding.cheforest.jpa.event.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EventController {

	@GetMapping("/event/events")
	public String showEvent() {
		
		return "event/events";
	}

	@GetMapping("/event/test")
	public String showEventTest() {

		return "event/test";
	}
}