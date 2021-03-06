package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book {

	//フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;

	private String name;

	private Integer price;

	private Integer rate;

	private Integer comment;

	private String amazon;

	//コンストラクタ
	public Book(Integer code, String name,Integer price, Integer rate, Integer comment, String amazon) {
		this(name, price, rate, comment, amazon);
		this.code = code;
	}

	public Book(String name,Integer price, Integer rate, Integer comment, String amazon) {
		super();
		this.name = name;
		this.price = price;
		this.rate = rate;
		this.comment = comment;
		this.amazon = amazon;
	}

	public Book() {
		super();
	}

	//セッターゲッター
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	public Integer getComment() {
		return comment;
	}

	public void setComment(Integer comment) {
		this.comment = comment;
	}

	public String getAmazon() {
		return amazon;
	}

	public void setAmazon(String amazon) {
		this.amazon = amazon;
	}





}
