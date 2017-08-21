package struts;

import org.apache.struts.validator.ValidatorForm;

public class FortuneForm extends ValidatorForm {
	// リクエス卜パラメータ名と同じフィールド
	private String birthday;

	// getter/setter メソッド
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	}