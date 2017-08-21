package struts;


import java.util.ResourceBundle;

interface Fortune {
	
ResourceBundle rb= ResourceBundle.getBundle("fortune");
String DISP_STR = rb.getString("disp_str");
	String disp();
}
