package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="code")
	private Integer userCode;
	@Column(name="name")
	private String name;
	@Column(name="userid")
	private String userId;
	@Column(name="email")
	private String email;
	@Column(name="password")
	private String password;
	@Column(name="q_number")
	private String q_number;
	@Column(name="answer")
	private String answer;

	//コンストラクタ
	public User() {

	}



	public User(Integer userCode, String name, String userId,String email,String password, String q_number, String answer) {
		this.userCode = userCode;
		this.name = name;
		this.userId = userId;
		this.email=email;
		this.password = password;
		this.q_number = q_number;
		this.answer = answer;
	}
    //新規登録
	//パスワード忘れ
	public User(String name, String userId, String email,String password, String q_number, String answer) {
		this.name = name;
		this.userId = userId;
		this.email=email;
		this.password = password;
		this.q_number = q_number;
		this.answer = answer;
	}

	//ログイン画面
	public User(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	//ゲッタ・セッタ
	public Integer getUserCode() {
		return userCode;
	}

	public void setUserCode(Integer userCode) {
		this.userCode = userCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getQ_number() {
		return q_number;
	}

	public void setQ_number(String q_number) {
		this.q_number = q_number;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
