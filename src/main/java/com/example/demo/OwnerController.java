package com.example.demo;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OwnerController extends SecurityController2{
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
	LikeRepository likeRepository;

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	ReplyRepository replyRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ReportRepository reportRepository;

	/**
	 * ログイン画面を表示
	 */

	//http://localhost:8080/ownership/studytool
	@RequestMapping("/ownership/studytool")
	public String login() {
		session.invalidate();
		return "ownerLogin";
	}

	//ログアウト
	@RequestMapping("/ownership/studytool/logout")
	public String logout() {
		session.invalidate();
		return "ownerLogin";
	}

	@RequestMapping("/owner/top")
	public ModelAndView top(ModelAndView mv){
		List<Report> reports = reportRepository.findAll();
		String answer = "none";
		for(int i = 1; i<reports.size(); i++) {
			Report report = reports.get(i);
			answer = report.getAnswer();
			if(answer.equals("まだ返答はありません。")) {
				mv.addObject("notification","🔔");
				break;
			}
		}


		mv.setViewName("ownerTop");
		return security(mv);
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
				return security(mv);
			} else {
				session.setAttribute("securityCode", "1234");
				return top(mv);

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
		return security(mv);
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
		return security(mv);
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
		return security(mv);
	}

	//参考書更新
	@PostMapping("/owner/book/update")
	public ModelAndView bookUpdate2(
			@RequestParam("book_code") int bookCode,
			@RequestParam("name") String name,
			@RequestParam("price") String book_price,
			@RequestParam("amazon") String amazon,
			ModelAndView mv) {

		int price = Integer.parseInt(book_price);
		Optional<Book> record = bookRepository.findById(bookCode);

		Book book = null;
        if (record.isEmpty() == false) {
			book = record.get();
		}

        //url追加

		//更新
		Book update = new Book(bookCode, name, price, book.getRate(), book.getComment(), amazon);
		bookRepository.saveAndFlush(update);

		return booklist(mv);
	}

	//参考書削除
	@RequestMapping("/owner/book/delete")
	@Transactional
	public ModelAndView bookDelete(
			@RequestParam("book_code") String book_code,
			ModelAndView mv) {
		int bookCode = Integer.parseInt(book_code);

		commentRepository.deleteByBookCode(bookCode);
		bookRepository.deleteById(bookCode);


		return booklist(mv);
	}
	//参考書レビュー削除
	@RequestMapping("/owner/bookreview/delete")
	@Transactional
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

		Book update = new Book(bookCode, book.getName(), book.getPrice(), book.getRate(), comment, book.getAmazon());
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
		return security(mv);
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
		return security(mv);
	}

	//tweetの削除
	@RequestMapping("/owner/tweet/delete")
	@Transactional
	public ModelAndView deleteTweet(
			@RequestParam("tweet_code") String tweet_code,
			ModelAndView mv) {
		//まずリプライを消す
		int tweetCode = Integer.parseInt(tweet_code);
		replyRepository.deleteByTweetCode(tweetCode);
		//次にlikeを消す
		likeRepository.deleteByTweetCode(tweetCode);
		//次にtweet自体を消す
		tweetRepository.deleteById(tweetCode);
		return tweets(mv);
	}

	//replyの削除
	@RequestMapping("/owner/tweet/reply/delete")
	@Transactional
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



	//flashcard共有されてるもの表示
	@RequestMapping("/owner/flashcard")
	public ModelAndView flashcards(ModelAndView mv) {
		//flashcardの中で、shareが1になってるものを一覧表示
		List<Flashcard> flashcards = flashcardRepository.findByShare(1);
		if(flashcards.size() == 0) {
			mv.addObject("none", "現在共有されているものはありません。");
		}

		mv.addObject("flashcards", flashcards);
		mv.setViewName("ownerFlashcard");
		return security(mv);
	}

	//flashcard共有停止
	@RequestMapping("/owner/flashcard/delete")
	public ModelAndView deleteFlashcard(
			@RequestParam("delete_code") String delete_code,
			ModelAndView mv) {
		int deleteCode = Integer.parseInt(delete_code);

		//コードで検索
		Flashcard flashcard = null;
		Optional<Flashcard> record = flashcardRepository.findById(deleteCode);
		if(record.isEmpty()==false) {
			flashcard = record.get();
		}
		//flashcardのshareを0に変更
		int share = 0;
		Flashcard update = new Flashcard(flashcard.getCode(),flashcard.getUserCode(),share, flashcard.getQuestion(), flashcard.getAnswer(), flashcard.getExplanation());
		flashcardRepository.saveAndFlush(update);

		return flashcards(mv);
	}

	//user情報の管理画面
	@RequestMapping("/owner/user")
	public ModelAndView users(ModelAndView mv) {
		//全情報を取得して一覧表示
		List<User> users = userRepository.findAll();
		mv.addObject("users", users);

        mv.setViewName("ownerUser");
		return security(mv);
	}
//
//
//	//user情報削除
//	@RequestMapping("/owner/user/delete")
//	public ModelAndView userDelete(
//			@RequestParam("user_code") String user_code,
//			ModelAndView mv) {
//		int userCode = Integer.parseInt(user_code);
//
//		userRepository.deleteById(userCode);
//
//
//		return users(mv);
//	}

	//reportの管理画面
	@RequestMapping("/owner/report")
	public ModelAndView reports(ModelAndView mv) {
		List<Report> reports = reportRepository.findByOrderByCodeAsc();
		if(reports.size() == 0) {
			mv.addObject("none", "まだ問い合わせはありません。");
		}else {
		    mv.addObject("reports", reports);
		}

        mv.setViewName("ownerReport");
        return security(mv);
	}

	@PostMapping("/owner/report")
	public ModelAndView report(
			@RequestParam("report_code") String report_code,
			ModelAndView mv) {
		int reportCode = Integer.parseInt(report_code);
	    Optional<Report> record = reportRepository.findById(reportCode);
        Report report = null;
	    if(record.isEmpty() == false) {
	    	report = record.get();
	    }
	    //対処済み報告を
	    String answer = "対処しました。";
	    Report update = new Report(report.getCode(), report.getUserCode(), report.getType(), report.getReport(), answer);
	    reportRepository.saveAndFlush(update);

		return reports(mv);
	}

}
