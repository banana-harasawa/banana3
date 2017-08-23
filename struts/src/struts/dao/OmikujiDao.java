package struts.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import struts.dto.GetUnseiId;
import struts.dto.Omikuji;
import struts.dto.UnseiBean;
import struts.util.DBManager;
import struts.util.ReadFile;

public class OmikujiDao {
	ReadFile read = new ReadFile();
	// ファイルの読み込み（既におみくじリストに詰められた状態）
	List<Omikuji> al = read.readFile();

	private static final String OMIKUJI_SQL = "INSERT INTO omikuji(omikuji_id,unsei_id,negaigoto,akinai,gakumon,author,created_date,updater,date_renovation) values(?, ?, ?, ?, ?,'原澤香織', current_date, '原澤香織', current_date)";
	private static final String INSERT_RESULT = "INSERT INTO result(omikuji_id,birthday,fortune_day,author,created_date,updater,date_renovation) values(?, ?,current_date,'原澤香織', current_date, '原澤香織', current_date)";
	private static final String SQL_ALL = "SELECT r.fortune_day,tbl.unsei,tbl.negaigoto,tbl.akinai,tbl.gakumon FROM(omikuji o LEFT OUTER JOIN unsei_master u ON o.unsei_id = u.unsei_id) AS tbl INNER JOIN result r ON tbl.omikuji_id = r.omikuji_id AND r.birthday = ? AND fortune_day >= ? AND fortune_day <= current_date ORDER BY r.fortune_day";
	private static final String TODAY_SAME_BIRTHDAY = "SELECT omikuji_id FROM result WHERE fortune_day = current_date and birthday = ?";
	private static final String COUNT_OMIKUJI = "SELECT count(omikuji_id) FROM omikuji";
	private static final String SELECT_RESULT = "SELECT u.unsei,o.omikuji_id,o.negaigoto,o.akinai,o.gakumon FROM omikuji o, unsei_master u where o.unsei_id = u.unsei_id and o.omikuji_id =?";
	private static final String SELECT_FORTUNE_HALFYEAR_ALLCOUNT = "SELECT COUNT(omikuji_id) FROM result WHERE fortune_day >= ? AND fortune_day <= current_date";
	private static final String SELECT_FORTUNE_HALFYEAR = "SELECT u.unsei_id ,u.unsei,COUNT(tbl.unsei_id) FROM unsei_master u LEFT OUTER JOIN (omikuji o INNER JOIN result r ON o.omikuji_id = r.omikuji_id) AS tbl ON u.unsei_id = tbl.unsei_id WHERE fortune_day >= ? AND fortune_day <= current_date GROUP BY u.unsei_id,u.unsei ORDER BY u.unsei_id";
	private static final String SELECT_SAME_BIRTHDAY_RESULT = "SELECT tbl.unsei,tbl.negaigoto,tbl.akinai,tbl.gakumon FROM(omikuji o LEFT OUTER JOIN unsei_master u ON o.unsei_id = u.unsei_id) AS tbl INNER JOIN result r ON tbl.omikuji_id = r.omikuji_id AND r.birthday = ? AND fortune_day = current_date";
	private static final String TODAY_FORUTUNE_COUNT = "SELECT count(omikuji_id) FROM result WHERE fortune_day = current_date";
	private static final String TODYA_URANAI_RESULT = "SELECT u.unsei,COUNT(tbl.unsei_id) FROM unsei_master u LEFT OUTER JOIN ( omikuji o INNER JOIN result r ON o.omikuji_id = r.omikuji_id AND r.fortune_day = current_date) AS tbl ON u.unsei_id = tbl.unsei_id GROUP BY u.unsei_id ORDER BY u.unsei_id";

	public void writerOmikujiTbl() throws SQLException {
		// 接続
		Connection con = DBManager.getConnection();
		con.setAutoCommit(false);
		PreparedStatement ps = null;
		// SQL文を定義する

		// omikujiテーブルに書き込む
		for (int i = 0; i < al.size(); i++) {

			GetUnseiId omi = new GetUnseiId();
			int unseiTBL = omi.unseiId(al.get(i).getUnsei());

			// 実行するSQL文とパラメータを指定する
			ps = con.prepareStatement(OMIKUJI_SQL);
			ps.setInt(1, i); // おみくじコード
			ps.setInt(2, unseiTBL); // 運勢コード
			ps.setString(3, al.get(i).getNegaigoto());// 願い事
			ps.setString(4, al.get(i).getAkinai()); // 商い
			ps.setString(5, al.get(i).getGakumon()); // 学問

			// INSERT文を実行する
			ps.executeUpdate();

			// コミット
			con.commit();
		}
		con.close();
	}

	public void writerResultTbl(Omikuji omikuji) throws SQLException {
		Connection con = DBManager.getConnection();
		PreparedStatement ps = null;
		// 実行するSQL文とパラメータを指定する
		ps = con.prepareStatement(INSERT_RESULT);
		ps.setInt(1, omikuji.getOmikujiId()); // おみくじコード
		ps.setString(2, omikuji.getBirthday()); // 誕生日
		// INSERT文を実行する
		ps.executeUpdate();

		con.close();
	}

	public static List<Omikuji> findKakoResult(String birthday, Date ds) throws SQLException {
		Connection con = DBManager.getConnection();
		PreparedStatement ps = null;

		// resultテーブルのbirthdayと入力値が等しければresultテーブルからomikuji_idを取る
		// birthdayが怪しい！！！
		ps = con.prepareStatement(SQL_ALL);
		ps.setString(1, birthday);
		ps.setDate(2, ds);
		ResultSet result = ps.executeQuery();

		// 上のsql文の条件にあうomikuji_idを見つけてね
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
		con.close();
		return birthKako;
	}

