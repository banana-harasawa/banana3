package struts;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ToukeiAction extends Action{

	public ActionForward execute(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException{

		Connection con = null;
		PreparedStatement ps = null;

		// 接続
		con = DBManager.getConnection();
		con.setAutoCommit(false);

		java.util.Date date = new java.util.Date();
		// カレンダークラスのインスタンスを取得
		        Calendar cal = Calendar.getInstance();
		        //セッションが別のクラスでも使えるかの確認
//		        	HttpSession session = request.getSession(true);
//		        	String test = (String )session.getAttribute("birth");
//		        	System.out.println(test);

		cal.setTime(date);
		cal.add(Calendar.MONTH, -6);
		//StringからsqlのDate型にしたい
		String str = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		java.sql.Date ds = java.sql.Date.valueOf(str);

		System.out.println(ds);
		//resultテーブルの中の各運勢の割合を出したい
		//何人が占って、大吉が何人、小吉が何人、、、
		//過去半年のomikuji_id（占った人）の数
		String sqlall ="SELECT "
				+ "COUNT(omikuji_id) "
				+ "FROM result "
				+ "WHERE fortune_day >= ? "
				+ "AND fortune_day < current_date;";

		ps = con.prepareStatement(sqlall);
		ps.setDate(1, ds);
		ResultSet result = ps.executeQuery();
		int uranaiResultCount = 0;

		while (result.next()) {
			uranaiResultCount = result.getInt(1);

		}
		request.setAttribute("resultCount",uranaiResultCount);

		//それぞれの運勢が何件なのか数値を取得する
		String sql = "SELECT u.unsei_id ,u.unsei,COUNT(tbl.unsei_id) "
				+ "FROM unsei_master u "
				+ "LEFT OUTER JOIN ("
				+ "omikuji o INNER JOIN result r "
				+ "ON o.omikuji_id = r.omikuji_id) AS tbl "
				+ "ON u.unsei_id = tbl.unsei_id "
				+ "WHERE fortune_day >= ? "
				+ "AND fortune_day < current_date "
				+ "GROUP BY u.unsei_id,u.unsei "
				+ "ORDER BY u.unsei_id";

		//過去半年の割合
		List<UnseiBean> wariai = new ArrayList<UnseiBean>();

			ps = con.prepareStatement(sql);
			//？に入る部分の数を毎回変えたい
			ps.setDate(1, ds);
			result = ps.executeQuery();
			//それそれのomikuji_idを数えて欲しい
			float idx=0;
			float count = 0;
			while (result.next()) {
				UnseiBean unsei = new UnseiBean();
				unsei.setUnsei(result.getString(2));
				idx = (float)result.getInt(3);
				count = (idx/uranaiResultCount)*100;
				BigDecimal bd = new BigDecimal(count);

				unsei.setUnseiId(bd.setScale(0, BigDecimal.ROUND_HALF_UP));
				wariai.add(unsei);
			}
			request.setAttribute("wariai",wariai);





			//今日の割合
			//(今日でた運勢/今日の占った人数)*100=今日の割合
			//uranaiResultCount=全ての占い結果の回数
			//今日でた運勢の番号
			//今日占った人数　
			String ninnzuuSql = "SELECT count(omikuji_id) FROM result WHERE fortune_day = current_date";
			List<UnseiBean> todayRatio = new ArrayList<UnseiBean>();

			float todayAllCount = 0;
			ps = con.prepareStatement(ninnzuuSql);
			result = ps.executeQuery();
			count = 0;
			while (result.next()) {
				todayAllCount = result.getInt(1);
			}

			//今日引かれたおみくじを出したい
			String todayUranaiResult="SELECT u.unsei,COUNT(tbl.unsei_id) "
					+ "FROM unsei_master u "
					+ "LEFT OUTER JOIN ( omikuji o INNER JOIN result r "
					+ "ON o.omikuji_id = r.omikuji_id "
					+ "AND r.fortune_day = current_date) AS tbl "
					+ "ON u.unsei_id = tbl.unsei_id "
					+ "GROUP BY u.unsei";


			//omiはその日引いた運勢のomikuji_id
			//int omi = (int)request.getAttribute("omikujiId");
			ps = con.prepareStatement(todayUranaiResult);
			//？に入る部分の数を毎回変えたい
			//ps.setInt(1, omi);
			result = ps.executeQuery();
			//それそれのomikuji_idを数えて欲しい
			count = 0;
			while (result.next()) {
				UnseiBean unsei = new UnseiBean();
				unsei.setUnsei(result.getString(1));
				idx = (float)result.getInt(2);
				count = (idx/todayAllCount)*100;
				BigDecimal bd = new BigDecimal(count);
				unsei.setUnseiId(bd.setScale(0, BigDecimal.ROUND_HALF_UP));
				todayRatio.add(unsei);
			}
			request.setAttribute("todayRatio",todayRatio);

		return mapping.findForward("toukei1");

	}
}
