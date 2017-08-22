package struts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
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

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (birthday == null || birthday.length() == 0) {
			errors.add("", new ActionMessage("errors.required","誕生日"));
			return errors;
		}
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date input = null;
		try {
			input = sdf.parse(birthday);
		} catch (ParseException e) {
			errors.add("", new ActionMessage("errors.date"));
			return errors;
		}

		;
		if(input.compareTo(now) > 0) {
			errors.add("", new ActionMessage("errors.future"));
		}
		return errors;
	}
}