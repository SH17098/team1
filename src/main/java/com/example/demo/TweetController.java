package com.example.demo;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TweetController extends SecurityController{
	@Autowired
	HttpSession session;

	@Autowired
	TweetRepository tweetRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	LikeRepository likeRepository;

	@Autowired
	ReplyRepository replyRepository;

	/**
	  *掲示板の初期表示
	 */

	@RequestMapping("/tweet")
	public ModelAndView tweet(ModelAndView mv) {
		//tweetテーブルを全件取得し、tweetのみ表示
		List<Tweet> tweets = tweetRepository.findByOrderByCodeDesc();
		mv.addObject("tweets", tweets);

		//userIdの取得
		List<String> users = new ArrayList<>();
		String b = "";
		int a = 0;
		for (int k = 0; k < tweets.size(); k++) {
			Tweet tweetRecord = tweets.get(k);
			a = tweetRecord.getUser_code();
			Optional<User> record2 = userRepository.findById(a); //userCodeを使ってuserIDを取得
			if (record2.isEmpty() == false) {
				User userData = record2.get();
				b = userData.getUserId();
				users.add(b);
			}
		}
		mv.addObject("users", users);

		//ハートの色を設定

		List<Boolean> hearts = new ArrayList<>();

		//userCodeを取得
		int user_code = (int) session.getAttribute("userCode");

		for (int i = 0; i < tweets.size(); i++) {
			//tweetCodeを取得する
			Tweet tweet = tweets.get(i);
			int tweet_code = tweet.getCode();

			//test
//			System.out.println(user_code + "/" + tweet_code);

			//デフォルト
			boolean like = false;

			//ハートを押したことがあるかlikeテーブルで確認
			Optional<Like> record = likeRepository.findByUserCodeAndTweetCode(user_code, tweet_code);
			if (record.isEmpty() == false) { //押したことがある場合
				like = true;
			}

			hearts.add(like); //trueまたはfalseの値をリストに追加していく
		}
		//test
		//		System.out.println(hearts);

		mv.addObject("hearts", hearts);

		mv.setViewName("tweet");//掲示板を表示
		return security(mv);
	}


	/**
	  *新規投稿作成
	 */

	@PostMapping("/tweet/add")
	public ModelAndView addTweet2(
			@RequestParam("tweet") String tweet, //作成した投稿内容を取得
			ModelAndView mv) {

		//サニタイジング
		tweet = Sanitizing.convert(tweet);

		//未入力の場合
		if (tweet.equals("")) {
			mv.addObject("error", "投稿内容を入力してください");
			tweet(mv);

			mv.setViewName("tweet");
			return security(mv);
		}

		//test用
		//		session.setAttribute("userCode", 1);

		//セッションからusercodeを取得
		int user_code = (int) session.getAttribute("userCode");

		//日付を取得
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(timestamp);
		Date today = Date.valueOf(str);

		//インスタンス生成して、userCodeと投稿内容をtweetテーブルに保存
		Tweet newTweet = new Tweet(0, user_code, tweet, today);
		tweetRepository.saveAndFlush(newTweet);

		//表示用
		List<Tweet> tweets = tweetRepository.findByOrderByCodeDesc();
		mv.addObject("tweets", tweets);

		//userCodeからuserIDを取得してリスト化
		//userIdの取得
		List<String> users = new ArrayList<>();
		String b = "";
		int a = 0;
		for (int k = 0; k < tweets.size(); k++) {
			Tweet tweetRecord = tweets.get(k);
			a = tweetRecord.getUser_code();
			Optional<User> record2 = userRepository.findById(a);
			if (record2.isEmpty() == false) {
				User userData = record2.get();
				b = userData.getUserId();
				users.add(b);
			}
		}
		mv.addObject("users", users);

		//ハートの色を設定
		List<Boolean> hearts = new ArrayList<>();

		//userCodeを取得
		int userCode = (int) session.getAttribute("userCode");

		for (int i = 0; i < tweets.size(); i++) {
			//tweetCodeを取得する
			Tweet tweet2 = tweets.get(i);
			int tweet_code = tweet2.getCode();

			//test
			//			System.out.println(userCode + "/" + tweet_code);

			//デフォルト
			boolean like = false;

			//ハートを押したことがあるかlikeテーブルで確認
			Optional<Like> record = likeRepository.findByUserCodeAndTweetCode(user_code, tweet_code);
			if (record.isEmpty() == false) { //押したことがある場合
				like = true;
			}

			hearts.add(like);
		}
		//test
		//	System.out.println(hearts);

		mv.addObject("hearts", hearts);

		mv.setViewName("Tweet");
		return security(mv);
	}

	/**
	  *編集機能
	 */

	//ページ移動
	@PostMapping("/tweet/edit")
	public ModelAndView edit1(@RequestParam("editCode") String editCode,
			ModelAndView mv){
		//ツイートの取得
		//int型をStringに変換
		int code = Integer.parseInt(editCode);

		session.setAttribute("code",code);
		mv.setViewName("edit");
		return security(mv);


	}


	//編集
	@RequestMapping("/tweet/edit")
	public ModelAndView edit(@RequestParam("tweet") String tweet,
			ModelAndView mv
			) {
		tweet = Sanitizing.convert(tweet);

		if (tweet.equals("")) {

			mv.addObject("message", "未入力です");
			mv.setViewName("edit");

			return security(mv);
		}

		int code = (int) session.getAttribute("code");
		Optional<Tweet> recordEdit = tweetRepository.findById(code);
		Tweet Edit = null;
		if (recordEdit.isEmpty() == false) {
			Edit = recordEdit.get();
		}

		// パラメータからオブジェクトを生成
		Tweet edit = new Tweet(code,Edit.getLikes(), Edit.getUser_code(), tweet, Edit.getDate());
		// tweetテーブルへの登録
		tweetRepository.saveAndFlush(edit);



		mv.addObject("tweet", tweet);



		mv.setViewName("edit");
		return security(mv);
	}


	/**
	  *投稿削除
	 */

	@PostMapping("/tweet/delete")
	@Transactional
	public ModelAndView delete(
			@RequestParam("deleteCode") String delete_code,
			ModelAndView mv) {
		int deleteCode = Integer.parseInt(delete_code);
		//test
		//		System.out.println(deleteCode);

		//まず子テーブルlikesの履歴を消す
		likeRepository.deleteByTweetCode(deleteCode);

		//それから親テーブルtweet自体を消す
		tweetRepository.deleteById(deleteCode);

		//再度表示用
		//tweetテーブルを全件取得し、tweetのみ表示
		List<Tweet> tweets = tweetRepository.findByOrderByCodeDesc();
		mv.addObject("tweets", tweets);

		//userIdの取得
		List<String> users = new ArrayList<>();
		String b = "";
		int a = 0;
		for (int k = 0; k < tweets.size(); k++) {
			Tweet tweetRecord = tweets.get(k);
			a = tweetRecord.getUser_code();
			Optional<User> record2 = userRepository.findById(a);
			if (record2.isEmpty() == false) {
				User userData = record2.get();
				b = userData.getUserId();
				users.add(b);
			}
		}
		mv.addObject("users", users);

		//ハートの色を設定
		List<Boolean> hearts = new ArrayList<>();

		//userCodeを取得
		int user_code = (int) session.getAttribute("userCode");

		for (int i = 0; i < tweets.size(); i++) {
			//tweetCodeを取得する
			Tweet tweet = tweets.get(i);
			int tweet_code = tweet.getCode();

			//test
			System.out.println(user_code + "/" + tweet_code);
			//デフォルト
			boolean like = false;

			//ハートを押したことがあるかlikeテーブルで確認
			Optional<Like> record = likeRepository.findByUserCodeAndTweetCode(user_code, tweet_code);
			if (record.isEmpty() == false) { //押したことがある場合
				like = true;
			}

			hearts.add(like);
		}
		//test
		//		System.out.println(hearts);

		mv.addObject("hearts", hearts);

		mv.setViewName("tweet");
		return security(mv);
	}

	/**
	  *リプライ機能
	 */

	@RequestMapping("/reply/{tweetCode}/{tweetUserCode}")
	@Transactional
	public ModelAndView reply(
			@PathVariable(name = "tweetCode") int tweetCode,
			@PathVariable(name = "tweetUserCode") int userCode,
			ModelAndView mv) {
		//セッションに保存
		session.setAttribute("tweetCode", tweetCode);
		session.setAttribute("tweetUser", userCode);

		//選択されたツイートも一番上に表示する
		Optional<Tweet> record = tweetRepository.findById(tweetCode);
		Tweet tweet = null;
		String tweetUser = "";
		if (record.isEmpty() == false) {
			tweet = record.get();
			mv.addObject("tweet", tweet); //選択したツイートの取得完了

			//userCodeからuserIDを取得
			Optional<User> record3 = userRepository.findById(userCode);
			User user = null;
			if (record3.isEmpty() == false) {
				user = record3.get();
			}
			tweetUser = user.getUserId(); //userネームの取得完了
			mv.addObject("tweetUser", tweetUser);

		}

		//tweetCodeからそれに関するreplyを取得
		List<Reply> replies = replyRepository.findByTweetCode(tweetCode);

		//なかった場合
		if (replies.size() == 0) {
			mv.addObject("none", "まだコメントはありません。");
			mv.setViewName("reply");
			return security(mv);

		} else {//あった場合
			mv.addObject("replies", replies); //tweetに対するreply取得

			//表示用
			//userIdの取得
			List<String> users = new ArrayList<>();
			String b = "";
			int a = 0;
			for (int k = 0; k < replies.size(); k++) {
				Reply replyRecord = replies.get(k);
				a = replyRecord.getUserCode();
				Optional<User> record2 = userRepository.findById(a);
				if (record2.isEmpty() == false) {
					User userData = record2.get();
					b = userData.getUserId();
					users.add(b);
				}
			}
			mv.addObject("users", users); //replyをしたuserのネーム取得

		}

		mv.setViewName("reply");
		return security(mv);
	}

	/**
	  *リプライ機能
	 */

	@PostMapping("/reply/add")
	public ModelAndView addReply(
			@RequestParam("reply") String reply,
			ModelAndView mv) {

		//サニタイジング
		reply = Sanitizing.convert(reply);

		int tweetCode = (int) session.getAttribute("tweetCode");
		int tweetUser = (int) session.getAttribute("tweetUser");

		//null処理
		if (reply.equals("")) {
			mv.addObject("error", "投稿内容を入力してください");

        //入力があった場合
		} else {

			int userCode = (int) session.getAttribute("userCode");

			//replyテーブルに保存
			Reply new_reply = new Reply(userCode, tweetCode, reply);
			replyRepository.saveAndFlush(new_reply);

		}
		reply(tweetCode, tweetUser, mv);

		mv.setViewName("reply");
		return security(mv);
	}



	/**
	  *削除機能
	 */

	@PostMapping("/reply/delete")
	@Transactional
	public ModelAndView deleteReply(
			@RequestParam("deleteCode") String delete_code,
			ModelAndView mv) {
		//削除

		int deleteCode = Integer.parseInt(delete_code);
		//test
		//		System.out.println(deleteCode);

		//replyテーブルの履歴を消す
		replyRepository.deleteById(deleteCode);

		//再度表示用
		int tweetCode = (int) session.getAttribute("tweetCode");
		int tweetUser = (int) session.getAttribute("tweetUser");

		reply(tweetCode, tweetUser, mv);

		mv.setViewName("reply");
		return security(mv);
	}


	/**
	  *ツイートが古い順に表示
	 */

	@GetMapping("/tweet/old")
	public ModelAndView oldLine(ModelAndView mv) {
		//レビューの数で降順に取得
				List<Tweet> tweets = tweetRepository.findByOrderByCodeAsc();

				mv.addObject("tweets", tweets);
				//userIdの取得
				List<String> users = new ArrayList<>();
				String b = "";
				int a = 0;
				for (int k = 0; k < tweets.size(); k++) {
					Tweet tweetRecord = tweets.get(k);
					a = tweetRecord.getUser_code();
					Optional<User> record2 = userRepository.findById(a); //userCodeを使ってuserIDを取得
					if (record2.isEmpty() == false) {
						User userData = record2.get();
						b = userData.getUserId();
						users.add(b);
					}
				}
				mv.addObject("users", users);

				//ハートの色を設定

				List<Boolean> hearts = new ArrayList<>();

				//userCodeを取得
				int user_code = (int) session.getAttribute("userCode");

				for (int i = 0; i < tweets.size(); i++) {
					//tweetCodeを取得する
					Tweet tweet = tweets.get(i);
					int tweet_code = tweet.getCode();

					//test
//					System.out.println(user_code + "/" + tweet_code);

					//デフォルト
					boolean like = false;

					//ハートを押したことがあるかlikeテーブルで確認
					Optional<Like> record = likeRepository.findByUserCodeAndTweetCode(user_code, tweet_code);
					if (record.isEmpty() == false) { //押したことがある場合
						like = true;
					}

					hearts.add(like); //trueまたはfalseの値をリストに追加していく
				}
				//test
				//		System.out.println(hearts);

				mv.addObject("hearts", hearts);

				mv.setViewName("tweet");//掲示板を表示
				return security(mv);
			}


	/**
	  *ツイートが新しい順に表示
	 */

	@GetMapping("/tweet/new")
	public ModelAndView newLine(ModelAndView mv) {

				List<Tweet> tweets = tweetRepository.findByOrderByCodeDesc();

				mv.addObject("tweets", tweets);
				//userIdの取得
				List<String> users = new ArrayList<>();
				String b = "";
				int a = 0;
				for (int k = 0; k < tweets.size(); k++) {
					Tweet tweetRecord = tweets.get(k);
					a = tweetRecord.getUser_code();
					Optional<User> record2 = userRepository.findById(a); //userCodeを使ってuserIDを取得
					if (record2.isEmpty() == false) {
						User userData = record2.get();
						b = userData.getUserId();
						users.add(b);
					}
				}
				mv.addObject("users", users);

				//ハートの色を設定

				List<Boolean> hearts = new ArrayList<>();

				//userCodeを取得
				int user_code = (int) session.getAttribute("userCode");

				for (int i = 0; i < tweets.size(); i++) {
					//tweetCodeを取得する
					Tweet tweet = tweets.get(i);
					int tweet_code = tweet.getCode();

					//test
//					System.out.println(user_code + "/" + tweet_code);

					//デフォルト
					boolean like = false;

					//ハートを押したことがあるかlikeテーブルで確認
					Optional<Like> record = likeRepository.findByUserCodeAndTweetCode(user_code, tweet_code);
					if (record.isEmpty() == false) { //押したことがある場合
						like = true;
					}

					hearts.add(like); //trueまたはfalseの値をリストに追加していく
				}
				//test
				//		System.out.println(hearts);

				mv.addObject("hearts", hearts);

				mv.setViewName("tweet");//掲示板を表示
				return security(mv);
			}



}
