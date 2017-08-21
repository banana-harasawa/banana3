package struts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class FortuneAction extends Action {

	public ActionForward execute(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse responsez)
					throws Exception {
		Connection connection = null;

		try{

			// ActionForm を LoginForm にキャス卜
			FortuneForm fortuneForm = (FortuneForm) form;
			//フォームに入力された ID を取得する
			String birthday = fortuneForm.getBirthday();

			// 接続
			connection = DBManager.getConnection();
			connection.setAutoCommit(false);

			HttpSession session = request.getSession(true);
			session.setAttribute("birth",birthday);

			String test = (String)session.getAttribute("birth");
			System.out.println(test);
			//DBに同じ誕生日がないか確認
			int i = OmikujiCommit(connection);

			//日付取得

			GetOmikuji omikuji = new GetOmikuji();
			Boolean flg = true;
			int idx = omikuji.getIdx(i);
			request.setAttribute("omikujiId",idx);
			//もし結果テーブルと同じ誕生日ならばflgをfalseにする
			flg = changeFlg(connection,birthday,idx,flg);

			//DBに登録
			Omikuji obj = resultOmikuji(connection,idx);
			resultCommit(flg,idx,connection,birthday,obj);

			//messageに入れる
			Omikuji message = obj ;
			// message を request スコープに登録する
			request.setAttribute("message", message);

			//接続を切断する
			if (connection != null) {
				connection.close();
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		//ActionForward を返却
		return mapping.findForward("result");
	}



	// もし結果テーブルに入力された誕生日があったら、その結果を出力するメソッド
	private static int sarchOmikuji(Connection con, String strDate) {
		// おみくじidを？に入れておみくじの結果をだす
		// resultテーブルの全てのbirthdayを出す構文
		final String sql = "SELECT omikuji_id FROM result WHERE fortune_day = current_date and birthday = ?";

		int idx = 0;
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, strDate);
			ResultSet result = ps.executeQuery();
			// 入力値をbirthdayとする

			while (result.next()) {
				idx = result.getInt(1);
			}


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return idx;

	}
	//テーブルの中身をカウントで数えて比較する
		private static int checkTable(Connection con) {
			// //おみくじテーブルの中身がnullなら
			final String sql = "SELECT count(omikuji_id) FROM omikuji";

			PreparedStatement stmt;
			int count = 0;
			try {
				stmt = con.prepareStatement(sql);
				ResultSet result = stmt.executeQuery();
				while (result.next()) {
					count = result.getInt(1);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return count;
		}

//結果を出力するメソッド
	private static Omikuji resultOmikuji(Connection con,int idx) {
		//おみくじidを？に入れておみくじの結果をだす
		final String sql = "SELECT u.unsei,o.omikuji_id,o.negaigoto,o.akinai,o.gakumon "
				+ "FROM omikuji o, unsei_master u "
				+ "where o.unsei_id = u.unsei_id and o.omikuji_id =?";

		Omikuji omikuji=null;
		PreparedStatement ps;
		try {
			//?に値を埋め込む
			//実行するSQL文とパラメータを指定する
			ps = con.prepareStatement(sql);
			ps.setString(1,Integer.toString(idx));	//おみくじコード

			//結果をresultに入れる
			ResultSet result = ps.executeQuery();

			//
			while (result.next()) {
				omikuji = new Omikuji();
				omikuji.setUnsei(result.getString("unsei"));
				//omikuji.setOmikujiId(result.getString("omikuji_id"));
				omikuji.setNegaigoto(result.getString("negaigoto"));
				omikuji.setAkinai(result.getString("akinai"));
				omikuji.setGakumon(result.getString("gakumon"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return omikuji;
	}

	//もし結果テーブルと同じ誕生日ならばflgをfalseにする
	public static boolean changeFlg(Connection connection,String strDate,int idx,boolean flg){
		int res = sarchOmikuji(connection,strDate);
		if(res != 0){
			idx = res;
			flg=false;
		}
		return flg;
	}

	//resultテーブルにおみくじが登録されていなければ、おみくじを登録
	public static int OmikujiCommit(Connection connection) throws SQLException{
		WriterSql writer = new WriterSql();
		int i=checkTable(connection);
		if(i == 0){
			writer.writerOmikujiTbl(connection);
		}
		return i;
	}

	//resultテーブルにおみくじ結果を保存
	public static void resultCommit(boolean flg,int idx,Connection connection,String strDate,Omikuji obj) throws SQLException{
		WriterSql writer = new WriterSql();
		if (flg) {
			obj.setOmikujiId(idx);
			obj.setBirthday(strDate);
			writer.writerResultTbl(obj,connection);
		}
	}
}