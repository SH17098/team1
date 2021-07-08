package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Comment {
	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;

	private Integer book_code;

	private String comment;

	//コンストラクタ
	public Comment(Integer code, Integer book_code, String comment) {
		this(book_code, comment);
		this.code = code;
	}

	public Comment(Integer book_code, String comment) {
		super();
		this.book_code = book_code;
		this.comment = comment;
	}

	public Comment() {
		super();
	}

	//セッターゲッター
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getBook_code() {
		return book_code;
	}

	public void setBook_code(Integer book_code) {
		this.book_code = book_code;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}




}
