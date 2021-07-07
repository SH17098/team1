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
	private String name;
	@Column(name="userid")
	private String userId;
	private String password;
	private String q_number;
	private String answer;

	//コンストラクタ
	public User() {

	}

	//新規会員登録
	public User(Integer userCode, String name, String userId, String password, String q_number, String answer) {
		this.userCode = userCode;
		this.name = name;
		this.userId = userId;
		this.password = password;
		this.q_number = q_number;
		this.answer = answer;
	}

	//パスワード忘れ
	public User(String name, String userId, String password, String q_number, String answer) {
		this.name = name;
		this.userId = userId;
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
