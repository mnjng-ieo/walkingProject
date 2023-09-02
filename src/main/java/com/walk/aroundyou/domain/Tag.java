package com.walk.aroundyou.domain;

import org.hibernate.annotations.ColumnDefault;

import com.walk.aroundyou.domainenum.StateId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "tag")
public class Tag {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name="tag_id", nullable=false, columnDefinition = "bigint")
   private Long tagId;
   
   @Column(name="tag_content", nullable=false, columnDefinition = "varchar(50)")
   private String tagContent;
   
   @Column(name = "state_id", nullable = false)
   @Enumerated(EnumType.STRING)
   @ColumnDefault("'NORMAL'")
   private StateId stateId;

}