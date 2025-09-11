package gg.jte.generated.ondemand.transactions;
import java.time.format.DateTimeFormatter;
import java.util.List;
import dev.arpan.expensetracker.expense.Expense;
@SuppressWarnings("unchecked")
public final class JtetransactionstemplateGenerated {
	public static final String JTE_NAME = "transactions/transactions-template.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,4,4,4,4,22,22,40,63,111,169,199,201,201,201,201,201,201,201,201,201,205,208,208,208,211,211,211,213,213,213,214,214,214,216,218,218,230,230,232,232,232,233,233,233,234,234,234,235,235,235,237,237,241,241,243,243,246,250,250,250,257,257,257,4,5,6,7,8,9,10,10,10,10};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String logoUrl, String username, String email, String startDate, String endDate, List<Expense> expenses, String year) {
		jteOutput.writeContent("\n<!DOCTYPE html>\n<html>\n<head>\n    <meta charset=\"UTF-8\"/>\n    <title>Transaction Summary</title>\n    <style type=\"text/css\">\n        @page {\n            margin: 0;\n        }\n\n        ");
		jteOutput.writeContent("\n        body, table, td, p, a, li, blockquote {\n            -webkit-text-size-adjust: 100%;\n            -ms-text-size-adjust: 100%;\n        }\n\n        table, td {\n            mso-table-lspace: 0pt;\n            mso-table-rspace: 0pt;\n        }\n\n        img {\n            -ms-interpolation-mode: bicubic;\n            border: 0;\n            outline: none;\n            text-decoration: none;\n        }\n\n        ");
		jteOutput.writeContent("\n        body {\n            margin: 0 !important;\n            padding: 0 !important;\n            background-color: #d4c4a8 !important;\n            font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif !important;\n            font-size: 16px;\n            line-height: 1.5;\n            color: #333333;\n        }\n\n        .email-wrapper {\n            width: 100% !important;\n            background-color: #d4c4a8;\n            padding: 10px 5px;\n            clip-path: inset(0 round 8px);\n        }\n\n        .email-container {\n            max-width: 95% !important;\n            margin: 0 auto !important;\n            background-color: #ffffff !important;\n            border-radius: 8px !important;\n            ");
		jteOutput.writeContent("\n            box-shadow: 0 4px 12px rgba(101, 67, 33, 0.15);\n        }\n\n        .header-section {\n            padding: 20px 15px 10px 15px !important;\n            text-align: center !important;\n            background-color: #ffffff;\n            border-bottom: 2px solid #e8ddd4;\n        }\n\n        .content-section {\n            padding: 15px 15px 20px 15px !important;\n            background-color: #ffffff;\n        }\n\n        .logo-img {\n            width: 200px !important;\n            height: auto !important;\n            max-width: 100% !important;\n            display: block !important;\n            margin: 0 auto 20px auto !important;\n        }\n\n        .main-title {\n            font-size: 24px !important;\n            font-weight: 600 !important;\n            color: #654321 !important;\n            margin: 0 0 15px 0 !important;\n            line-height: 1.2 !important;\n        }\n\n        .welcome-heading {\n            font-size: 18px !important;\n            font-weight: 500 !important;\n            color: #654321 !important;\n            margin: 10px 0;\n            text-align: center !important;\n        }\n\n        .description-text {\n            font-size: 15px !important;\n            color: #4a3728 !important;\n            margin: 0 0 20px 0 !important;\n            line-height: 1.5 !important;\n            text-align: center !important;\n        }\n\n        ");
		jteOutput.writeContent("\n        .transactions-table {\n            width: 100%;\n            border-collapse: collapse;\n            margin: 15px 0;\n            table-layout: fixed;\n        }\n\n        .transactions-table th {\n            background-color: #f5f5f5;\n            color: #654321;\n            padding: 12px 8px;\n            border: 1px solid #ddd;\n            font-size: 14px;\n            text-align: left;\n            font-weight: 600;\n        }\n\n        .transactions-table td {\n            padding: 12px 8px;\n            border: 1px solid #ddd;\n            font-size: 14px;\n            word-wrap: break-word;\n            vertical-align: top;\n        }\n\n        .transactions-table th:nth-child(1),\n        .transactions-table td:nth-child(1) {\n            width: 20%;\n        }\n\n        .transactions-table th:nth-child(2),\n        .transactions-table td:nth-child(2) {\n            width: 35%;\n        }\n\n        .transactions-table th:nth-child(3),\n        .transactions-table td:nth-child(3) {\n            width: 20%;\n        }\n\n        .transactions-table th:nth-child(4),\n        .transactions-table td:nth-child(4) {\n            width: 25%;\n        }\n\n        .transactions-table tr:nth-child(even) {\n            background-color: #fafafa;\n        }\n\n        .transactions-table .amount {\n            text-align: right;\n        }\n\n        .transactions-table .amount-header {\n            text-align: right;\n        }\n\n        ");
		jteOutput.writeContent("\n        .footer-section {\n            padding: 15px 20px !important;\n            text-align: center !important;\n            border-top: 1px solid #e8ddd4 !important;\n            background-color: #ffffff;\n        }\n\n        .footer-text {\n            font-size: 13px !important;\n            color: #8b7355 !important;\n            margin: 8px 0 !important;\n            line-height: 1.4 !important;\n        }\n\n        .support-text {\n            color: #A0522D !important;\n        }\n\n        .company-highlight {\n            color: #654321 !important;\n        }\n\n\n    </style>\n</head>\n<body>\n<div class=\"email-wrapper\">\n    <div class=\"email-container\" style=\"border-radius: 20%;\">\n\n        ");
		jteOutput.writeContent("\n        <div class=\"header-section\">\n            <img");
		var __jte_html_attribute_0 = logoUrl;
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeContent(" src=\"");
			jteOutput.setContext("img", "src");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("img", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" alt=\"Company Logo\" class=\"logo-img\"/>\n            <h1 class=\"main-title\">Transaction Summary</h1>\n        </div>\n\n        ");
		jteOutput.writeContent("\n        <div class=\"content-section\">\n            <p class=\"welcome-heading\">Hello, <span\n                        class=\"company-highlight\">");
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(username);
		jteOutput.writeContent("</span>\n            </p>\n            <p class=\"description-text\">Here are your transactions\n                linked to <b>");
		jteOutput.setContext("b", null);
		jteOutput.writeUserContent(email);
		jteOutput.writeContent("</b>.</p>\n            <p class=\"description-text\">Your transaction from\n                ");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(startDate);
		jteOutput.writeContent("\n                to ");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(endDate);
		jteOutput.writeContent("</p>\n        </div>\n        ");
		jteOutput.writeContent("\n        <div class=\"content-section\" style=\"padding: 15px 10px 20px 10px;\">\n            ");
		if (expenses != null && !expenses.isEmpty()) {
			jteOutput.writeContent("\n                <div style=\"overflow-x: auto;\">\n                    <table class=\"transactions-table\">\n                        <thead>\n                        <tr>\n                            <th>Category</th>\n                            <th>Description</th>\n                            <th class=\"amount-header\">Amount</th>\n                            <th>Created Date</th>\n                        </tr>\n                        </thead>\n                        <tbody>\n                        ");
			for (Expense expense : expenses) {
				jteOutput.writeContent("\n                            <tr>\n                                <td>");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(expense.getCategory().getName().substring(0, 1).toUpperCase() + expense.getCategory().getName().substring(1).toLowerCase());
				jteOutput.writeContent("</td>\n                                <td>");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(expense.getDescription());
				jteOutput.writeContent("</td>\n                                <td class=\"amount\">");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(expense.getAmount());
				jteOutput.writeContent("</td>\n                                <td>");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(expense.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				jteOutput.writeContent("</td>\n                            </tr>\n                        ");
			}
			jteOutput.writeContent("\n                        </tbody>\n                    </table>\n                </div>\n            ");
		} else {
			jteOutput.writeContent("\n                <p class=\"description-text\">You have no recent transactions.</p>\n            ");
		}
		jteOutput.writeContent("\n        </div>\n\n        ");
		jteOutput.writeContent("\n        <div class=\"footer-section\">\n            <p class=\"footer-text\">Need help? Contact our\n                <span class=\"support-text\">support team</span>.</p>\n            <p class=\"footer-text\">©");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(year);
		jteOutput.writeContent(" Your Company. All rights\n                reserved.</p>\n        </div>\n\n    </div>\n</div>\n</body>\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String logoUrl = (String)params.get("logoUrl");
		String username = (String)params.get("username");
		String email = (String)params.get("email");
		String startDate = (String)params.get("startDate");
		String endDate = (String)params.get("endDate");
		List<Expense> expenses = (List<Expense>)params.get("expenses");
		String year = (String)params.get("year");
		render(jteOutput, jteHtmlInterceptor, logoUrl, username, email, startDate, endDate, expenses, year);
	}
}
