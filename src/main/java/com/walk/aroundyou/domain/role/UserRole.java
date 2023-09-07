package com.walk.aroundyou.domain.role;

public enum UserRole {
	USER("USER"), ADMIN("ADMIN"), GUEST("GUEST");
	
	private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
