package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LikeController extends SecurityController{
	@Autowired
	LikeRepository likeRepository;

	@Autowired
	HttpSession session;

	@Autowired
	TweetRepository tweetRepository;

	@Autowired
	UserRepository userRepository;

	/**
	  *likeボタンが押されたとき
	 */

	@RequestMapping("/like/{tweetCode}")
	public ModelAndView like(
			@PathVariable(name = "tweetCode") int tweetCode,
			ModelAndView mv) {
		//get userCode from session
		int userCode = (int) session.getAttribute("userCode");

		//もしlikesに同じ値が保存されていたら、likeを削除
		Optional<Like> record = likeRepository.findByUserCodeAndTweetCode(userCode, tweetCode);
		if (record.isEmpty() == false) {//もしすでにlikeしていたら
			Like liked = record.get();

			//recordに来た情報を削除
			likeRepository.deleteById(liked.getCode());

			//tweetテーブルのlikesを１減らす
			Tweet tweet = null;
			Optional<Tweet> record3 = tweetRepository.findById(tweetCode);
			if (record3.isEmpty() == false) {
				tweet = record3.get();
				int likes = tweet.getLikes();
				likes--;

				//更新する
				Tweet update = new Tweet(tweet.getCode(), likes, tweet.getUser_code(), tweet.getTweet(),
						tweet.getDate());
				tweetRepository.saveAndFlush(update);
			}

			//表示用
			//全データを取得して表示
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
				Tweet tweeted = tweets.get(i);
				int tweet_code = tweeted.getCode();

				//デフォルト
				boolean like = false;

				//ハートを押したことがあるかlikeテーブルで確認
				Optional<Like> record4 = likeRepository.findByUserCodeAndTweetCode(user_code, tweet_code);
				if (record4.isEmpty() == false) { //押したことがある場合
					like = true;
				}

				hearts.add(like);
			}

			//test
			//		    System.out.println(hearts);

			mv.addObject("hearts", hearts);
			mv.setViewName("tweet");
			return security(mv);

		} else {//まだいいねしてなかったら

			//押されたtweetのlikeだけ増やす
			Like like = new Like(userCode, tweetCode);
			likeRepository.saveAndFlush(like);

			//Tweetテーブルのlikesを1増やす
			Tweet tweet2 = null;
			Optional<Tweet> record2 = tweetRepository.findById(tweetCode);
			if (record2.isEmpty() == false) {
				tweet2 = record2.get();
				int likes = tweet2.getLikes();
				likes++;

				//更新する
				Tweet update = new Tweet(tweet2.getCode(), likes, tweet2.getUser_code(), tweet2.getTweet(),
						tweet2.getDate());
				tweetRepository.saveAndFlush(update);
			}

			//表示用
			//tweetテーブルを全件取得し、tweetと時間、like数を表示
			List<Tweet> tweets = tweetRepository.findByOrderByCodeDesc();
			mv.addObject("tweets", tweets);

			//userIdの取得
			List<String> users = new ArrayList<>();
			String b = "";
			int a = 0;
			for (int k = 0; k < tweets.size(); k++) {
				Tweet tweetRecord = tweets.get(k);
				a = tweetRecord.getUser_code();
				Optional<User> record5 = userRepository.findById(a);
				if (record2.isEmpty() == false) {
					User userData = record5.get();
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
				Tweet tweeted = tweets.get(i);
				int tweet_code = tweeted.getCode();

				//デフォルト
				boolean liked = false;

				//ハートを押したことがあるかlikeテーブルで確認
				Optional<Like> record4 = likeRepository.findByUserCodeAndTweetCode(user_code, tweet_code);
				if (record4.isEmpty() == false) { //押したことがある場合
					liked = true;
				}

				hearts.add(liked);
			}

			//test
			System.out.println(hearts);

			mv.addObject("hearts", hearts);
			mv.setViewName("tweet");
			return security(mv);
		}
	}
}
