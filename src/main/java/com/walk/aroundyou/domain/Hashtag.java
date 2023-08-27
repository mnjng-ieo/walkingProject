package com.walk.aroundyou.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "hashtag")
public class Hashtag {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name="tag_id", nullable=false, columnDefinition = "bigint")
   @OneToMany(mappedBy = "tagId", cascade=CascadeType.REMOVE)
   private long tagId;
   
   @Column(name="hashtag", nullable=false, columnDefinition = "varchar(50)")
   private String hashTag;
   
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "state_id", nullable = false)
   private int stateId;

}