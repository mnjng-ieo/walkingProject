package com.walk.aroundyou.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.walk.aroundyou.domainenum.BoardType;
import com.walk.aroundyou.dto.BoardRequest;
import com.walk.aroundyou.dto.IBoardDetailResponse;
import com.walk.aroundyou.dto.IBoardListResponse;
import com.walk.aroundyou.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainViewController {


	@GetMapping("/")
	public String getMain() {
		return "main";
	}
	
	@PostMapping("/")
	public String postMain() {
		
		return "main";
	}

}
