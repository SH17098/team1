package com.example.demo;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
public class TweetController {
	@Autowired
	HttpSession session;

	@Autowired
	TweetRepository tweetRepository;

	@Autowired
	LikeRepository likeRepository;

//掲示板の初期表示
	@RequestMapping("/tweet")
	public ModelAndView tweet(ModelAndView mv) {
    //tweetテーブルを全件取得し、tweetのみ表示
		List<Tweet> tweets = tweetRepository.findByOrderByCodeAsc();
		mv.addObject("tweets", tweets);

	//ハートの色を設定
		List<Boolean> hearts = new ArrayList<>();

		//userCodeを取得
		int user_code = (int) session.getAttribute("userCode");


		for(int i = 0; i < tweets.size(); i++) {
			//tweetCodeを取得する
			Tweet tweet = tweets.get(i);
			int tweet_code = tweet.getCode();

			//test
			System.out.println(user_code + "/" + tweet_code);
			//デフォルト
			boolean like = false;

			//ハートを押したことがあるかlikeテーブルで確認
			Optional<Like> record = likeRepository.findByUserCodeAndTweetCode(user_code, tweet_code);
			if(record.isEmpty() == false) { //押したことがある場合
				like = true;
			}

			hearts.add(like);
		}
		//test
		System.out.println(hearts);

	    mv.addObject("hearts", hearts);

		mv.setViewName("tweet");//掲示板を表示
		return mv;
	}

//新規投稿作成画面の表示
	@RequestMapping("/tweet/add")
	public ModelAndView addTweet(ModelAndView mv) {
		mv.setViewName("addTweet");
		return mv;
	}

//新規投稿作成
	@PostMapping("/tweet/add")
	public ModelAndView addTweet2(
			@RequestParam("tweet") String tweet,//作成した投稿内容を取得
			ModelAndView mv) {
		//test用
		session.setAttribute("userCode", 1);
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

		List<Tweet> tweets = tweetRepository.findByOrderByCodeAsc();
		mv.addObject("tweets", tweets);

		mv.setViewName("Tweet");
		return mv;
	}

}
