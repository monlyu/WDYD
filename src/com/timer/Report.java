package com.timer;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Report extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String ym = req.getParameter("month");

		DBs db = DBs.instance();
		try {
			List<TimeItem> items = db.queryMonth(ym);
			Map<String, Boolean> allKeys = new HashMap<String, Boolean>();
			Collections.reverse(items); // 倒序排序
			SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String bigMonth = null;

			StringBuilder body = new StringBuilder("");
			BigDecimal total = BigDecimal.ZERO;

			for (TimeItem timeItem : items) {
				StringBuilder tr = new StringBuilder("<tr>");
				String start = timeItem.getStart();
				String date = start.substring(0, 10);
				if (!allKeys.containsKey(date)) {
					SimpleDateFormat outp = new SimpleDateFormat("MM/dd/yyyy");
					allKeys.put(date, true);
					Date dateOn = sm.parse(start);
					tr.append("<td>" + outp.format(dateOn) + "</td>");
					bigMonth = new SimpleDateFormat("MMM yyyy").format(dateOn);

				} else {
					tr.append("<td/>");
				}
				tr.append("<td>" + timeItem.getProject() + "</td>");
				tr.append("<td>" + timeItem.getHour() + "</td>");
				tr.append("<td>" + timeItem.getTask() + "</td>");

				tr.append("</tr>");
				body.append(tr);

				total = total.add(new BigDecimal(timeItem.getHour()));
			}

			req.setAttribute("WORK", body);
			req.setAttribute("MONTH", bigMonth);
			req.setAttribute("TOTAL", total.doubleValue());

			req.getRequestDispatcher("report.jsp").forward(req, response);

		} catch (Exception e) {

		} finally {
			db.close();
		}

	}
}
