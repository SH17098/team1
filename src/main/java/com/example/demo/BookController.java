package com.example.demo;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BookController extends SecurityController{
	@Autowired
	HttpSession session;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	CommentRepository commentRepository;

	/**
	 * 参考書一覧の表示
	 */

	@RequestMapping("/book")
	public ModelAndView book(
			ModelAndView mv) {
		List<Book> books = bookRepository.findByOrderByCodeAsc();
		mv.addObject("books", books);

		mv.setViewName("book");
		return security(mv);
	}

	/**
	  *参考書登録画面
	 */

	@RequestMapping("/book/add")
	public ModelAndView addBook(ModelAndView mv) {
		mv.setViewName("addBook");
		return security(mv);
	}

	/**
	  *参考書登録
	 */

	@PostMapping("/book/add")
	public ModelAndView addBook2(
			@RequestParam("name") String name,
			@RequestParam("price") String book_price,
			@RequestParam("star") String star,
			@RequestParam("comment") String comment,
			ModelAndView mv) {

		//サニタイジング
		name = Sanitizing.convert(name);
		book_price = Sanitizing.convert(book_price);
		comment = Sanitizing.convert(comment);

		//test
		//		System.out.println(star);

		//未入力がある場合
		if (name.equals("") || book_price.equals("") || star.equals("none")) {
			mv.addObject("error", "すべての項目に入力してください");
			mv.addObject("name", name);
			mv.addObject("price", book_price);

			mv.setViewName("addBook");
			return mv;

		} else {//正しく入力された場合
			int price = Integer.parseInt(book_price);
			//同じ名前の本が登録されてたらエラー
			Optional<Book> record = bookRepository.findByName(name);
			if (record.isEmpty() == false) {
				mv.addObject("error", "この本はすでに登録されています");
				mv.setViewName("addBook");
				return mv;

			//同じ本がなかったら登録
			} else {
				int rate = Integer.parseInt(star);
				int review = 0;
				//本の登録
				if (!comment.equals("")) { //コメントが書かれていたら、コメント数を1にする
					review = 1;
				}
				//データベースに登録
				Book newBook = new Book(name, price, rate, review);
				bookRepository.saveAndFlush(newBook);

				List<Book> books = bookRepository.findByOrderByCodeAsc();
				mv.addObject("books", books);

			}

			//コメントが入力されていればコメントテーブルに登録
			if (!comment.equals("")) {
				Optional<Book> record2 = bookRepository.findByName(name);
				if (record2.isEmpty() == false) {
					Book newBook = record2.get();
					int bookCode = newBook.getCode(); //登録した本のcodeを取得

					Comment newComment = new Comment(bookCode, comment);
					commentRepository.saveAndFlush(newComment); //データベースにコメントを保存
				}
			}
			mv.setViewName("book");
			return security(mv);
		}

	}

	/**
	  *参考書レビューの表示
	 */

	@RequestMapping("/comment/{bookCode}")
	public ModelAndView comment(
			@PathVariable(name = "bookCode") int bookCode,
			ModelAndView mv) {

		//bookCodeが一致するものを取得
		List<Comment> comments = commentRepository.findByBookCode(bookCode);
		session.setAttribute("bookCode", bookCode);

		if (comments.size() == 0) { //なかった場合
			mv.addObject("mess", "まだレビューはありません。");
			mv.setViewName("comment");
			return mv;
		}
		mv.addObject("comments", comments);

		//表示用
		Book book = null;
		Optional<Book> record = bookRepository.findById(bookCode);
		if (record.isEmpty() == false) {
			book = record.get();
		}
		mv.addObject("name", book.getName());
		mv.addObject("price", book.getPrice());
		mv.setViewName("comment");
		return security(mv);
	}

	/**
	  *レビューの追加
	 */

	@PostMapping("/comment/add")
	public ModelAndView addComment(
			@RequestParam("comment") String comment,
			@RequestParam("star") String star,
			ModelAndView mv) {

		//サニタイジング
		comment = Sanitizing.convert(comment);

		//get bookCode from session
		int bookCode = (int) session.getAttribute("bookCode");

		//未入力がある場合
		if (comment.equals("") || star.equals("none")) {
			mv.addObject("error", "未入力箇所があります");
			comment(bookCode, mv);

			mv.setViewName("comment");
			return security(mv);
		}

		//add
		Comment newComment = new Comment(bookCode, comment);
		commentRepository.saveAndFlush(newComment); //データベースにコメントを保存

		//星評価の計算と保存
		//bookテーブルからbookCodeで現在のrateを取得
		Book book = null;
		Optional<Book> record = bookRepository.findById(bookCode);
		if (record.isEmpty() == false) {
			book = record.get();

			int current_rate = book.getRate();

			//今回のrateと取得した現在のrateを足して2で割る、四捨五入用に+0.5
			int rate = Integer.parseInt(star);
			double caliculate_rate = (current_rate + rate) / 2 + 0.5;

			//doubleからintに
			int new_rate = (int) caliculate_rate;

			//bookテーブルのcomment数を一増やす
			int review = book.getComment();
			review++;

			//更新
			Book update = new Book(book.getCode(), book.getName(), book.getPrice(), new_rate, review);
			//		    Book update = new Book(book.getCode(), book.getName(), book.getPrice(), book.getRate(), review);
			bookRepository.saveAndFlush(update);

		}
		//get all data
		List<Comment> comments = commentRepository.findByBookCode(bookCode);
		mv.addObject("comments", comments);

		mv.addObject("name", book.getName());
		mv.addObject("price", book.getPrice());
		mv.setViewName("comment");
		return security(mv);
	}

	/**
	  *評価の高い順に表示
	 */

	@GetMapping("/book/order/desc")
	public ModelAndView order(ModelAndView mv) {
		//星評価の数で降順に取得
		List<Book> books = bookRepository.findByOrderByRateDesc();

		mv.addObject("books", books);
		mv.setViewName("book");
		return security(mv);
	}

	/**
	  *レビューの多い順に表示
	 */

	@GetMapping("/book/order/review")
	public ModelAndView orderReview(ModelAndView mv) {

		//レビューの数で降順に取得
		List<Book> books = bookRepository.findByOrderByCommentDesc();

		mv.addObject("books", books);
		mv.setViewName("book");
		return security(mv);
	}

}
