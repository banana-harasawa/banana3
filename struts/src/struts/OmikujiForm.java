package struts;

import org.apache.struts.action.ActionForm;

public class OmikujiForm extends ActionForm{

	//運勢を格納するおみくじ抽象クラス
		private String unsei;
		private String negaigoto;
		private String akinai;
		private String gakumon;
		private String today;
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
}
