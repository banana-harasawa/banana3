package struts.action;

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

import struts.dao.OmikujiDao;
import struts.dto.Omikuji;

public class Toukei2Action extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		java.util.Date date = new java.util.Date();
		// カレンダークラスのインスタンスを取得
		Calendar cal = Calendar.getInstance();

		cal.setTime(date);
		cal.add(Calendar.MONTH, -6);
		// StringからsqlのDate型にしたい
		String str = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		java.sql.Date ds = java.sql.Date.valueOf(str);

		System.out.println(ds);

		HttpSession session = request.getSession(true);
		String birthday = (String) session.getAttribute("birth");

		// 入力された誕生日の過去半年の結果

		List<Omikuji> birthKako = new ArrayList<Omikuji>();
		birthKako = OmikujiDao.findKakoResult(birthday, ds);
		request.setAttribute("omi", birthKako);
		//
		// //見つけたomikuji_idを使っておみくじ結果をだす
		// //運勢、願い事、商い、学問を取り出すsql文
		// String sql ="SELECT
		// u.unsei,o.omikuji_id,o.negaigoto,o.akinai,o.gakumon "
		// + "FROM omikuji o, unsei_master u "
		// + "WHERE o.unsei_id = u.unsei_id and o.omikuji_id =?";
		// ps = con.prepareStatement(sql);
		// ps.setString(1,Integer.toString(idx)); //おみくじコード
		//
		// //結果をresultに入れる
		// result = ps.executeQuery();
		// //結合されたテーブルを上から順番に確認していく（見つかるかな〜）
		// while (result.next()) {
		// Omikuji omikuji = new Omikuji();
		// omikuji.setUnsei(result.getString("unsei"));
		// //omikuji.setOmikujiId(result.getString("omikuji_id"));
		// omikuji.setNegaigoto(result.getString("negaigoto"));
		// omikuji.setAkinai(result.getString("akinai"));
		// omikuji.setGakumon(result.getString("gakumon"));
		//
		// request.setAttribute("omikuji", omikuji);
		// //ActionForward を返却
		// }
		return mapping.findForward("toukei2");
	}
}
