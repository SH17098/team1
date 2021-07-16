package com.example.demo;

import java.util.Optional;

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
	@RequestMapping("/flashcard/new/add")
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

		//サニタイジング
		question = Sanitizing.convert(question);
		answer = Sanitizing.convert(answer);
		explanation = Sanitizing.convert(explanation);


		//未入力の項目
		if (question.equals("") || answer.equals("") || explanation.equals("")) {
			mv.addObject("message", "未入力の項目があります。");
		mv.addObject("question", question);
		mv.addObject("answer", answer);
		mv.addObject("explanation", explanation);
			mv.setViewName("addFlashcard");
			return mv;

		}

		//新規カードをflashcardfeテーブルに保存
		Flashcard newflashcard = new Flashcard(question, answer, explanation);
		flashcardRepository.saveAndFlush(newflashcard);

		mv.setViewName("addFlashcard");
		return mv;
	}

	//学習画面と問題を表示
	@RequestMapping("/flashcard/use")
	public ModelAndView useFlashcard(ModelAndView mv) {
		session.removeAttribute("flashcard");
		//順番に取得していくver.
		int card_code = 1; //初期化


		if (session.getAttribute("card_code") != null) {//セッションがあれば
			card_code = (int) session.getAttribute("card_code"); //コードを取得して１増やす
			card_code++;
		}

		//１増やしたコードで検索
		Optional<Flashcard> record = flashcardRepository.findById(card_code);
		    session.setAttribute("card_code", card_code);

		if (record.isEmpty() == false) {
			Flashcard flashcard = record.get();
			session.setAttribute("flashcard", flashcard);//セッションに追加
			mv.addObject("question", flashcard.getQuestion());
		}else {
			if(card_code < 100) {//recordがなければ、もう一度一から実行する
			    useFlashcard(mv);

			}else {//100以上になったら、card_codeを再度一に初期化させる
				session.removeAttribute("card_code"); //card_codeセッションは破棄
				useFlashcard(mv);
			}
		}

		mv.setViewName("useFlashcard");
		return mv;
	}

	//解答を表示
	@RequestMapping("/flashcard/answer")
	public ModelAndView showAnswer(ModelAndView mv) {
		Flashcard current_flashcard = (Flashcard) session.getAttribute("flashcard");//現在のフラッシュカード情報をセッションから取得
		String answer = current_flashcard.getAnswer(); //answerのみ取得

		mv.addObject("answer", answer);
        mv.addObject("question", current_flashcard.getQuestion());

		mv.setViewName("useFlashcard");
		return mv;
	}

	//解説を表示
	@RequestMapping("/flashcard/explanation")
	public ModelAndView showexplanation(ModelAndView mv) {
		Flashcard current_flashcard = (Flashcard) session.getAttribute("flashcard");//現在のフラッシュカード情報をセッションから取得
		String explanation = current_flashcard.getExplanation(); //answerのみ取得

		mv.addObject("explanation", explanation);
		mv.addObject("answer", current_flashcard.getAnswer());
        mv.addObject("question", current_flashcard.getQuestion());

		mv.setViewName("useFlashcard");
		return mv;
	}

}
