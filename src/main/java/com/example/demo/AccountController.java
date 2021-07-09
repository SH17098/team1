package com.example.demo;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

	@RequestMapping(value = "/login", method = RequestMethod.POST)
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
			mv.addObject("message", "パスワードを入力してください");
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
			if (password .equals(user_password)) {
				// セッションスコープにユーザー情報を格納する
				session.setAttribute("userCode", user.getUserCode());
				session.setAttribute("userId", user.getUserId());
				session.setAttribute("userInfo", user);

				mv.setViewName("top");
				return mv;

			} else {//パスワードが一致しなかった場合、パスワードが間違ってる
				mv.addObject("message", "ユーザーIDとパスワードが一致しません");
				mv.setViewName("login");
				return mv;

			}

		}

		return mv;
	}

	//新規登録

		/**
		 * 新規登録画面表示
		 */
		@GetMapping("/user/add")
		public String userAdd() {
			return "addUser";
		}

		/**
		 * 新規登録処理
		 */
		@RequestMapping(value = "/user/add", method = RequestMethod.POST)
		public ModelAndView doUserAdd(
				@RequestParam("name") String name,
				@RequestParam("userId") String userId,
				@RequestParam("mail") String mail,
				@RequestParam("password") String password,
				@RequestParam("passwordCheck") String passwordCheck,
				@RequestParam("q_number") String q_number,
				@RequestParam("answer") String answer,
				ModelAndView mv) {

			//空白があったときの処理
			if (name.equals("") || userId.equals("") || mail.equals("") || password.equals("") || q_number.equals("")
					|| answer.equals("")) {

				mv.addObject("message", "未入力の箇所があります。");
				mv.setViewName("addUser");

				return mv;
			}


            //２つのパスワードが一致していない
			if(!password.equals(passwordCheck)) {
				mv.addObject("message","パスワードが一致していません");
				mv.setViewName("addUser");

				return mv;
			}

			//ユーザーIDがすでに登録済みの場合
			Optional<User> record = userRepository.findByUserId(userId);

			if (record.isEmpty() == false) {
				mv.addObject("message", "このIDは既に存在しています");
				mv.setViewName("addUser");

				return mv;
			}

			// パラメータからオブジェクトを生成
			User user = new User(name, userId, mail,password, q_number, answer);
			// userテーブルへの登録
			userRepository.saveAndFlush(user);

			mv.addObject("name", name);
			mv.addObject("userId", userId);
			mv.addObject("mail", mail);
			mv.addObject("secret", "秘密");
			//登録完了メッセージを表示
			mv.addObject("complete", "登録が完了しました。再度ログインを行ってください。");
			mv.setViewName("addUser");
			return mv;

		}

		/**
		 * パスワード忘れの処理
		 */

		@GetMapping("/help")
		public String userHelp() {
			return "help";
		}

		@RequestMapping(value = "/help", method = RequestMethod.POST)
		public ModelAndView help(
				@RequestParam("name") String name,
				@RequestParam("mail") String mail,
				@RequestParam("q_number") String q_number,
				@RequestParam("answer") String answer,
				ModelAndView mv) {

			//空白箇所の処理
			if (name.equals("") || mail.equals("") || q_number.equals("")|| answer.equals("")) {

				mv.addObject("message", "未入力の箇所があります。");
				mv.setViewName("help");

				return mv;

			}


			//emailから情報を取得
			Optional<User> record = userRepository.findByEmail(mail);

			//DBでの検証
			if (record.isEmpty() == false) { //データが見つかれば
				User user = record.get();
				String user_name = user.getName();
				String user_Qnumber = user.getQ_number();
				String user_answer = user.getAnswer();


				//4つの情報を検証

				if (name.equals(user_name) && q_number.equals(user_Qnumber) && answer.equals(user_answer)) {


					//DBと情報が一致したらuserIDとパスワードを表示
					String password = user.getPassword();
					String userId = user.getUserId();
					mv.addObject("password","パスワードは" + password + "です。");
					mv.addObject("userId","あなたのユーザーネームは" + userId + "です。");
					mv.addObject("name", name);
					mv.addObject("mail", mail);
					mv.addObject("q_number", q_number);
					mv.addObject("answer", answer);

					mv.setViewName("help");
					return mv;

				} else {//DBと情報が一致しなかったら

					mv.addObject("message", "登録内容と情報が一致しません");
					mv.setViewName("help");
					return mv;

				}

			}else {
					mv.addObject("message","データが見つかりません");
					mv.setViewName("help");

			}
			return mv;


		}

		//TOP画面
		@GetMapping("/top")
		public String top() {
			return "top";
		}




		//ホーム画面
		@GetMapping("/home")
		public String home() {
			return "home";
		}



		/**
		 * ログアウトを実行
		 */
		@RequestMapping("/logout")
		public String logout() {
			// ログイン画面表示処理を実行するだけ
			return login();
		}

	}



