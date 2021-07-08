package com.example.demo;

import java.util.Optional;

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

		//userIdで検索
//		List<User> users = userRepository.findByUserIdAndPassword(userId, password);
        Optional<User> record = userRepository.findByUserId(userId);
		//入力されたIDとパスワードが合う場合

		//DBと照合
		if (record.isEmpty() == false) { //データが見つかれば
			User user = record.get();
			String user_password = user.getPassword();//そのパスワードを取得

			//パスワードの照合
			if(password == user_password) {
			// セッションスコープにユーザー情報を格納する
				session.setAttribute("userCode", user.getUserCode());
				session.setAttribute("userId", user.getUserId());
			    session.setAttribute("userInfo", user);

				mv.setViewName("top");
				return mv;
			}else {//パスワードが一致しなかった場合、パスワードが間違ってる

			}


		} else {

			// エラーメッセージをセット
			mv.addObject("message", "ユーザーIDとパスワードが一致しません");
			mv.setViewName("login");
			return mv;
		}
     return mv;
	}

}
