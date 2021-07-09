package com.example.demo;

import javax.persistence.Column;
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

	@Column(name="book_code")
	private Integer bookCode;

	private String comment;

	//コンストラクタ
	public Comment(Integer code, Integer bookCode, String comment) {
		this(bookCode, comment);
		this.code = code;
	}

	public Comment(Integer bookCode, String comment) {
		super();
		this.bookCode = bookCode;
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

	public Integer getBookCode() {
		return bookCode;
	}

	public void setBookCode(Integer bookCode) {
		this.bookCode = bookCode;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}




}
