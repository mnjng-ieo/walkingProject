package com.walk.aroundyou;

import java.sql.Timestamp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.walk.aroundyou.domain.User;
import com.walk.aroundyou.domain.role.StateId;
import com.walk.aroundyou.domain.role.UserRole;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class WalkAroundYouApplication implements CommandLineRunner {

	private String userId;
	private String userPwd;
	private String userName;
	private String userNickname;
	private String userTelNumber;
	private String userEmail;
	private String userImg;
	private String userDescription;
	private Timestamp userJoinDate;
	private Timestamp userUpdateDate;
	private StateId stateId;
	private UserRole role;
	private boolean socialYn;

	public static void main(String[] args) {
		SpringApplication.run(WalkAroundYouApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		// TODO Auto-generated method stub
		User user = User.builder()
				.userId(userId)
				.userPwd(userPwd)
				.userName(userName)
				.userNickname(userNickname)
				.userTelNumber(userTelNumber)
				.userEmail(userEmail)
				.userImg(userImg)
				.userDescription(userDescription)
				.stateId(stateId)
				.role(role)
				.socialYn(socialYn)
				.build();
		
		log.info("");
		log.info("");
		log.info("");
		log.info("");
		log.info("");
		
	}

}
