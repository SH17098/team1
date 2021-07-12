package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="likes")
public class Like {
	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="code")
	private Integer code;

	@Column(name="user_code")
	private Integer userCode;

	@Column(name="tweet_code")
	private Integer tweetCode;

	//コンストラクタ
	public Like(Integer code, Integer userCode, Integer tweetCode) {
		this(userCode,tweetCode);
		this.code = code;
	}
	public Like(Integer userCode, Integer tweetCode) {
		super();
		this.userCode = userCode;
		this.tweetCode = tweetCode;
	}
	public Like() {
		super();
	}

	//セッターゲッター
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Integer getUserCode() {
		return userCode;
	}
	public void setUserCode(Integer userCode) {
		this.userCode = userCode;
	}
	public Integer getTweetCode() {
		return tweetCode;
	}
	public void setTweetCode(Integer tweetCode) {
		this.tweetCode = tweetCode;
	}


}
