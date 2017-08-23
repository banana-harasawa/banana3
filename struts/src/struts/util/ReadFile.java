package struts.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import struts.dto.DayResult;
import struts.dto.Omikuji;

public class ReadFile {
	public List<Omikuji> readFile() {
		// Listを生成
		List<Omikuji> al = new ArrayList<Omikuji>();
		String line;
		try {
			// ファイルを読み込む
			File f = new File("/Users/k_harasawa/Documents/uranai.csv");
			BufferedReader br = new BufferedReader(new FileReader(f));

			// 1行ずつCSVファイルを読み込む
			// 読み込んだデータがnullでなければ
			while ((line = br.readLine()) != null) {
				// 行をカンマ区切りで配列に変換
				String[] data = line.split(",", 0);

				Omikuji omikuji = new Omikuji();
				omikuji.setUnsei(data[0]);
				omikuji.setNegaigoto(data[1]);
				omikuji.setAkinai(data[2]);
				omikuji.setGakumon(data[3]);
				al.add(omikuji);

			}
			br.close();

		} catch (IOException e) {
			System.out.println(e);
		}
		return al;
	}

	// Writerクラスで書き込まれたものを読み込む
	public List<DayResult> readWriterFile() {
		// Listを生成
		List<DayResult> ystrday = new ArrayList<DayResult>();
		String line;

		try {
			// ファイルを読み込む
			File f = new File("/Users/k_harasawa/Documents/Writer.csv");
			BufferedReader br = new BufferedReader(new FileReader(f));

			// 1行ずつCSVファイルを読み込む
			// 読み込んだデータがnullでなければ
			while ((line = br.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}
				// 行をカンマ区切りで配列に変換
				String[] data = line.split(",", 0);

				DayResult dayResult = new DayResult();
				dayResult.setDay2(data[0]);
				dayResult.setStrDate(data[1]);
				dayResult.setUnsei(data[2]);
				dayResult.setNegaigoto(data[3]);
				dayResult.setAkinai(data[4]);
				dayResult.setGakumon(data[5]);

				ystrday.add(dayResult);

			}
			br.close();
		} catch (IOException e) {
			System.out.println(e);

		}

		return ystrday;

	}
}
