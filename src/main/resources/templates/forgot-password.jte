@import java.util.Map
@param Map<String, String> data
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml"
      xmlns:o="urn:schemas-microsoft-com:office:office">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Reset Your Password - ExpenseWise</title>
    <!--[if !mso]><!-->
    <style type="text/css">
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
            background-color: #D2B48C !important;
            font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif !important;
            font-size: 16px;
            line-height: 1.5;
            color: #333333;
        }

        .email-wrapper {
            width: 100% !important;
            background-color: #D2B48C;
            padding: 20px 0;
        }

        .email-container {
            max-width: 480px !important;
            margin: 0 auto !important;
            background-color: #ffffff !important;
            border-radius: 16px !important;
            overflow: hidden;
            box-shadow: 0 4px 12px rgba(101, 67, 33, 0.15);
        }

        .header-section {
            padding: 40px 32px 20px 32px !important;
            text-align: center !important;
            background-color: #ffffff;
        }

        .content-section {
            padding: 20px 32px 40px 32px !important;
            text-align: center !important;
            background-color: #ffffff;
        }

        .logo-img {
            width: 220px !important;
            height: auto !important;
            max-width: 100% !important;
            display: block !important;
            margin: 0 auto 30px auto !important;
        }

        .main-title {
            font-size: 28px !important;
            font-weight: 600 !important;
            color: #654321 !important;
            margin: 0 0 20px 0 !important;
            line-height: 1.2 !important;
        }

        .greeting-text {
            font-size: 16px !important;
            color: #8B7355 !important;
            margin: 0 0 8px 0 !important;
        }

        .description-text {
            font-size: 16px !important;
            color: #8B7355 !important;
            margin: 0 0 30px 0 !important;
            line-height: 1.5 !important;
        }

        .company-highlight {
            color: #654321 !important;
            font-weight: 500 !important;
        }

        /* Button table for maximum email client compatibility */
        .button-table {
            margin: 30px auto !important;
        }

        .button-cell {
            background-color: #654321 !important;
            border-radius: 8px !important;
            padding: 0 !important;
        }

        .button-link {
            display: block !important;
            padding: 16px 32px !important;
            background-color: #654321 !important;
            color: #FFF8DC !important;
            text-decoration: none !important;
            font-weight: 500 !important;
            font-size: 16px !important;
            border-radius: 8px !important;
            text-align: center !important;
        }

        .alternative-text {
            font-size: 14px !important;
            color: #8B7355 !important;
            margin: 20px 0 8px 0 !important;
        }

        .reset-link {
            font-size: 14px !important;
            color: #D2691E !important;
            text-decoration: underline !important;
            word-break: break-all;
            margin: 0 0 20px 0 !important;
            display: block !important;
        }

        .security-notice {
            font-size: 14px !important;
            color: #8B7355 !important;
            margin: 20px 0 !important;
            line-height: 1.4 !important;
        }

        .footer-section {
            padding: 20px 32px !important;
            text-align: center !important;
            border-top: 1px solid #e5e5e5 !important;
            background-color: #ffffff;
        }

        .signature-text {
            font-size: 14px !important;
            color: #8B7355 !important;
            margin: 8px 0 !important;
        }

        .team-name {
            font-size: 14px !important;
            color: #A0522D !important;
            font-style: italic !important;
            margin: 0 !important;
        }

        /* Responsive styles */
        @media only screen and (max-width: 600px) {
            .email-wrapper {
                padding: 10px 0 !important;
            }

            .email-container {
                width: 95% !important;
                margin: 0 auto !important;
                max-width: 400px !important;
            }

            .header-section {
                padding: 20px 15px 10px 15px !important;
            }

            .content-section {
                padding: 10px 15px 20px 15px !important;
            }

            .footer-section {
                padding: 15px !important;
            }

            .main-title {
                font-size: 20px !important;
                margin: 0 0 12px 0 !important;
            }

            .logo-img {
                width: 180px !important;
                margin: 0 auto 20px auto !important;
            }

            .greeting-text {
                font-size: 15px !important;
                margin: 0 0 6px 0 !important;
            }

            .description-text {
                font-size: 14px !important;
                margin: 0 0 20px 0 !important;
            }

            .button-table {
                margin: 20px auto !important;
            }

            .button-link {
                padding: 12px 20px !important;
                font-size: 14px !important;
            }

            .alternative-text {
                font-size: 13px !important;
                margin: 15px 0 6px 0 !important;
            }

            .reset-link {
                font-size: 13px !important;
                margin: 0 0 15px 0 !important;
            }

            .security-notice {
                font-size: 13px !important;
                margin: 15px 0 !important;
            }

            .signature-text,
            .team-name {
                font-size: 13px !important;
                margin: 6px 0 !important;
            }
        }
    </style>
    <!--<![endif]-->
</head>
<body>
<!-- Email Wrapper Table -->
<table role="presentation" cellspacing="0" cellpadding="0" border="0" width="100%" class="email-wrapper">
    <tr>
        <td align="center">
            <!-- Main Email Container -->
            <table role="presentation" cellspacing="0" cellpadding="0" border="0" class="email-container">

                <!-- Header Section with Logo -->
                <tr>
                    <td class="header-section">
                        <img src="${data.get("appLogo")}" alt="ExpenseWise Logo" class="logo-img">

                        <!-- Main Title -->
                        <h1 class="main-title">Reset your password</h1>
                    </td>
                </tr>

                <!-- Content Section -->
                <tr>
                    <td class="content-section">
                        <!-- Greeting -->
                        <p class="greeting-text">Hey ${data.get("username")},</p>

                        <!-- Description -->
                        <p class="description-text">
                            A request has been received to change the password for your <span class="company-highlight">ExpenseWise</span>
                            account.
                        </p>

                        <!-- Reset Button -->
                        <table role="presentation" cellspacing="0" cellpadding="0" border="0" class="button-table">
                            <tr>
                                <td class="button-cell">
                                    <a href="${data.get("resetLink")}" class="button-link">Reset Password</a>
                                </td>
                            </tr>
                        </table>

                        <!-- Alternative link -->
                        <p class="alternative-text">If the link doesn't work, paste this link to your browser:</p>
                        <a href="${data.get("resetLink")}" class="reset-link">${data.get("resetLink")}</a>

                        <!-- Security notice -->
                        <p class="security-notice">
                            If you didn't request a password change, ignore this email.
                        </p>
                    </td>
                </tr>

                <!-- Footer Section -->
                <tr>
                    <td class="footer-section">
                        <p class="signature-text">Thank you,</p>
                        <p class="team-name">ExpenseWise Team</p>
                    </td>
                </tr>

            </table>
        </td>
    </tr>
</table>
</body>
</html>