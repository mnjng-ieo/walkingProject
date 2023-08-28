package com.walk.aroundyou.domainenum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
   USER("사용자"),
   ADMIN("관리자");
   
   private final String kor;
   
}

