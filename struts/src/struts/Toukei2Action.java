package struts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class Toukei2Action extends Action {
	public ActionForward execute(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response)
					throws Exception {

		Connection con = null;
		PreparedStatement ps = null;

		// 接続
		con = DBManager.getConnection();
		con.setAutoCommit(false);

		java.util.Date date = new java.util.Date();
		// カレンダークラスのインスタンスを取得
		        Calendar cal = Calendar.getInstance();

		        cal.setTime(date);
				cal.add(Calendar.MONTH, -6);
				//StringからsqlのDate型にしたい
				String str = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				java.sql.Date ds = java.sql.Date.valueOf(str);

				System.out.println(ds);

				HttpSession session = request.getSession(true);
				String birthday = (String)session.getAttribute("birth");

		//入力された誕生日の過去半年の結果
		//resultテーブルのbirthdayと入力値が等しければresultテーブルからomikuji_idを取る
		String sqlall ="SELECT r.fortune_day,tbl.unsei,tbl.negaigoto,tbl.akinai,tbl.gakumon "
				+ "FROM(omikuji o LEFT OUTER JOIN unsei_master u "
				+ "ON o.unsei_id = u.unsei_id) "
				+ "AS tbl INNER JOIN result r "
				+ "ON tbl.omikuji_id = r.omikuji_id "
				+ "AND r.birthday = ? "
				+ "AND fortune_day >= ? "
				+ "AND fortune_day <= current_date "
				+ "ORDER BY r.fortune_day";

		//birthdayが怪しい！！！
		ps = con.prepareStatement(sqlall);
		ps.setString(1,birthday);
		ps.setDate(2,ds);
		ResultSet result = ps.executeQuery();

		//上のsql文の条件にあうomikuji_idを見つけてね
List<Omikuji> birthKako = new ArrayList<Omikuji>();
		while (result.next()) {
			Omikuji omikuji = new Omikuji();
			omikuji.setToday(result.getString(1));
			omikuji.setUnsei(result.getString(2));
			omikuji.setNegaigoto(result.getString(3));
			omikuji.setAkinai(result.getString(4));
			omikuji.setGakumon(result.getString(5));
			birthKako.add(omikuji);
		}
		request.setAttribute("omi", birthKako);
//
//		//見つけたomikuji_idを使っておみくじ結果をだす
//		//運勢、願い事、商い、学問を取り出すsql文
//		String sql ="SELECT u.unsei,o.omikuji_id,o.negaigoto,o.akinai,o.gakumon "
//				+ "FROM omikuji o, unsei_master u "
//				+ "WHERE o.unsei_id = u.unsei_id and o.omikuji_id =?";
//		ps = con.prepareStatement(sql);
//		ps.setString(1,Integer.toString(idx));	//おみくじコード
//
//		//結果をresultに入れる
//		result = ps.executeQuery();
//		//結合されたテーブルを上から順番に確認していく（見つかるかな〜）
//		while (result.next()) {
//			Omikuji omikuji = new Omikuji();
//			omikuji.setUnsei(result.getString("unsei"));
//			//omikuji.setOmikujiId(result.getString("omikuji_id"));
//			omikuji.setNegaigoto(result.getString("negaigoto"));
//			omikuji.setAkinai(result.getString("akinai"));
//			omikuji.setGakumon(result.getString("gakumon"));
//
//			request.setAttribute("omikuji", omikuji);
//			//ActionForward を返却
//		}
		return mapping.findForward("toukei2");
	}
}
