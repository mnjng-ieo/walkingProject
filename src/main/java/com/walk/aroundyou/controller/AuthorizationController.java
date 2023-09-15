//package com.walk.aroundyou.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.walk.aroundyou.dto.UserSignupDTO;
//import com.walk.aroundyou.service.UserService;
//
//// 회원가입 api
//@RestController
//@RequestMapping("/auth")
//public class AuthorizationController {
//
//    private final UserService registerMemberService;
//
//    public AuthorizationController(UserService registerMemberService) {
//        this.registerMemberService = registerMemberService;
//    }
//
//
//    @PostMapping("/join")
//    public ResponseEntity<String> join(@RequestBody UserSignupDTO dto) {
//        try {
//            registerMemberService.join(dto.getUserId(), dto.getUserPwd());
//            return ResponseEntity.ok("join success");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//}
