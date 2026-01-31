@import java.time.format.DateTimeFormatter
@import java.util.List
@import dev.expensewise.backend.expense.Expense

@param String logoUrl
@param String username
@param String email
@param String startDate
@param String endDate
@param List<Expense> expenses
@param String year

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Transaction Summary</title>
    <style type="text/css">
        @page {
            margin: 0;
        }

        /* Reset styles for email clients */
        body, table, td, p, a, li, blockquote {
            -webkit-text-size-adjust: 100%;
            -ms-text-size-adjust: 100%;
        }

        table, td {
            mso-table-lspace: 0pt;
            mso-table-rspace: 0pt;
        }

        img {
            -ms-interpolation-mode: bicubic;
            border: 0;
            outline: none;
            text-decoration: none;
        }

        /* Main styles */
        body {
            margin: 0 !important;
            padding: 0 !important;
            background-color: #d4c4a8 !important;
            font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif !important;
            font-size: 16px;
            line-height: 1.5;
            color: #333333;
        }

        .email-wrapper {
            width: 100% !important;
            background-color: #d4c4a8;
            padding: 10px 5px;
            clip-path: inset(0 round 8px);
        }

        .email-container {
            max-width: 95% !important;
            margin: 0 auto !important;
            background-color: #ffffff !important;
            border-radius: 8px !important;
            /*overflow: hidden;*/
            box-shadow: 0 4px 12px rgba(101, 67, 33, 0.15);
        }

        .header-section {
            padding: 20px 15px 10px 15px !important;
            text-align: center !important;
            background-color: #ffffff;
            border-bottom: 2px solid #e8ddd4;
        }

        .content-section {
            padding: 15px 15px 20px 15px !important;
            background-color: #ffffff;
        }

        .logo-img {
            width: 200px !important;
            height: auto !important;
            max-width: 100% !important;
            display: block !important;
            margin: 0 auto 20px auto !important;
        }

        .main-title {
            font-size: 24px !important;
            font-weight: 600 !important;
            color: #654321 !important;
            margin: 0 0 15px 0 !important;
            line-height: 1.2 !important;
        }

        .welcome-heading {
            font-size: 18px !important;
            font-weight: 500 !important;
            color: #654321 !important;
            margin: 10px 0;
            text-align: center !important;
        }

        .description-text {
            font-size: 15px !important;
            color: #4a3728 !important;
            margin: 0 0 20px 0 !important;
            line-height: 1.5 !important;
            text-align: center !important;
        }

        /* Transactions table */
        .transactions-table {
            width: 100%;
            border-collapse: collapse;
            margin: 15px 0;
            table-layout: fixed;
        }

        .transactions-table th {
            background-color: #f5f5f5;
            color: #654321;
            padding: 12px 8px;
            border: 1px solid #ddd;
            font-size: 14px;
            text-align: left;
            font-weight: 600;
        }

        .transactions-table td {
            padding: 12px 8px;
            border: 1px solid #ddd;
            font-size: 14px;
            word-wrap: break-word;
            vertical-align: top;
        }

        .transactions-table th:nth-child(1),
        .transactions-table td:nth-child(1) {
            width: 20%;
        }

        .transactions-table th:nth-child(2),
        .transactions-table td:nth-child(2) {
            width: 35%;
        }

        .transactions-table th:nth-child(3),
        .transactions-table td:nth-child(3) {
            width: 20%;
        }

        .transactions-table th:nth-child(4),
        .transactions-table td:nth-child(4) {
            width: 25%;
        }

        .transactions-table tr:nth-child(even) {
            background-color: #fafafa;
        }

        .transactions-table .amount {
            text-align: right;
        }

        .transactions-table .amount-header {
            text-align: right;
        }

        /* Footer */
        .footer-section {
            padding: 15px 20px !important;
            text-align: center !important;
            border-top: 1px solid #e8ddd4 !important;
            background-color: #ffffff;
        }

        .footer-text {
            font-size: 13px !important;
            color: #8b7355 !important;
            margin: 8px 0 !important;
            line-height: 1.4 !important;
        }

        .support-text {
            color: #A0522D !important;
        }

        .company-highlight {
            color: #654321 !important;
        }


    </style>
</head>
<body>
<div class="email-wrapper">
    <div class="email-container" style="border-radius: 20%;">

        <!-- Header -->
        <div class="header-section">
            <img src="${logoUrl}" alt="Company Logo" class="logo-img"/>
            <h1 class="main-title">Transaction Summary</h1>
        </div>

        <!-- User Info -->
        <div class="content-section">
            <p class="welcome-heading">Hello, <span
                        class="company-highlight">${username}</span>
            </p>
            <p class="description-text">Here are your transactions
                linked to <b>${email}</b>.</p>
            <p class="description-text">Your transaction from
                ${startDate}
                to ${endDate}</p>
        </div>
        <!-- Transactions Table -->
        <div class="content-section" style="padding: 15px 10px 20px 10px;">
            @if(expenses != null && !expenses.isEmpty())
                <div style="overflow-x: auto;">
                    <table class="transactions-table">
                        <thead>
                        <tr>
                            <th>Category</th>
                            <th>Description</th>
                            <th class="amount-header">Amount</th>
                            <th>Created Date</th>
                        </tr>
                        </thead>
                        <tbody>
                        @for(Expense expense : expenses)
                            <tr>
                                <td>${expense.getCategory().getName().substring(0, 1).toUpperCase() + expense.getCategory().getName().substring(1).toLowerCase()}</td>
                                <td>${expense.getDescription()}</td>
                                <td class="amount">${expense.getAmount()}</td>
                                <td>${expense.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}</td>
                            </tr>
                        @endfor
                        </tbody>
                    </table>
                </div>
            @else
                <p class="description-text">You have no recent transactions.</p>
            @endif
        </div>

        <!-- Footer -->
        <div class="footer-section">
            <p class="footer-text">Need help? Contact our
                <span class="support-text">support team</span>.</p>
            <p class="footer-text">©${year} Your Company. All rights
                reserved.</p>
        </div>

    </div>
</div>
</body>
</html>