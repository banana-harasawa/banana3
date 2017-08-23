package struts.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.dao.OmikujiDao;
import struts.dto.UnseiBean;

public class ToukeiAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws SQLException, ParseException {

		java.util.Date date = new java.util.Date();
		// カレンダークラスのインスタンスを取得
		Calendar cal = Calendar.getInstance();

		cal.setTime(date);
		cal.add(Calendar.MONTH, -6);
		// StringからsqlのDate型にしたい
		String str = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		java.sql.Date ds = java.sql.Date.valueOf(str);

		//半年前の日付をコンソールに出力
		System.out.println(ds);
		// resultテーブルの中の各運勢の割合を出したい
		// 何人が占って、大吉が何人、小吉が何人、、、
		// 過去半年のomikuji_id（占った人）の数
		int uranaiResultCount = OmikujiDao.unseiRate(ds);
		request.setAttribute("resultCount", uranaiResultCount);

		// それぞれの運勢が何件なのか数値を取得する
		List<UnseiBean> wariai = OmikujiDao.KaoRate(ds, uranaiResultCount);
		request.setAttribute("wariai", wariai);

		// 今日の割合
		// (今日でた運勢/今日の占った人数)*100=今日の割合
		// uranaiResultCount=全ての占い結果の回数
		// 今日でた運勢の番号
		// 今日占った人数
		float todayAllCount = OmikujiDao.TodayAllCount();
		request.setAttribute("todayRatio", OmikujiDao.TodayOmikuji(todayAllCount));

		return mapping.findForward("toukei1");

	}
}
