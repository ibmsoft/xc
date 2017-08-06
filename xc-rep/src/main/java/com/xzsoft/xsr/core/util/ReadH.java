package com.xzsoft.xsr.core.util;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadH {

	BufferedReader br2;
	int curLine = 0;//当前行
	public String readLine() throws IOException {
		this.curLine = this.curLine + 1;
		return br2.readLine();
	}
	public void setBufferedReader(BufferedReader br2) {
		this.br2 = br2;
	}
	public int getCurLine() {
		return curLine;
	}
}
