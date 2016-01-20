package com.timer;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Action extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		String method = req.getParameter("method");
		if ("projects".equalsIgnoreCase(method)) {
			resp.getWriter().print(allprojects());
		}
		if ("worktime".equalsIgnoreCase(method)) {
			resp.setContentType("application/json");
			String ym = req.getParameter("ym");
			resp.getWriter().print(allTime(ym));
		}
		if ("delete".equalsIgnoreCase(method)) {
			DBs sb = DBs.instance();
			try {
				String uuid = req.getParameter("uuid");
				sb.delete(uuid);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sb.close();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		String uuid = req.getParameter("uuid");
		TimeItem itme = new TimeItem();
		if (uuid != null && !"".equalsIgnoreCase(uuid)) {
			itme.setUuid(Integer.parseInt(uuid));
		}
		itme.setStart(req.getParameter("start"));
		itme.setEnd(req.getParameter("end"));
		itme.setProject(req.getParameter("project"));
		itme.setTask(req.getParameter("task"));
		itme.setHour(req.getParameter("hour"));
		DBs sb = DBs.instance();
		try {
			sb.inserOrUpdate(itme);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sb.close();
		}
		resp.getWriter().print("ok");
	}

	private StringBuilder allprojects() {
		String[] projects = DBs.allconfig().get("projects").split(",");
		StringBuilder bu = new StringBuilder();
		for (String string : projects) {
			bu.append(String.format("<option value=\"%s\">%s</option>", string, string));
		}
		return bu;
	}

	private StringBuilder allTime(String ym) {
		try {
			DBs sb = DBs.instance();
			List<TimeItem> times = sb.queryMonth(ym);
			sb.close();
			StringBuilder bu = new StringBuilder("[");
			for (TimeItem timeItem : times) {
				if (bu.length() != 1)
					bu.append(",");
				bu.append(String.format("{\"uuid\":%s,\"start\":\"%s\",\"end\":\"%s\",\"project\":\"%s\",\"task\":\"%s\",\"hour\":\"%s\"}",
						timeItem.uuid, timeItem.start, timeItem.end, timeItem.project, timeItem.task, timeItem.hour));
			}
			return bu.append("]");
		} catch (Exception e) {
			throw new RuntimeException("查询失败");
		}
	}

}
