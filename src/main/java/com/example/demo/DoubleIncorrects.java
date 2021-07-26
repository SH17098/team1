package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="double_incorrects")
public class DoubleIncorrects {
	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="code")
	private Integer code;

	@Column(name="user_code")
	private Integer userCode;

	@Column(name="test_code")
	private Integer testCode;

	//コンストラクタ
	public DoubleIncorrects(Integer code, Integer userCode, Integer testCode) {
		this(userCode, testCode);
		this.code = code;

	}

	public DoubleIncorrects(Integer userCode, Integer testCode) {
		super();
		this.userCode = userCode;
		this.testCode = testCode;
	}

	public DoubleIncorrects() {
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

	public Integer getTestCode() {
		return testCode;
	}

	public void setTestCode(Integer testCode) {
		this.testCode = testCode;
	}


}
