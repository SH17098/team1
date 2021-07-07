package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tweetfe")
public class Tweet {
	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="code")
	private Integer code;

	@Column(name="user_code")
	private Integer user_code;

	@Column(name="tweet")
	private String tweet;

	//コンストラクタ
	public Tweet(Integer code, Integer user_code, String tweet) {
		this(user_code, tweet);
		this.code = code;
	}

	public Tweet(Integer user_code, String tweet) {
		super();
		this.user_code = user_code;
		this.tweet = tweet;
	}
	public Tweet() {
		super();
	}



	//セッターゲッター
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getUser_code() {
		return user_code;
	}

	public void setUser_code(Integer user_code) {
		this.user_code = user_code;
	}

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {
		this.tweet = tweet;
	}



}
