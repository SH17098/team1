package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="favorites")
public class Favorite {
	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="code")
	private Integer code;

	@Column(name="user_code")
	private Integer userCode;

	@Column(name="flashcard_code")
	private Integer flashcardCode;

	//コンストラクタ
	public Favorite(Integer code, Integer userCode, Integer flashcardCode) {
		this(userCode, flashcardCode);
		this.code = code;

	}

	public Favorite(Integer userCode, Integer flashcardCode) {
		super();
		this.userCode = userCode;
		this.flashcardCode = flashcardCode;
	}

	public Favorite() {
		super();
	}

	//ゲッターセッター
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

	public Integer getFlashcardCode() {
		return flashcardCode;
	}

	public void setFlashcardCode(Integer flashcardCode) {
		this.flashcardCode = flashcardCode;
	}



}
