package struts.action;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import struts.dao.OmikujiDao;
import struts.dto.Omikuji;
import struts.form.FortuneForm;
import struts.util.GetOmikuji;

public class FortuneAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse responsez) throws Exception {
		try {

			// ActionForm を LoginForm にキャス卜
			FortuneForm fortuneForm = (FortuneForm) form;
			// フォームに入力された ID を取得する
			String birthday = fortuneForm.getBirthday();

			HttpSession session = request.getSession(true);
			session.setAttribute("birth", birthday);

			// おみくじ50件が登録されているかの確認、登録されていない場合は登録される
			int i = OmikujiCommit();
			Omikuji obj = null;

			// 同じ日、同じ誕生日がないか確認。もしなければ、新しくおみくじを引く
			obj = OmikujiDao.findTodayResult(birthday);
			if (obj == null) {
				// 日付取得
				GetOmikuji omikuji = new GetOmikuji();
				Boolean flg = true;
				// おみくじの数を使ったランダムな数を取得
				int idx = omikuji.getIdx(i);
				request.setAttribute("omikujiId", idx);

				// もし入力された値がresultテーブルと同じ誕生日ならばflgをfalseにする
				// falseになる時、idxにはomikuij_idが入る
				flg = changeFlg(birthday, idx, flg);

				// flgがtrue => 新しいおみくじを引く。
				// flgがfalse => resultから同じ誕生日のおみくじ結果を取り出す。
				obj = OmikujiDao.resultOmikuji(idx);

				// resultテーブルに登録
				resultCommit(flg, idx, birthday, obj);
			}

			// message を request スコープに登録する
			request.setAttribute("message", obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ActionForward を返却
		return mapping.findForward("result");
	}

	// もし結果テーブルと同じ誕生日ならばflgをfalseにする
	public static boolean changeFlg(String birthday, int idx, boolean flg) throws SQLException {
		int res = OmikujiDao.sarchOmikuji(birthday);
		// omikuji_idは40とかが返ってくるこの構文はおかしい。必ず0以上になる。
		if (res != 0) {
			idx = res;
			flg = false;
		}
		return flg;
	}

	// resultテーブルにおみくじが登録されていなければ、おみくじを登録
	public static int OmikujiCommit() throws SQLException {
		OmikujiDao writer = new OmikujiDao();
		int i = OmikujiDao.checkTable();
		if (i == 0) {
			writer.writerOmikujiTbl();
		}
		return i;
	}

	// resultテーブルにおみくじ結果を保存
	public static void resultCommit(boolean flg, int idx, String birthday, Omikuji obj) throws SQLException {
		OmikujiDao writer = new OmikujiDao();
		// flgがtrueなら実行される
		if (flg) {
			obj.setOmikujiId(idx);
			obj.setBirthday(birthday);
			writer.writerResultTbl(obj);
		}
	}
}