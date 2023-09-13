package com.walk.aroundyou.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.walk.aroundyou.dto.UserPasswordSendDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
// 메일 전송 처리 담당 서비스
public class MailService{

	private JavaMailSender mailSender;
	
	private static final String title = "Walk Around You 임시비밀번호 안내 이메일입니다.";
	private static final String message = "안녕하세요. Walk Around You 임시비밀번호 안내 관련 이메일 입니다." + "\n" + " 회원님의 임시 비밀번호는 아래와 같습니다. 로그인 후 반드시 비밀번호를 변경해주세요"  + "\n";
	private static final String fromAddress = "WalkAroundYou601.gmail.com";
			
	// 이메일 생성		
	public UserPasswordSendDTO createMail(String tmpPwd, String userEmail) {
		
		// JavaMailSender 구현체 생성
		mailSender = new JavaMailSenderImpl();
		
		
		UserPasswordSendDTO dto = UserPasswordSendDTO.builder()
				.toaddress(userEmail)
				.title(title)
				.message(message + tmpPwd)
				.fromAddress(fromAddress)
				.build();
		
		log.info("메일 생성 완료");
		return dto;
		
	}

	
	// 이메일 전송
	public void sendMail(UserPasswordSendDTO dto) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(dto.getToaddress());
		message.setSubject(dto.getTitle());
		message.setText(dto.getTitle());
		message.setFrom(dto.getFromAddress());
		message.setReplyTo(dto.getFromAddress());
		
		mailSender.send(message);
		
		log.info("메일 전송 완료");
		
		
	}
	
}