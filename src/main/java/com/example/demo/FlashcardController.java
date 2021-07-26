package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FlashcardController extends SecurityController {
	@Autowired
	HttpSession session;

	@Autowired
	FlashcardRepository flashcardRepository;

	@Autowired
	FavoriteRepository favoriteRepository;

	/**
	  *フラッシュカードのホーム画面の表示
	 */

	@RequestMapping("/flashcard")
	public ModelAndView flashcard(ModelAndView mv) {
		mv.setViewName("flashcard");
		return security(mv);
	}

	/**
	  *新規カード作成画面の表示
	 */

	@RequestMapping("/flashcard/new/add")
	public ModelAndView addFlashcard(ModelAndView mv) {
		mv.setViewName("addFlashcard");
		return security(mv);
	}

	/**
	  *新規カードを登録
	 */

	@PostMapping("/flashcard/add")
	public ModelAndView addFlashcard2(
			@RequestParam("share") String share_yn,
			@RequestParam("question") String question,
			@RequestParam("answer") String answer,
			@RequestParam("explanation") String explanation,
			ModelAndView mv) {

		//サニタイジング
		question = Sanitizing.convert(question);
		answer = Sanitizing.convert(answer);
		explanation = Sanitizing.convert(explanation);

		int share = Integer.parseInt(share_yn);

		//未入力の項目がある場合
		if (question.equals("") || answer.equals("") || explanation.equals("") || share_yn.equals("")) {
			mv.addObject("message", "未入力の項目があります。");
			mv.addObject("question", question);
			mv.addObject("answer", answer);
			mv.addObject("explanation", explanation);
			mv.setViewName("addFlashcard");
			return security(mv);

		}

		int userCode = (int) session.getAttribute("userCode");

		//新規カードをflashcardfeテーブルに保存
		Flashcard newflashcard = new Flashcard(userCode, share, question, answer, explanation);
		flashcardRepository.saveAndFlush(newflashcard);

		mv.setViewName("addFlashcard");
		return security(mv);
	}

	/**
	  *学習画面と問題を表示(共有ver)
	 */

	@RequestMapping("/flashcard/use")
	public ModelAndView useFlashcard(ModelAndView mv) {
		session.removeAttribute("flashcard");

		int card_code = 1; //初期化

		if (session.getAttribute("card_code") != null) {//セッションがあれば
			card_code = (int) session.getAttribute("card_code"); //コードを取得して１増やす
			card_code++;
		}

		//１増やしたコードで検索
		Optional<Flashcard> record = flashcardRepository.findById(card_code);
		session.setAttribute("card_code", card_code);

		boolean favorite = false; //お気に入り登録用初期値

		if (record.isEmpty() == false) {
			Flashcard flashcard = record.get();

			if (flashcard.getShare() == 1) {//共有していいものなら
				session.setAttribute("flashcard", flashcard);//セッションに追加
				mv.addObject("question", flashcard.getQuestion());

				//登録表示用
				int userCode = (int) session.getAttribute("userCode");
				int flashcardCode = flashcard.getCode();
				Optional<Favorite> record2 = favoriteRepository.findByUserCodeAndFlashcardCode(userCode, flashcardCode);
				if (record2.isEmpty() == false) {
					favorite = true;
				}
				session.setAttribute("favorite", favorite);
			} else {
				useFlashcard(mv);
			}

		} else {
			if (card_code < 500) {//recordがなければ、もう一度一から実行する
				useFlashcard(mv);

			} else {//500以上になったら、card_codeを再度一に初期化させる
				session.removeAttribute("card_code"); //card_codeセッションは破棄
				useFlashcard(mv);
			}
		}

		mv.setViewName("useFlashcard");
		return security(mv);
	}

	/**
	  *学習画面と問題を表示(共有なしver)
	 */

	@RequestMapping("/flashcard/use/my")
	public ModelAndView useMyFlashcard(ModelAndView mv) {
		session.removeAttribute("flashcard");

		int card_code = 1; //初期化

		if (session.getAttribute("card_code") != null) {//セッションがあれば
			card_code = (int) session.getAttribute("card_code"); //コードを取得して１増やす
			card_code++;
		}

		//１増やしたコードで検索
		Optional<Flashcard> record = flashcardRepository.findById(card_code);
		session.setAttribute("card_code", card_code);

		boolean favorite = false; //お気に入り登録用初期値

		if (record.isEmpty() == false) {
			Flashcard flashcard = record.get();

			int userCode = (int) session.getAttribute("userCode");

			if (flashcard.getUserCode() == userCode) {//ユーザ自身のものなら
				session.setAttribute("flashcard", flashcard);//セッションに追加
				mv.addObject("question", flashcard.getQuestion());
				mv.addObject("flashcard", flashcard);

				//登録表示用
				int flashcardCode = flashcard.getCode();
				Optional<Favorite> record2 = favoriteRepository.findByUserCodeAndFlashcardCode(userCode, flashcardCode);
				if (record2.isEmpty() == false) {
					favorite = true;
				}
				session.setAttribute("favorite", favorite);
			} else {
				useMyFlashcard(mv);
			}

		} else {
			if (card_code < 500) {//recordがなければ、もう一度一から実行する
				useMyFlashcard(mv);

			} else {//500以上になったら、card_codeを再度一に初期化させる
				session.removeAttribute("card_code"); //card_codeセッションは破棄
				useMyFlashcard(mv);
			}
		}

		mv.setViewName("useMyFlashcard");
		return security(mv);
	}

	/**
	  *解答を表示
	 */

	@RequestMapping("/flashcard/answer")
	public ModelAndView showAnswer(ModelAndView mv) {
		Flashcard current_flashcard = (Flashcard) session.getAttribute("flashcard");//現在のフラッシュカード情報をセッションから取得
		String answer = current_flashcard.getAnswer(); //answerのみ取得

		mv.addObject("answer", answer);
		mv.addObject("question", current_flashcard.getQuestion());

		mv.setViewName("useFlashcard");
		return security(mv);
	}

	/**
	  *解説を表示
	 */

	@RequestMapping("/flashcard/explanation")
	public ModelAndView showexplanation(ModelAndView mv) {
		Flashcard current_flashcard = (Flashcard) session.getAttribute("flashcard");//現在のフラッシュカード情報をセッションから取得
		String explanation = current_flashcard.getExplanation(); //answerのみ取得

		mv.addObject("explanation", explanation);
		mv.addObject("answer", current_flashcard.getAnswer());
		mv.addObject("question", current_flashcard.getQuestion());

		mv.setViewName("useFlashcard");
		return security(mv);
	}

	/**
	  *解答を表示
	 */

	@RequestMapping("/flashcard/answer/my")
	public ModelAndView showmyAnswer(ModelAndView mv) {
		Flashcard current_flashcard = (Flashcard) session.getAttribute("flashcard");//現在のフラッシュカード情報をセッションから取得
		String answer = current_flashcard.getAnswer(); //answerのみ取得

		mv.addObject("answer", answer);
		mv.addObject("question", current_flashcard.getQuestion());
		mv.addObject("flashcard", current_flashcard);

		mv.setViewName("useMyFlashcard");
		return security(mv);
	}

	/**
	  *解説を表示
	 */

	@RequestMapping("/flashcard/explanation/my")
	public ModelAndView showmyexplanation(ModelAndView mv) {
		Flashcard current_flashcard = (Flashcard) session.getAttribute("flashcard");//現在のフラッシュカード情報をセッションから取得
		String explanation = current_flashcard.getExplanation(); //answerのみ取得

		mv.addObject("explanation", explanation);
		mv.addObject("answer", current_flashcard.getAnswer());
		mv.addObject("question", current_flashcard.getQuestion());
		mv.addObject("flashcard", current_flashcard);

		mv.setViewName("useMyFlashcard");
		return security(mv);
	}

	/**
	  *お気に入り機能
	 */

	@RequestMapping("/flashcard/favorite")
	public ModelAndView favorite(ModelAndView mv) {
		//userCodeとtestCodeを取得
		int userCode = (int) session.getAttribute("userCode");
		Flashcard current_flashcard = (Flashcard) session.getAttribute("flashcard");
		int flashcardCode = current_flashcard.getCode();

		//もしテーブルに同じ値が保存されていたら、お気に入りを削除
		Optional<Favorite> record = favoriteRepository.findByUserCodeAndFlashcardCode(userCode, flashcardCode);
		if (record.isEmpty() == false) {//もしすでにお気に入り登録していたら
			Favorite favorite = record.get();

			//recordに来た情報を削除
			favoriteRepository.deleteById(favorite.getCode());
			boolean favorited = false;
			session.setAttribute("favorite", favorited);

			//test
			//			System.out.println(session.getAttribute("favorite"));

		} else {//まだお気に入り登録してなかったら

			//favoriteデータベースに保存
			Favorite new_favorite = new Favorite(userCode, flashcardCode);
			favoriteRepository.saveAndFlush(new_favorite);
			boolean favorited = true;
			session.setAttribute("favorite", favorited);

			//test
			//			System.out.println(session.getAttribute("favorite"));

		}
		mv.addObject("question", current_flashcard.getQuestion());
		mv.addObject("answer", current_flashcard.getAnswer());
		mv.addObject("explanation", current_flashcard.getExplanation());

		mv.setViewName("useFlashcard");
		return security(mv);
	}

	/**
	  *お気に入り機能
	 */

	@RequestMapping("/flashcard/favorite/my")
	public ModelAndView myfavorite(ModelAndView mv) {
		//userCodeとtestCodeを取得
		int userCode = (int) session.getAttribute("userCode");
		Flashcard current_flashcard = (Flashcard) session.getAttribute("flashcard");
		int flashcardCode = current_flashcard.getCode();

		//もしテーブルに同じ値が保存されていたら、お気に入りを削除
		Optional<Favorite> record = favoriteRepository.findByUserCodeAndFlashcardCode(userCode, flashcardCode);
		if (record.isEmpty() == false) {//もしすでにお気に入り登録していたら
			Favorite favorite = record.get();

			//recordに来た情報を削除
			favoriteRepository.deleteById(favorite.getCode());
			boolean favorited = false;
			session.setAttribute("favorite", favorited);
		} else {//まだお気に入り登録してなかったら

			//favoriteデータベースに保存
			Favorite new_favorite = new Favorite(userCode, flashcardCode);
			favoriteRepository.saveAndFlush(new_favorite);
			boolean favorited = true;
			session.setAttribute("favorite", favorited);
		}
		mv.addObject("question", current_flashcard.getQuestion());
		mv.addObject("answer", current_flashcard.getAnswer());
		mv.addObject("explanation", current_flashcard.getExplanation());

		mv.setViewName("useMyFlashcard");
		return security(mv);
	}

	/**
	  *お気に入り一覧画面表示
	 */

	@RequestMapping("/flashcard/favorites")
	public ModelAndView favoriteFlashcards(ModelAndView mv) {
		//favoriteテーブルからuserCodeの一致するflashcardCodeを取得する
		int userCode = (int) session.getAttribute("userCode");
		List<Favorite> lists = favoriteRepository.findByUserCode(userCode);

		if (lists.size() == 0) {
			mv.addObject("none", "まだお気に入り登録したカードはありません。");
		} else {

			List<Integer> flashcardCodes = new ArrayList<>();
			Favorite favorite = null;
			int a = 0;
			//flashcardCodeをリストに
			for (int i = 0; i < lists.size(); i++) {
				favorite = lists.get(i);
				a = favorite.getFlashcardCode();
				flashcardCodes.add(a);

			}
			List<Flashcard> flashcards = new ArrayList<>();

			for (int k = 0; k < flashcardCodes.size(); k++) {
				int b = flashcardCodes.get(k);
				Optional<Flashcard> record = flashcardRepository.findById(b);
				Flashcard fav_flashcard = null;
				if (record.isEmpty() == false) {
					fav_flashcard = record.get();
				}
				flashcards.add(fav_flashcard);
			}

			mv.addObject("flashcards", flashcards);
		}

		mv.setViewName("favoriteFlashcard");
		return security(mv);
	}

	/**
	  *お気に入り一覧詳細表示
	 */

	@PostMapping("/flashcard/favorite/detail")
	public ModelAndView favoritesDetail(
			@RequestParam("flashcard_code") int flashcardCode,
			ModelAndView mv) {
		Optional<Flashcard> record = flashcardRepository.findById(flashcardCode);
		Flashcard flashcard = null;
		if (record.isEmpty() == false) {
			flashcard = record.get();
		}

		mv.addObject("question", flashcard.getQuestion());
		mv.addObject("answer", flashcard.getAnswer());
		mv.addObject("explanation", flashcard.getExplanation());

		mv.setViewName("favoriteDetail");
		return security(mv);
	}

	/**
	  *内容変更
	 */

	@RequestMapping("/flashcard/update")
	public ModelAndView updateFlashcard(ModelAndView mv) {
		Flashcard flashcard = (Flashcard) session.getAttribute("flashcard");
		mv.addObject("flashcard", flashcard);

		mv.setViewName("updateFlashcard");
		return security(mv);
	}

	/**
	  *update
	 */

	@PostMapping("/flashcard/update")
	public ModelAndView updateFlashcard2(
			@RequestParam("share") String share_yn,
			@RequestParam("question") String question,
			@RequestParam("answer") String answer,
			@RequestParam("explanation") String explanation,
			ModelAndView mv) {

		//サニタイジング
		question = Sanitizing.convert(question);
		answer = Sanitizing.convert(answer);
		explanation = Sanitizing.convert(explanation);

		int share = Integer.parseInt(share_yn);

		//未入力の項目がある場合
		if (question.equals("") || answer.equals("") || explanation.equals("") || share_yn.equals("")) {
			mv.addObject("message", "未入力の項目があります。");
			mv.addObject("question", question);
			mv.addObject("answer", answer);
			mv.addObject("explanation", explanation);
			mv.setViewName("addFlashcard");
			return security(mv);

		}

		Flashcard flashcard = (Flashcard) session.getAttribute("flashcard");
		int cardCode = flashcard.getCode();
		int userCode = (int) session.getAttribute("userCode");

		//変更内容をflashcardfeテーブルに保存
		Flashcard newflashcard = new Flashcard(cardCode, userCode, share, question, answer, explanation);
		flashcardRepository.saveAndFlush(newflashcard);

		int card_code = (int) session.getAttribute("card_code"); //コードを取得して１減らしとく
		card_code--;
		session.setAttribute("card_code", card_code);
		mv.addObject("complete", "変更が完了しました");

		useMyFlashcard(mv);
//		mv.setViewName("addFlashcard");
		return security(mv);
	}

	/**
	  *削除
	 */

	@RequestMapping("/flashcard/delete")
	@Transactional
	public ModelAndView deleteFlashcard(ModelAndView mv) {
		Flashcard flashcard = (Flashcard) session.getAttribute("flashcard");
		int cardCode = flashcard.getCode();

		favoriteRepository.deleteByFlashcardCode(cardCode);
		flashcardRepository.deleteById(cardCode);

		mv.addObject("complete", "削除が完了しました");

		useMyFlashcard(mv);
		return security(mv);
	}


}
