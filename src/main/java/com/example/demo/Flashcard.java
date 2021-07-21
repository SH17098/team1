package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="flashcardsfe")
public class Flashcard {
	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="code")
	private Integer code;

	@Column(name="user_code")
	private Integer userCode;

	@Column(name="share")
	private Integer share;

	@Column(name="question")
	private String question;

	@Column(name="answer")
	private String answer;

	@Column(name="explanation")
	private String explanation;

	//コンストラクタ
	public Flashcard(Integer code, Integer userCode, Integer share, String question,String answer, String explanation) {
		this(userCode, share, question, answer, explanation);
		this.code = code;
	}

	public Flashcard(Integer userCode, Integer share, String question,String answer, String explanation) {
		super();
		this.userCode = userCode;
		this.share = share;
		this.question = question;
		this.answer = answer;
		this.explanation = explanation;
	}

	public Flashcard() {
		super();
	}

	//gettersetter

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

	public Integer getShare() {
		return share;
	}

	public void setShare(Integer share) {
		this.share = share;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
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
