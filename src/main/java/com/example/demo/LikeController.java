package com.example.demo;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LikeController {
	@Autowired
	LikeRepository likeRepository;

	@Autowired
	HttpSession session;

	@Autowired
	TweetRepository tweetRepository;

	//likeボタンが押されたとき
	@RequestMapping("/like/{tweetCode}")
	public ModelAndView like(
			@PathVariable(name="tweetCode") int tweetCode,
			ModelAndView mv) {
		//get userCode from session
		int userCode = (int) session.getAttribute("userCode");

		//もしlikesに同じ値が保存されていたら、likeを削除
		Optional<Like> record = likeRepository.findByUserCodeAndTweetCode(userCode, tweetCode);
		if(record.isEmpty() == false) {//もしすでにlikeしていたら
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
				Tweet update = new Tweet(tweet.getCode(), likes, tweet.getUser_code(), tweet.getTweet(), tweet.getDate());
				tweetRepository.saveAndFlush(update);
			}

			//全データを取得して表示
		    List<Tweet> tweets = tweetRepository.findByOrderByCodeAsc();
		    mv.addObject("tweets", tweets);

		    //ハートの表示用
		    boolean heart = false;

		    //test
//		    System.out.println(heart);

            mv.addObject("heart", heart);
		    mv.setViewName("tweet");
		    return mv;


		}else {//まだいいねしてなかったら

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
			    Tweet update = new Tweet(tweet2.getCode(), likes, tweet2.getUser_code(), tweet2.getTweet(), tweet2.getDate());
			    tweetRepository.saveAndFlush(update);
		    }

		//tweetテーブルを全件取得し、tweetと時間、like数を表示
		    List<Tweet> tweets = tweetRepository.findByOrderByCodeAsc();
		    mv.addObject("tweets", tweets);

		    //ハートの表示用
		    boolean heart = true;

		    //test
//		    System.out.println(heart);

		    mv.addObject("heart", heart);
		    mv.setViewName("tweet");
		    return mv;
	    }
    }
}
