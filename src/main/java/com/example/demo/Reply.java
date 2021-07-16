package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Reply {
	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;

	@Column(name="user_code")
	private Integer userCode;

	@Column(name="tweet_code")
	private Integer tweetCode;

	private String reply;

	//コンストラクタ
	public Reply(Integer code, Integer userCode, Integer tweetCode,String reply) {
		this(userCode, tweetCode, reply);
		this.code = code;
	}

	public Reply(Integer userCode, Integer tweetCode,String reply) {
		super();
		this.userCode = userCode;
		this.tweetCode = tweetCode;
		this.reply = reply;
	}

	public Reply() {
		super();
	}

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

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

}
