package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {
	@Autowired
	HttpSession session;

	@Autowired
	TestRepository testRepository;

	@Autowired
	IncorrectRepository incorrectRepository;

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
			mv.addObject("option_a", test.getOption_a());
			mv.addObject("option_b", test.getOption_b());
			mv.addObject("option_c", test.getOption_c());
			mv.addObject("option_d", test.getOption_d());

		} else {
			session.removeAttribute("card_code"); //card_codeセッションは破棄
			useTest(mv);
		}
		//test
		//		System.out.println(session.getAttribute("card_code"));

		mv.setViewName("useTest");
		return mv;
	}

	//解答を表示
	@PostMapping("/test/answer")
	public ModelAndView showAnswer(
			@RequestParam("option") String option,
			ModelAndView mv) {
		Test current_test = (Test) session.getAttribute("test");//現在のフラッシュカード情報をセッションから取得
		String check_answer = current_test.getCheck_answer(); //採点用に取得
		String answer = current_test.getAnswer(); //answerのみ取得
		String explanation = current_test.getExplanation(); //解説のみ取得

		//test
		System.out.println(option);
		System.out.println(check_answer);

		//採点
		if (option.equals(check_answer)) {//正解だったら
			//正解と表示
			mv.addObject("correct", "正解");

		} else {//不正解なら
				//データベースにuserCodeと問題番号を保存
				//セッションからuserCodeを取得
			int userCode = (int) session.getAttribute("userCode");

			//セッションからtestCodeを取得
			int testCode = current_test.getCode();

			//incorrectテーブルに保存
			Incorrect newIncorrect = new Incorrect(userCode, testCode);
			incorrectRepository.saveAndFlush(newIncorrect);

			//不正解と表示
			mv.addObject("correct", "不正解");
		}

		mv.addObject("explanation", explanation);
		mv.addObject("answer", answer);
		mv.addObject("question", current_test.getQuestion());
		mv.addObject("option_a", current_test.getOption_a());
		mv.addObject("option_b", current_test.getOption_b());
		mv.addObject("option_c", current_test.getOption_c());
		mv.addObject("option_d", current_test.getOption_d());

		mv.setViewName("useTest");
		return mv;
	}

	//過去に間違えた問題を表示
	@RequestMapping("/test/incorrect")
	public ModelAndView incorrect(ModelAndView mv) {
		//userCodeを取得して、test_codeを取得
		int userCode = (int) session.getAttribute("userCode");
		List<Incorrect> lists = incorrectRepository.findByUserCode(userCode);
		if(lists.size() == 0) {
			mv.addObject("none", "まだ間違えた問題はありません。");
		}

		List<Integer> testCode = new ArrayList<Integer>();
		for (int i = 0; i < lists.size(); i++) {
			Incorrect incorrect = lists.get(i);
			testCode.add(incorrect.getTestCode());
		}

		List<Test> tests = new ArrayList<Test>();
		//testCodeを使って、テスト一覧を表示
		for (int k = 0; k < testCode.size(); k++) {
			Optional<Test> record = testRepository.findById(testCode.get(k));
			if (record.isEmpty() == false) {
				Test nextTest = record.get();
				tests.add(nextTest);
			}
		}

		mv.addObject("tests", tests);
		mv.setViewName("incorrectTest");
		return mv;
	}

	//過去に間違えた問題の詳細を表示
	@PostMapping("/incorrect/detail")
	public ModelAndView incorrect2(
			@RequestParam("test_code") String test_code,
			ModelAndView mv) {
		int testCode = Integer.parseInt(test_code);
		//testCodeから全情報をデータベースより取得
		Test test = null;
		Optional<Test> record = testRepository.findById(testCode);
        if(record.isEmpty() == false) {
        	test = record.get();
        }

 		mv.addObject("explanation", test.getExplanation());
		mv.addObject("answer", test.getAnswer());
		mv.addObject("question", test.getQuestion());
		mv.addObject("option_a", test.getOption_a());
		mv.addObject("option_b", test.getOption_b());
		mv.addObject("option_c", test.getOption_c());
		mv.addObject("option_d", test.getOption_d());

        mv.setViewName("incorrectDetail");
		return mv;
	}

	//	//解説を表示
	//	@RequestMapping("/test/explanation")
	//	public ModelAndView showexplanation(ModelAndView mv) {
	//		Test current_test = (Test) session.getAttribute("test");//現在のフラッシュカード情報をセッションから取得
	//		String explanation = current_test.getExplanation(); //answerのみ取得
	//
	//		mv.addObject("explanation", explanation);
	//		mv.addObject("answer", current_test.getAnswer());
	//        mv.addObject("question", current_test.getQuestion());
	//		mv.addObject("option", current_test.getOption());
	//
	//		mv.setViewName("useTest");
	//		return mv;
	//	}

}
