package com.walk.aroundyou.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

// 권한에 따른 제어 (user 어노테이션으로 만들어주기 -> @UserAuthorize)
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('USER')")
// @interface : 사용자 정의 어노테이션 만들기
public @interface UserAuthorize {
}
