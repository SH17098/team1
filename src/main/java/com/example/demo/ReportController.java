package com.example.demo;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportController extends SecurityController{
	@Autowired
	HttpSession session;

	@Autowired
	ReportRepository reportRepository;

	//一覧表示
	@RequestMapping("/report")
	public ModelAndView reports(ModelAndView mv) {
		int userCode = (int) session.getAttribute("userCode");

		List<Report> reports = reportRepository.findByUserCode(userCode);
		if(reports.size() == 0) {
			mv.addObject("none", "まだアクションがありません。");
		}else {
		    mv.addObject("reports", reports);
		}

		mv.setViewName("report");
		return security(mv);
	}

	@PostMapping("/report")
	public ModelAndView report(
			@RequestParam("type") String type,
			@RequestParam("report") String report,
			ModelAndView mv) {
        int userCode = (int)session.getAttribute("userCode");
        String answer = "まだ返答はありません。";

        //追加
        Report new_report = new Report(userCode, type, report, answer);
        reportRepository.saveAndFlush(new_report);

		return reports(mv);
	}


}
