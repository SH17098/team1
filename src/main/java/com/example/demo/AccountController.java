package com.example.demo;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccountController extends SecurityController{

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

		//サニタイジング
		userId = Sanitizing.convert(userId);
		password = Sanitizing.convert(password);

		//test
		//		System.out.println(userId);

		//両方が空だった場合
		if (userId.equals("") && password.equals("")) {
			mv.addObject("message", "ユーザーIDとパスワードを入力してください");
			mv.setViewName("login");
			return mv;

		//片方が空欄だった場合
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

		//すべてが入力された場合照合を行う

		//userIdで検索
		Optional<User> record = userRepository.findByUserId(userId);

		//DBと照合
		if (record.isEmpty() == false) { //データが見つかれば
			User user = record.get();
			String user_password = user.getPassword();//そのパスワードを取得

			//パスワードの照合
			if (password.equals(user_password)) { //パスワードが一致した場合
				// セッションスコープにユーザー情報を格納する
				session.setAttribute("userCode", user.getUserCode());
				session.setAttribute("userId", user.getUserId());
				session.setAttribute("userInfo", user);
				session.setAttribute("securityCode", "1234");

				mv.setViewName("home");
				return mv;

			} else {//パスワードが一致しなかった場合
				mv.addObject("message", "ユーザーIDとパスワードが一致しません");
				mv.setViewName("login");
				return mv;

			}

		}

		return mv;
	}

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

		//サニタイジング
		userId = Sanitizing.convert(userId);
		password = Sanitizing.convert(password);
		mail = Sanitizing.convert(mail);
		password = Sanitizing.convert(password);
		passwordCheck = Sanitizing.convert(passwordCheck);
		answer = Sanitizing.convert(answer);

		//空白があったときの処理
		if (name.equals("") || userId.equals("") || mail.equals("") || password.equals("") || q_number.equals("")
				|| answer.equals("")) {

			mv.addObject("message", "未入力の箇所があります。");
			mv.setViewName("addUser");

			return mv;
		}

		//２つのパスワードが一致していない
		if (!password.equals(passwordCheck)) {
			mv.addObject("message", "パスワードが一致していません");
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

		//登録作業

		// パラメータからオブジェクトを生成
		User user = new User(name, userId, mail, password, q_number, answer);
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

		//サニタイジング
		name = Sanitizing.convert(name);
		mail = Sanitizing.convert(mail);
		answer = Sanitizing.convert(answer);

		//空白箇所の処理
		if (name.equals("") || mail.equals("") || q_number.equals("") || answer.equals("")) {

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
				mv.addObject("password", "パスワードは" + password + "です。");
				mv.addObject("userId", "あなたのユーザーネームは" + userId + "です。");
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

		} else {
			mv.addObject("message", "データが見つかりません");
			mv.setViewName("help");

		}
		return mv;

	}

	/**
	 * プロフィールを表示
	 */
	@RequestMapping("/profile")
	public ModelAndView profile(ModelAndView mv) {
		//セッションからuserCodeを取得
		int userCode = (int) session.getAttribute("userCode");

		//userCodeでusersテーブルから情報を取得
		User user = null;
		Optional<User> record = userRepository.findById(userCode);
		if (record.isEmpty() == false) {
			user = record.get();
		}
		mv.addObject("name", user.getName());
		mv.addObject("mail", user.getEmail());
		mv.addObject("userId", user.getUserId());
		mv.setViewName("profile");
		return security(mv);
	}

	/**
	 * パスワードを表示
	 */
	@RequestMapping("/password")
	public ModelAndView password(ModelAndView mv) {
		int userCode = (int) session.getAttribute("userCode");

		User user = null;
		Optional<User> record = userRepository.findById(userCode);
		if (record.isEmpty() == false) {
			user = record.get();
		}
		mv.addObject("name", user.getName());
		mv.addObject("mail", user.getEmail());
		mv.addObject("userId", user.getUserId());
		mv.addObject("password", user.getPassword());
		mv.setViewName("profile");
		return security(mv);
	}

	/**
	 * プロフィール変更画面
	 */
	@RequestMapping("/profile/update")
	public ModelAndView update(ModelAndView mv) {
		int userCode = (int) session.getAttribute("userCode");

		User user = null;
		Optional<User> record = userRepository.findById(userCode);
		if (record.isEmpty() == false) {
			user = record.get();
		}
		mv.addObject("name", user.getName());
		mv.addObject("mail", user.getEmail());
		mv.addObject("password", user.getPassword());

		mv.setViewName("updateProfile");
		return security(mv);
	}

	/**
	 * プロフィール変更画面
	 */
	@PostMapping("/profile/update")
	public ModelAndView update2(
			@RequestParam("name") String name,
			@RequestParam("mail") String mail,
			@RequestParam("password") String password,
			ModelAndView mv) {

		//サニタイジング
		name = Sanitizing.convert(name);
		mail = Sanitizing.convert(mail);
		password = Sanitizing.convert(password);

		//test
		//			System.out.println(name);
		//空白があったときの処理
		if (name.equals("") || mail.equals("") || password.equals("")) {

			mv.addObject("message", "未入力の箇所があります。");
			mv.setViewName("updateProfile");

			return security(mv);
		}

		int userCode = (int) session.getAttribute("userCode");
		Optional<User> record2 = userRepository.findById(userCode);
		User user = null;
		if (record2.isEmpty() == false) {
			user = record2.get();
		}

		// パラメータからオブジェクトを生成
		User update = new User(userCode, name, user.getUserId(), mail, password, user.getQ_number(), user.getAnswer());
		// userテーブルへの登録
		userRepository.saveAndFlush(update);

		//test
		//			System.out.println(mail);
		mv.addObject("name", name);
		mv.addObject("mail", mail);

		mv.setViewName("profile");
		return security(mv);
	}

	/**
	 * ホーム画面
	 */

	@GetMapping("/home")
	public ModelAndView home(ModelAndView mv) {

		mv.setViewName("home");
		return security(mv);
	}

	/**
	 * ログアウトを実行
	 */
	@RequestMapping("/logout")
	public String logout() {
		session.invalidate();
		// ログイン画面表示処理を実行するだけ
		return login();
	}

}
