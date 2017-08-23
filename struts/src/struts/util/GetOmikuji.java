package struts.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import struts.dto.Omikuji;

public class GetOmikuji {

	ReadFile read = new ReadFile();
	List<Omikuji> al = read.readFile();

	public String date(String birthday) throws ParseException {

		// yyyyMMddの形に変換
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setLenient(false); // ←これで厳密にチェックしてくれるようになる
		String s1 = birthday;
		df.format(df.parse(s1)); // ←df.parseでParseExceptionがThrowされる

		// 占った日付を生成（DBに登録する方）
		Date day = new Date();
		// yyyy-MM-ddの形に変換
		String today = df.format(day);

		return today;
	}

	public int getIdx(int i) {
		Random r = new Random();
		int idx = r.nextInt(i);
		return idx;
	}
}
