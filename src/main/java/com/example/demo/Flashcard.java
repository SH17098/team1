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

	@Column(name="question")
	private String question;

	@Column(name="answer")
	private String answer;

	@Column(name="explanation")
	private String explanation;

	//コンストラクタ
	public Flashcard(Integer code, String question,String answer, String explanation) {
		this(question, answer, explanation);
		this.code = code;
	}

	public Flashcard(String question,String answer, String explanation) {
		super();
		this.question = question;
		this.answer = answer;
		this.explanation = explanation;
	}

	public Flashcard() {
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
