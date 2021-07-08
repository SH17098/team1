package com.example.demo;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BookController {
	@Autowired
	HttpSession session;

	@Autowired
	BookRepository bookRepository;

	//参考書一覧の表示
	@RequestMapping("/book")
	public ModelAndView book(ModelAndView mv) {
		List<Book> books = bookRepository.findAll();
		mv.setViewName("book");
		return mv;
	}


}
