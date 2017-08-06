/**
 *云ERP打印js 
 */
var XCVP = {
		/**
		 * 单个凭证打印
		 * 
		 * @param contextPath
		 * @param headerIds
		 *            凭证头id（多个凭证id拼接成'a','b'...的形式）
		 * @param leaderId
		 *            账簿id
		 * @param voucherType
		 *            凭证类型 1-金额凭证；2-数量凭证；3-外币凭证
		 */
		singlePrint : function(contextPath, headerIds, leaderId, voucherType,
				isAccount) {
			var params = '&isFirst=N&ledgerId=' + leaderId + '&headerIds='
					+ headerIds;
			var url = contextPath
					+ '/ReportServer?reportlet=xc_voucher/voucher_print_cash_t.cpt';
			if (voucherType == '2') {
				url = contextPath
						+ '/ReportServer?reportlet=xc_voucher/voucher_print_amount_t.cpt';
			}
			if (voucherType == '3') {
				url = contextPath
						+ '/ReportServer?reportlet=xc_voucher/voucher_print_foreign_t.cpt';
			}
			window.open(url + params, '_blank');
		},
		/**
		 * 批量凭证打印
		 * 
		 * @param contextPath
		 * @param headerIds
		 *            凭证头id（多个凭证id拼接成'a','b'...的形式）
		 * @param leaderId
		 *            账簿id
		 * @param voucherType
		 *            凭证类型 1-金额凭证；2-数量凭证；3-外币凭证
		 */
		batchPrint : function(contextPath, headerIds, leaderId, voucherType,
				isAccount,systemLanguage) {
			var params = '&isFirst=N&ledgerId=' + leaderId + '&headerIds='
					+ headerIds+'&language='+systemLanguage;
			var url = contextPath
					+ '/ReportServer?reportlet=xc_voucher/voucher_print_vertical_cash_t.cpt';
			if (voucherType == '2') {
				url = contextPath
						+ '/ReportServer?reportlet=xc_voucher/voucher_print_vertical_amount_t.cpt';
			}
			if (voucherType == '3') {
				url = contextPath
						+ '/ReportServer?reportlet=xc_voucher/voucher_print_vertical_foreign_t.cpt';
			}
			window.open(url + params, '_blank');
		},
		/**
		 * 批量凭证打印（A4）
		 * 
		 * @param contextPath
		 * @param headerIds
		 *            凭证头id（多个凭证id拼接成'a','b'...的形式）
		 * @param leaderId
		 *            账簿id
		 * @param voucherType
		 *            凭证类型 1-金额凭证；2-数量凭证；3-外币凭证
		 */
		batchA4Print : function(contextPath, headerIds, leaderId, voucherType,
				isAccount,systemLanguage) {
			var params = '&isFirst=N&ledgerId=' + leaderId + '&headerIds='
					+ headerIds+'&language='+systemLanguage;;
			var url = contextPath
					+ '/ReportServer?reportlet=xc_voucher/voucher_A4_cash_t.cpt';
			if (voucherType == '2') {
				url = contextPath
						+ '/ReportServer?reportlet=xc_voucher/voucher_A4_amount_t.cpt';
			}
			if (voucherType == '3') {
				url = contextPath
						+ '/ReportServer?reportlet=xc_voucher/voucher_A4_foreign_t.cpt';
			}
			window.open(url + params, '_blank');
		}

};