	// もし結果テーブルに入力された誕生日があったら、その結果を出力するメソッド
	public static int sarchOmikuji(String birthday) throws SQLException {
		Connection con = DBManager.getConnection();
		PreparedStatement ps = null;
		// おみくじidを？に入れておみくじの結果をだす
		// resultテーブルの全てのbirthdayを出す構文
		int idx = 0;
		try {
			// omikuji_idが返ってくる
			ps = con.prepareStatement(TODAY_SAME_BIRTHDAY);
			ps.setString(1, birthday);
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
		con.close();
		return idx;

	}

	// テーブルの中身をカウントで数えて比較する
	public static int checkTable() throws SQLException {
		Connection con = DBManager.getConnection();
		PreparedStatement ps = null;
		// おみくじテーブルの中身がnullなら
		int count = 0;
		try {
			ps = con.prepareStatement(COUNT_OMIKUJI);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				count = result.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.close();
		return count;
	}

	// 結果を出力するメソッド
	public static Omikuji resultOmikuji(int idx) throws SQLException {
		Connection con = DBManager.getConnection();
		PreparedStatement ps = null;
		// おみくじidを？に入れておみくじの結果をだす

		Omikuji omikuji = null;
		try {

			// 実行するSQL文とパラメータを指定する
			ps = con.prepareStatement(SELECT_RESULT);
			// ?に値を埋め込む
			ps.setString(1, Integer.toString(idx)); // おみくじコード
			// SQL文の実行
			ResultSet result = ps.executeQuery();

			while (result.next()) {
				omikuji = new Omikuji();
				omikuji.setUnsei(result.getString("unsei"));
				// omikuji.setOmikujiId(result.getString("omikuji_id"));
				omikuji.setNegaigoto(result.getString("negaigoto"));
				omikuji.setAkinai(result.getString("akinai"));
				omikuji.setGakumon(result.getString("gakumon"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.close();
		return omikuji;
	}

	public static Omikuji findTodayResult(String birthday) throws SQLException {
		Connection con = DBManager.getConnection();
		PreparedStatement ps = null;
		Omikuji omikuji = null;
		try {

			// 実行するSQL文とパラメータを指定する
			ps = con.prepareStatement(SELECT_SAME_BIRTHDAY_RESULT);
			// ?に値を埋め込む
			ps.setString(1, birthday); // おみくじコード
			// SQL文の実行
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				omikuji = new Omikuji();
				omikuji.setUnsei(result.getString("unsei"));
				omikuji.setNegaigoto(result.getString("negaigoto"));
				omikuji.setAkinai(result.getString("akinai"));
				omikuji.setGakumon(result.getString("gakumon"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.close();
		return omikuji;
	}

	public static int unseiRate(java.sql.Date ds) throws SQLException {
		Connection con = DBManager.getConnection();
		PreparedStatement ps = null;

		ps = con.prepareStatement(SELECT_FORTUNE_HALFYEAR_ALLCOUNT);
		ps.setDate(1, ds);
		ResultSet result = ps.executeQuery();
		int uranaiResultCount = 0;

		while (result.next()) {
			uranaiResultCount = result.getInt(1);

		}
		con.close();
		return uranaiResultCount;

	}

	public static List<UnseiBean> KaoRate(Date ds, int uranaiResultCount) throws SQLException {
		Connection con = DBManager.getConnection();
		PreparedStatement ps = null;

		// 過去半年の割合
		List<UnseiBean> wariai = new ArrayList<UnseiBean>();

		ps = con.prepareStatement(SELECT_FORTUNE_HALFYEAR);
		// ？に入る部分の数を毎回変えたい
		ps.setDate(1, ds);
		ResultSet result = ps.executeQuery();
		// それそれのomikuji_idを数えて欲しい
		float idx = 0;
		float count = 0;
		while (result.next()) {
			UnseiBean unsei = new UnseiBean();
			unsei.setUnsei(result.getString(2));
			idx = (float) result.getInt(3);
			count = (idx / uranaiResultCount) * 100;
			BigDecimal bd = new BigDecimal(count);

			unsei.setUnseiId(bd.setScale(0, BigDecimal.ROUND_HALF_UP));
			wariai.add(unsei);
		}
		con.close();
		return wariai;
	}

	public static float TodayAllCount() throws SQLException {
		Connection con = DBManager.getConnection();
		PreparedStatement ps = null;

		float todayAllCount = 0;
		ps = con.prepareStatement(TODAY_FORUTUNE_COUNT);
		ResultSet result = ps.executeQuery();
		while (result.next()) {
			todayAllCount = result.getInt(1);
		}
		con.close();
		return todayAllCount;
	}

	public static List<UnseiBean> TodayOmikuji(float todayAllCount) throws SQLException {
		Connection con = DBManager.getConnection();
		PreparedStatement ps = null;
		List<UnseiBean> todayRatio = new ArrayList<UnseiBean>();
		// 今日引かれたおみくじを出したい
		// omiはその日引いた運勢のomikuji_id
		// int omi = (int)request.getAttribute("omikujiId");
		ps = con.prepareStatement(TODYA_URANAI_RESULT);
		// ？に入る部分の数を毎回変えたい
		// ps.setInt(1, omi);
		ResultSet result = ps.executeQuery();
		// それそれのomikuji_idを数えて欲しい
		float count = 0;
		while (result.next()) {
			UnseiBean unsei = new UnseiBean();
			unsei.setUnsei(result.getString(1));
			float idx = (float) result.getInt(2);
			count = (idx / todayAllCount) * 100;
			BigDecimal bd = new BigDecimal(count);
			unsei.setUnseiId(bd.setScale(0, BigDecimal.ROUND_HALF_UP));
			todayRatio.add(unsei);
		}
		con.close();
		return todayRatio;
	}

}
