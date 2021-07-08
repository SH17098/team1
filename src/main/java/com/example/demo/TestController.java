package com.example.demo;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {
	@Autowired
	HttpSession session;

	@Autowired
	TestRepository testRepository;

	//過去問のホーム画面の表示
	@RequestMapping("/test")
	public ModelAndView test(ModelAndView mv) {
		mv.setViewName("test");
		return mv;
	}

	//学習画面と問題を表示
	@RequestMapping("/test/use")
	public ModelAndView useTest(ModelAndView mv) {
		session.removeAttribute("test");
		//順番に取得していくver.
		int card_code = 1; //初期化

		if (session.getAttribute("card_code") != null) {//セッションがあれば
			card_code = (int) session.getAttribute("card_code"); //コードを取得して１増やす
			card_code++;
		}

		//１増やしたコードで検索
		Optional<Test> record = testRepository.findById(card_code);
		    session.setAttribute("card_code", card_code);

		if (record.isEmpty() == false) {
			Test test = record.get();
			session.setAttribute("test", test);//セッションに追加
			mv.addObject("question", test.getQuestion());
		}else {
				session.removeAttribute("card_code"); //card_codeセッションは破棄
				useTest(mv);
		}
        //test
//		System.out.println(session.getAttribute("card_code"));

		mv.setViewName("useTest");
		return mv;
	}

	//解答を表示
	@RequestMapping("/test/answer")
	public ModelAndView showAnswer(ModelAndView mv) {
		Test current_test = (Test) session.getAttribute("test");//現在のフラッシュカード情報をセッションから取得
		String answer = current_test.getAnswer(); //answerのみ取得

		mv.addObject("answer", answer);
        mv.addObject("question", current_test.getQuestion());

		mv.setViewName("useTest");
		return mv;
	}

	//解説を表示
	@RequestMapping("/test/explanation")
	public ModelAndView showexplanation(ModelAndView mv) {
		Test current_test = (Test) session.getAttribute("test");//現在のフラッシュカード情報をセッションから取得
		String explanation = current_test.getExplanation(); //answerのみ取得

		mv.addObject("explanation", explanation);
		mv.addObject("answer", current_test.getAnswer());
        mv.addObject("question", current_test.getQuestion());

		mv.setViewName("useTest");
		return mv;
	}

}
