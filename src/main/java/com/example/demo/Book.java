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

	//コンストラクタ
	public Book(Integer code, String name,Integer price) {
		this(name, price);
		this.code = code;
	}

	public Book(String name,Integer price) {
		super();
		this.name = name;
		this.price = price;
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




}
