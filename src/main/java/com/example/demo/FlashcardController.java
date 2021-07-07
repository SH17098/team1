package com.example.demo;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FlashcardController {
	@Autowired
	HttpSession session;

	@Autowired
	FlashcardRepository flashcardRepository;

//フラッシュカードのホーム画面の表示
	@RequestMapping("/flashcard")
	public ModelAndView flashcard(ModelAndView mv) {
		mv.setViewName("flashcard");
		return mv;
	}


//新規カード作成画面の表示
	@RequestMapping("/flashcard/add")
	public ModelAndView addFlashcard(ModelAndView mv) {
		mv.setViewName("addFlashcard");
		return mv;
	}

//新規カードを登録
	@PostMapping("/flashcard/add")
	public ModelAndView addFlashcard2(
			@RequestParam("question") String question,
			@RequestParam("answer") String answer,
			@RequestParam("explanation") String explanation,
			ModelAndView mv) {
		//新規カードをflashcardfeテーブルに保存
		Flashcard newflashcard = new Flashcard(question, answer, explanation);
		flashcardRepository.saveAndFlush(newflashcard);

        mv.addObject("question", question);
        mv.addObject("answer", answer);
        mv.addObject("explanation", explanation);

		mv.setViewName("addFlashcard");
		return mv;
	}

//学習画面を表示


}
