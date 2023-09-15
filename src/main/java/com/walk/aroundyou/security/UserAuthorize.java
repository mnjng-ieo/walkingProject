package com.walk.aroundyou.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

// 권한에 따른 제어 (user 어노테이션으로 만들어주기 -> @UserAuthorize)
// 인터페이스의 범위를 나타냄(대상) : 메소드에 적용됨, 클래스 등 열거형에 적용
@Target({ ElementType.METHOD, ElementType.TYPE })
// 정보가 언제 유지되는지를 지정할 때 사용(정보를 언제까지 유지할 것인지)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('USER')")
// @interface : 사용자 정의 어노테이션 만들기
public @interface UserAuthorize {
}
