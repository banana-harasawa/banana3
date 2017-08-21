package struts;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GetOmikuji {

	ReadFile read = new ReadFile();
	List<Omikuji> al = read.readFile();

	public String date(String strDate) throws ParseException{

		// yyyyMMddの形に変換
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		df.setLenient(false); // ←これで厳密にチェックしてくれるようになる
		String s1 = strDate;
		df.format(df.parse(s1)); // ←df.parseでParseExceptionがThrowされる

		// 占った日付を生成（DBに登録する方）
		Date day = new Date();
		// yyyyMMddの形に変換
		String today = df.format(day);

		return today;
	}

	public int getIdx(int i){
		Random r = new Random();
		int idx = r.nextInt(i);
		return idx;
	}
}
