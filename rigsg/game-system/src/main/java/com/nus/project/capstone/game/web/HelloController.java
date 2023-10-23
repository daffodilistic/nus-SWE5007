package com.nus.project.capstone.game.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {

	@GetMapping("/")
	public String index() {
		return "Welcome to game.ric-robotics.xyz!";
	}

}