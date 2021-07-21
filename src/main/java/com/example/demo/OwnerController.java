package com.example.demo;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OwnerController {
	@Autowired
	HttpSession session;

	@Autowired
	OwnerRepository ownerRepository;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	TweetRepository tweetRepository;

	@Autowired
	FlashcardRepository flashcardRepository;

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	ReplyRepository replyRepository;

	/**
	 * ログイン画面を表示
	 */

	//http://localhost:8080/ownership/studytool
	@RequestMapping("/ownership/studytool")
	public String login() {
		session.invalidate();
		return "ownerLogin";
	}

	@RequestMapping("/owner/top")
	public String top() {
		return "ownerTop";
	}


	//ログイン認証後ホーム画面へ
	@PostMapping("/owner/login")
	public ModelAndView login(
			@RequestParam("id") String id,
			@RequestParam("password") String password,
			ModelAndView mv) {
		if (id.equals("") || password.equals("")) {
			mv.addObject("error", "未入力があります");
			mv.setViewName("ownerLogin");
			return mv;
		} else {

			Optional<Owner> record = ownerRepository.findByIdAndPassword(id, password);
			if (record.isEmpty()) {
				mv.addObject("error", "idもしくはpasswordが一致しません");
				mv.setViewName("ownerLogin");
				return mv;
			} else {
				mv.setViewName("ownerTop");
				return mv;

			}
		}
	}

	//参考書一覧表示
	@RequestMapping("/owner/booklist")
	public ModelAndView booklist(ModelAndView mv) {
		//登録された参考書情報をデータベースより取得し表示
		List<Book> books = bookRepository.findByOrderByCodeAsc();

		mv.addObject("books", books);

		mv.setViewName("ownerbook");
		return mv;
	}

	//参考書レビュー表示
	@RequestMapping("/owner/bookreview/{bookCode}")
	public ModelAndView bookReview(
			@PathVariable(name = "bookCode") int bookCode,
			ModelAndView mv) {
		List<Comment> bookReview = commentRepository.findByBookCode(bookCode);

		boolean review = false;

		if (bookReview.size() == 0) {
			mv.addObject("none", "レビューなし");
		} else {
			mv.addObject("reviews", bookReview);
		}

		mv.setViewName("ownerBookReview");
		return mv;
	}

	//参考書更新画面
	@RequestMapping("/owner/book/update")
	public ModelAndView bookUpdate(
			@RequestParam("book_code") String book_code,
			ModelAndView mv) {
		int bookCode = Integer.parseInt(book_code);
		Book book = null;

		Optional<Book> record = bookRepository.findById(bookCode);
		if (record.isEmpty() == false) {
			book = record.get();
			mv.addObject("book", book);
		}

		mv.setViewName("ownerBookUpdate");
		return mv;
	}

	//参考書更新
	@PostMapping("/owner/book/update")
	public ModelAndView bookUpdate2(
			@RequestParam("book_code") int bookCode,
			@RequestParam("name") String name,
			@RequestParam("price") String book_price,
			ModelAndView mv) {

		int price = Integer.parseInt(book_price);
		Optional<Book> record = bookRepository.findById(bookCode);

		Book book = null;
        if (record.isEmpty() == false) {
			book = record.get();
		}
		//更新
		Book update = new Book(bookCode, name, price, book.getRate(), book.getComment());
		bookRepository.saveAndFlush(update);

		return booklist(mv);
	}

	//参考書削除
	@RequestMapping("/owner/book/delete")
	public ModelAndView bookDelete(
			@RequestParam("book_code") String book_code,
			ModelAndView mv) {
		int bookCode = Integer.parseInt(book_code);

		bookRepository.deleteById(bookCode);


		return booklist(mv);
	}
	//参考書レビュー削除
	@RequestMapping("/owner/bookreview/delete")
	public ModelAndView bookReviewDelete(
			@RequestParam("book_code") String book_code,
			@RequestParam("review_code") String review_code,
			ModelAndView mv) {
		int reviewCode = Integer.parseInt(review_code);
		int bookCode = Integer.parseInt(book_code);

		commentRepository.deleteById(reviewCode);

		Optional<Book> record = bookRepository.findById(bookCode);
		Book book = null;
		int comment = 0;
		if(record.isEmpty() == false) {
			book = record.get();
			comment = book.getComment();
			comment--;
		}

		Book update = new Book(bookCode, book.getName(), book.getPrice(), book.getRate(), comment);
		bookRepository.saveAndFlush(update);


		return bookReview(bookCode, mv);
	}

	//tweet一覧表示
	@RequestMapping("/owner/tweet")
	public ModelAndView tweets(ModelAndView mv) {
		//登録されたtweet情報をデータベースより取得し表示
		List<Tweet> tweets = tweetRepository.findByOrderByCodeAsc();

		mv.addObject("tweets", tweets);

		mv.setViewName("ownerTweet");
		return mv;
	}

	//リプライ一覧表示
	@RequestMapping("/owner/tweet/reply/{tweetCode}")
	public ModelAndView tweetsReply(
			@PathVariable(name="tweetCode") int tweetCode,
			ModelAndView mv) {
		//登録されたreply情報をデータベースより取得し表示
		List<Reply> replies = replyRepository.findByTweetCode(tweetCode);

		if(replies.size() == 0) {
			mv.addObject("none", "コメントはありません");
		}else {

		mv.addObject("replies", replies);
		}

		mv.setViewName("ownerTweetReply");
		return mv;
	}

	//tweetの削除
	@RequestMapping("/owner/tweet/delete")
	public ModelAndView deleteTweet(
			@RequestParam("tweet_code") String tweet_code,
			ModelAndView mv) {
		//まずリプライを消す
		int tweetCode = Integer.parseInt(tweet_code);
		replyRepository.deleteByTweetCode(tweetCode);
		//次にtweet自体を消す
		tweetRepository.deleteById(tweetCode);
		return tweets(mv);
	}

	//replyの削除
	@RequestMapping("/owner/tweet/reply/delete")
	public ModelAndView deleteReply(
			@RequestParam("tweet_code") String tweet_code,
			@RequestParam("reply_code") String reply_code,
			ModelAndView mv) {
		//リプライを消す
		int replyCode = Integer.parseInt(reply_code);
		int tweetCode = Integer.parseInt(tweet_code);
		replyRepository.deleteById(replyCode);

		return tweetsReply(tweetCode, mv);
	}


//
//	//flashcard一覧表示
//	@RequestMapping("/owner/tweet")
//	public ModelAndView tweets(ModelAndView mv) {
//


}
