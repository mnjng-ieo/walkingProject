package com.walk.aroundyou.domain;

import java.sql.Timestamp;

import org.hibernate.annotations.ColumnDefault;

import com.walk.aroundyou.domainenum.StateId;
import com.walk.aroundyou.domainenum.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Entity
public class User {

   // 멤버변수
   @Id
   @Column(name="user_id", nullable=false, columnDefinition= "varchar(100)")
   private String userId;
   
   @Column(name="user_pwd", nullable=false, columnDefinition= "varchar(100)")
   private String userPwd;
   
   @Column(name="user_name", nullable=false, columnDefinition= "varchar(100)")
   private String userName;
   
   @Column(name="user_nickname", nullable=false, columnDefinition= "varchar(50)")
   private String userNickname;
   
   @Column(name="user_tel_number", nullable=false, columnDefinition= "varchar(50)")
   private String userTelNumber;
   
   @Column(name="user_email", nullable=false, unique = true, columnDefinition= "varchar(50)")
   private String userEmail;
   
   
   // 	버전2를 위한 컬럼
//   @Column(name="user_img", columnDefinition= "varchar(255)")
//   private String userImg;
   
   @Column(name="user_join_date", nullable=false, columnDefinition= "datetime")
   private Timestamp userJoinDate;
   
   @Column(name="user_update_date", nullable=false, columnDefinition= "datetime")
   private Timestamp userUpdateDate;
   
   @Column(name="state_id", nullable=false)
   @Enumerated(EnumType.STRING)
   @ColumnDefault("'NORMAL'")
   private StateId stateId;
   
   @Column(name="user_role", nullable=false)
   @Enumerated(EnumType.STRING)
   @ColumnDefault("'USER'")
   private UserRole role;
   
   @Column(name="social", nullable=false, columnDefinition= "tinyint")
   private int social;
      
}
