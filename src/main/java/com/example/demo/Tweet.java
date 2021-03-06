package com.example.demo;



import java.sql.Date;

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

	private Integer likes;

	@Column(name="user_code")
	private Integer user_code;

	private Date date;

	@Column(name="tweet")
	private String tweet;

	//コンストラクタ
	public Tweet(Integer code, Integer likes, Integer user_code,String tweet, Date date) {
		this(likes, user_code,tweet, date);
		this.code = code;
	}
	public Tweet(Integer likes, Integer user_code,String tweet, Date date) {
		super();
		this.likes = likes;
		this.user_code = user_code;
		this.date= date;
		this.tweet = tweet;
	}



	public Tweet(Integer user_code, String tweet) {
		super();
		this.user_code = user_code;
		this.tweet = tweet;
	}

	public Tweet(Integer code, Integer likes) {
		super();
		this.likes = likes;
		this.code = code;

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

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getUser_code() {
		return user_code;
	}

	public void setUser_code(Integer user_code) {
		this.user_code = user_code;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {
		this.tweet = tweet;
	}



}
