package com.walk.aroundyou.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 권한에 따른 제어 (admin 어노테이션으로 만들어주기 -> @AdminAuthorize)
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ADMIN')")
public @interface AdminAuthorize {
}