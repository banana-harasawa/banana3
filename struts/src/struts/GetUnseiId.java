package struts;


public class GetUnseiId {
	
	public int unseiId(String unsei){
		int unseiId = 0;
		switch(unsei){
		case "大吉": 
			unseiId=1;
			break;
		case "小吉": 
			unseiId=2;
			break;
		case "中吉": 
			unseiId=3;
			break;
		case "吉": 
			unseiId=4;
			break;
		case "末吉": 
			unseiId=5;
			break;
		case "凶": 
			unseiId=6;
			break;
		}
		return unseiId;
	}
}
