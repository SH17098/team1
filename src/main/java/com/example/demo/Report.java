package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Report {
	//field
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;

	@Column(name="user_code")
	private Integer userCode;

	private String type;

	private String report;

	private String answer;

	//contructor
	public Report(Integer code, Integer userCode,String type, String report, String answer) {
		this(userCode, type, report, answer);
		this.code = code;
	}

	public Report(Integer userCode,String type, String report, String answer) {
		this.userCode = userCode;
		this.type = type;
		this.report = report;
		this.answer = answer;
	}

	public Report() {
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
