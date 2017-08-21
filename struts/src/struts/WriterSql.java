package struts;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class WriterSql {

	ReadFile read = new ReadFile();
	Connection connection = null;
	PreparedStatement ps = null;
	//ファイルの読み込み（既におみくじリストに詰められた状態）
	List<Omikuji> al = read.readFile();

	public void writerOmikujiTbl(Connection connection) throws SQLException{
		//SQL文を定義する
		String omikujiSql = "INSERT INTO omikuji(omikuji_id,unsei_id,negaigoto,akinai,gakumon,author,created_date,updater,date_renovation)"
				+ " values(?, ?, ?, ?, ?,'原澤香織', current_date, '原澤香織', current_date)";//9  

		//omikujiテーブルに書き込む
		for(int i =0; i<al.size(); i++){

			GetUnseiId omi = new GetUnseiId();
			int unseiTBL = omi.unseiId(al.get(i).getUnsei());

			//実行するSQL文とパラメータを指定する
			ps = connection.prepareStatement(omikujiSql);
			ps.setInt(1,i);	//おみくじコード
			ps.setInt(2, unseiTBL);	//運勢コード
			ps.setString(3, al.get(i).getNegaigoto());//願い事
			ps.setString(4, al.get(i).getAkinai());	//商い
			ps.setString(5, al.get(i).getGakumon());	//学問
			//作成者
			//作成日
			//登録者
			//登録日


			//INSERT文を実行する
			ps.executeUpdate();

			//コミット
			connection.commit();
		}
	
	}
	
	public void writerResultTbl(Omikuji omikuji,Connection connection) throws SQLException{
	//SQL文を定義する
	String resultSql = "INSERT INTO result(omikuji_id,birthday,fortune_day,author,created_date,updater,date_renovation)"
			+ " values(?, ?,current_date,'原澤香織', current_date, '原澤香織', current_date)";//7  

	//DBに書き込む
	
		//実行するSQL文とパラメータを指定する
		ps = connection.prepareStatement(resultSql);
		ps.setInt(1,omikuji.getOmikujiId());	//おみくじコード
		ps.setString(2, omikuji.getBirthday());	//誕生日
		//占った日
		//作成者
		//作成日
		//登録者
		//登録日


		//INSERT文を実行する
		ps.executeUpdate();

		//コミット
		connection.commit();
	}
}
