package struts.dto;

import struts.Fortune;

public class Omikuji implements Fortune {
	// 運勢を格納するおみくじ抽象クラス
	private String unsei;
	private String negaigoto;
	private String akinai;
	private String gakumon;
	private String today;
	private String birthday;
	private int omikujiId;

	public int getOmikujiId() {
		return omikujiId;
	}

	public void setOmikujiId(int omikujiId) {
		this.omikujiId = omikujiId;
	}

	public String getUnsei() {
		return unsei;
	}

	public void setUnsei(String unsei) {
		this.unsei = unsei;
	}

	public String getNegaigoto() {
		return negaigoto;
	}

	public void setNegaigoto(String negaigoto) {
		this.negaigoto = negaigoto;
	}

	public String getAkinai() {
		return akinai;
	}

	public void setAkinai(String akinai) {
		this.akinai = akinai;
	}

	public String getGakumon() {
		return gakumon;
	}

	public void setGakumon(String gakumon) {
		this.gakumon = gakumon;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	@Override
	public String disp() {
		// TODO 自動生成されたメソッド・スタブ

		// 文字列連結
		StringBuilder buf = new StringBuilder();
		String str = String.format(DISP_STR, unsei);
		String sep = System.getProperty("line.separator");

		buf.append(str);
		buf.append(sep);
		buf.append("願い事：");
		buf.append(getNegaigoto());
		buf.append(sep);
		buf.append("商い：");
		buf.append(getAkinai());
		buf.append(sep);
		buf.append("学問：");
		buf.append(getGakumon());
		return buf.toString();
	}
}
