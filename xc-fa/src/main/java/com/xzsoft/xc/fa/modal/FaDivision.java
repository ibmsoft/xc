package com.xzsoft.xc.fa.modal;

import java.util.List;

public class FaDivision {
	// 头信息
		private FaDivisionH faDivisionH;
		// 行信息
		private List<FaDivisionL> faDivisionLs;
		//资产信息集合
		private List<FaAddition> faAdditions;
		//资产信息
		private FaAddition faAdditionOne ;
		//拆分资产个数/同步到资产个数
		private int countFa;
		//是否已经计提折旧
		private String isJt;
		
		public int getCountFa() {
			return countFa;
		}
		public void setCountFa(int countFa) {
			this.countFa = countFa;
		}
		public List<FaAddition> getFaAdditions() {
			return faAdditions;
		}
		public void setFaAdditions(List<FaAddition> faAdditions) {
			this.faAdditions = faAdditions;
		}
	
		public FaAddition getFaAdditionOne() {
			return faAdditionOne;
		}
		public void setFaAdditionOne(FaAddition faAdditionOne) {
			this.faAdditionOne = faAdditionOne;
		}
		public FaDivisionH getFaDivisionH() {
			return faDivisionH;
		}
		public void setFaDivisionH(FaDivisionH faDivisionH) {
			this.faDivisionH = faDivisionH;
		}
		public List<FaDivisionL> getFaDivisionLs() {
			return faDivisionLs;
		}
		public void setFaDivisionLs(List<FaDivisionL> faDivisionLs) {
			this.faDivisionLs = faDivisionLs;
		}
		public String getIsJt() {
			return isJt;
		}
		public void setIsJt(String isJt) {
			this.isJt = isJt;
		}
}
