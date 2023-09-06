package com.walk.aroundyou.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.service.UserService;

import java.util.Optional;

@Component
public class MyUserDetailService implements UserDetailsService {
	
    private final UserService memberService;

    @Autowired
    public MyUserDetailService(UserService memberService) {
        this.memberService = memberService;
    }

    // securityconfig에서 사용할 사용자 정보 가져오기
    // 회원가입 기능
    @Override
    public UserDetails loadUserByUsername(String insertedId) throws UsernameNotFoundException {
        Optional<User> findOne = memberService.findByUserId(insertedId);
        User member = findOne.orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다 ㅠ"));

        return User.builder()
                .userId(member.getUserId())
                .userPwd(member.getUserPwd())
                .role(member.getRole())
                .build();
    }
}
