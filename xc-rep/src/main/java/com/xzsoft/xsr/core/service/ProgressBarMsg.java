package com.xzsoft.xsr.core.service;

import javax.servlet.http.HttpSession;

public interface ProgressBarMsg {
	public void progressMsgIntoSession(HttpSession session, String modalSheetId, int count, int totalCount, String flag,
			String scrollBarMark);
}
