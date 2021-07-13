package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="test")
public class Test {
	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="code")
	private Integer code;

	@Column(name="question")
	private String question;

	private String option_a;
	private String option_b;
	private String option_c;
	private String option_d;

	private String check_answer;

	@Column(name="answer")
	private String answer;

	@Column(name="explanation")
	private String explanation;

	//コンストラクタ
	public Test(Integer code, String question,String option_a, String option_b, String option_c,String option_d, String check_answer,String answer, String explanation) {
		this(question, option_a, option_b,option_c,option_d,check_answer,answer, explanation);
		this.code = code;
	}

	public Test(String question,String option_a, String option_b, String option_c,String option_d,String check_answer, String answer, String explanation) {
		super();
		this.question = question;
		this.option_a = option_a;
		this.option_a = option_b;
		this.option_a = option_c;
		this.option_a = option_d;
		this.check_answer = check_answer;
		this.answer = answer;
		this.explanation = explanation;
	}

	public Test() {
		super();
	}


	//ゲッターセッター
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getOption_a() {
		return option_a;
	}

	public void setOption_a(String option_a) {
		this.option_a = option_a;
	}
	public String getOption_b() {
		return option_b;
	}

	public void setOption_b(String option_b) {
		this.option_b = option_b;
	}
	public String getOption_c() {
		return option_c;
	}

	public void setOption_c(String option_c) {
		this.option_c = option_c;
	}
	public String getOption_d() {
		return option_d;
	}

	public void setOption_d(String option_d) {
		this.option_d = option_d;
	}
	public String getCheck_answer() {
		return check_answer;
	}

	public void setCheck_answer(String check_answer) {
		this.check_answer = check_answer;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}


}
