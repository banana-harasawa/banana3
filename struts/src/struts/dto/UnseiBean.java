package struts.dto;

import java.math.BigDecimal;

import org.apache.struts.action.ActionForm;

public class UnseiBean extends ActionForm {

	private String unsei;
	private BigDecimal unseiCount;

	public String getUnsei() {
		return unsei;
	}

	public void setUnsei(String unsei) {
		this.unsei = unsei;
	}

	public BigDecimal getUnseiId() {
		return unseiCount;
	}

	public BigDecimal setUnseiId(BigDecimal unseiId) {
		return this.unseiCount = unseiId;
	}
}
