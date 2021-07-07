package com.example.demo;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccountController {

	@Autowired
	HttpSession session;

	@Autowired
	public UserRepository userRepository;

	/**
	 * ログイン画面を表示
	 */

	//http://localhost:8080/
	@RequestMapping("/")
	public String login() {
		session.invalidate();
		return "login";
	}

	/**
	 * ログイン処理
	 */

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView doLogin(
			@RequestParam("userId") String userId,
			@RequestParam("password") String password,
			ModelAndView mv) {
		//両方が空だった場合
		if (userId.equals("") && password.equals("")) {
			mv.addObject("message", "ユーザーIDとパスワードを入力してください");
			mv.setViewName("login");
			return mv;

		} else if (userId.equals("")) {
			//ユーザーIDが空だった場合

			mv.addObject("message", "ユーザーIDを入力してください");
			mv.setViewName("login");
			return mv;

		} else if (password.equals("")) {
			//パスワードが空だった場合
			mv.addObject("message", "パスポートを入力してください");
			mv.setViewName("login");

			return mv;

		}

		//userIdとパスワードをDBに入れる
		List<User> users = userRepository.findByUserIdAndPassword(userId, password);

		//入力されたIDとパスワードが合う場合

		//DBと照合
		if () {
			User user = users.get(0);
			session.setAttribute("userInfo", user);
			// セッションスコープにユーザー情報を格納する
			session.setAttribute("user", userRepository.findAll());
			// top.htmlを表示する
			mv.setViewName("top");
			return mv;

		} else {

			// エラーメッセージをセット
			mv.addObject("message", "ユーザーIDとパスワードが一致しません");
			mv.setViewName("login");
			return mv;
		}

	}

}
