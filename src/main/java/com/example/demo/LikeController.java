package com.example.demo;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LikeController {
	@Autowired
	HttpSession session;

	@Autowired
	LikeRepository likeRepository;

	@Autowired
	TweetRepository tweetRepository;

//likeボタンが押されたとき
	@RequestMapping("/like")
	public ModelAndView like(
			@RequestParam("tweet_code") int tweetCode,
			ModelAndView mv) {
		//get userCode from session
		int userCode = (int) session.getAttribute("userCode");

		//押されたtweetのlikeだけ増やす
		Like like = new Like(userCode, tweetCode);
		likeRepository.saveAndFlush(like);


		//全tweetcodeがそれぞれ何個あるか調べる
//		long countAll = likeRepository.countAll();
//		for(int i = 1; i<= )
//       long count = likeRepository.countByTweetCode(tweetCode);
//       mv.addObject("count", count);

       //tweetテーブルを全件取得し、tweetのみ表示
    		List<Tweet> tweets = tweetRepository.findAll();
    		mv.addObject("tweets", tweets);

       mv.setViewName("tweet");
		return mv;
    }
}
