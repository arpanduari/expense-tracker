@import java.util.Map
@param Map<String, String> data

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml"
      xmlns:o="urn:schemas-microsoft-com:office:office">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Email Verification - ExpenseWise</title>
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
            background-color: #d4c4a8 !important;
            font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif !important;
            font-size: 16px;
            line-height: 1.5;
            color: #333333;
        }

        .email-wrapper {
            width: 100% !important;
            background-color: #d4c4a8;
            padding: 20px 0;
        }

        .email-container {
            max-width: 520px !important;
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
            border-bottom: 2px solid #e8ddd4;
        }

        .content-section {
            padding: 30px 32px 40px 32px !important;
            text-align: center !important;
            background-color: #ffffff;
        }

        .logo-img {
            width: 240px !important;
            height: auto !important;
            max-width: 100% !important;
            display: block !important;
            margin: 0 auto 24px auto !important;
        }

        .main-title {
            font-size: 28px !important;
            font-weight: 600 !important;
            color: #654321 !important;
            margin: 0 0 15px 0 !important;
            line-height: 1.2 !important;
        }

        .brand-accent {
            width: 60px !important;
            height: 4px !important;
            background: linear-gradient(90deg, #8b4513 0%, #cd853f 100%) !important;
            margin: 0 auto !important;
            border-radius: 2px !important;
        }

        .greeting-text {
            font-size: 18px !important;
            font-weight: 500 !important;
            color: #654321 !important;
            margin: 0 0 16px 0 !important;
        }

        .description-text {
            font-size: 16px !important;
            color: #4a3728 !important;
            margin: 0 0 30px 0 !important;
            line-height: 1.5 !important;
        }

        .company-highlight {
            color: #654321 !important;
            font-weight: 600 !important;
        }

        .otp-container {
            background: linear-gradient(135deg, #f4f0e8 0%, #ede4d3 100%) !important;
            border: 2px solid #d4c4a8 !important;
            border-radius: 12px !important;
            padding: 20px 30px !important;
            margin: 30px auto !important;
            display: inline-block !important;
            box-shadow: 0 4px 12px rgba(101, 67, 33, 0.1) !important;
        }

        .otp-code {
            font-size: 32px !important;
            font-weight: 700 !important;
            color: #654321 !important;
            letter-spacing: 8px !important;
            font-family: 'Courier New', monospace !important;
            margin: 0 !important;
        }

        .expiry-notice {
            background-color: #fef7f0 !important;
            border-left: 4px solid #cd853f !important;
            border-radius: 8px !important;
            padding: 15px 20px !important;
            margin: 25px 0 !important;
            text-align: left !important;
        }

        .expiry-text {
            color: #8b4513 !important;
            font-weight: 500 !important;
            font-size: 14px !important;
            margin: 0 !important;
        }

        .instruction-text {
            font-size: 16px !important;
            color: #4a3728 !important;
            margin: 20px 0 0 0 !important;
            line-height: 1.5 !important;
        }

        .footer-section {
            padding: 25px 32px !important;
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
                margin: 0 0 10px 0 !important;
            }

            .logo-img {
                width: 180px !important;
                margin: 0 auto 20px auto !important;
            }

            .brand-accent {
                width: 50px !important;
                height: 3px !important;
            }

            .greeting-text {
                font-size: 16px !important;
                margin: 0 0 12px 0 !important;
            }

            .description-text {
                font-size: 14px !important;
                margin: 0 0 20px 0 !important;
            }

            .otp-container {
                padding: 15px 20px !important;
                margin: 20px auto !important;
            }

            .otp-code {
                font-size: 24px !important;
                letter-spacing: 6px !important;
            }

            .expiry-notice {
                padding: 12px 15px !important;
                margin: 20px 0 !important;
            }

            .expiry-text {
                font-size: 13px !important;
            }

            .instruction-text {
                font-size: 14px !important;
                margin: 15px 0 0 0 !important;
            }

            .footer-text {
                font-size: 12px !important;
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
                        <h1 class="main-title">Email Verification</h1>

                        <!-- Brand Accent -->
                        <div class="brand-accent"></div>
                    </td>
                </tr>

                <!-- Content Section -->
                <tr>
                    <td class="content-section">
                        <!-- Greeting -->
                        <p class="greeting-text">Hello, ${data.get("username")}</p>

                        <!-- Description -->
                        <p class="description-text">
                            Thank you for registering with <span class="company-highlight">ExpenseWise</span>. Please
                            use the verification code below to complete your email verification:
                        </p>

                        <!-- OTP Code -->
                        <div class="otp-container">
                            <p class="otp-code">${data.get("otp")}</p>
                        </div>

                        <!-- Expiry Notice -->
                        <div class="expiry-notice">
                            <p class="expiry-text">
                                <strong>Important:</strong> This verification code will expire in <strong>5
                                    minutes</strong>.
                            </p>
                        </div>

                        <!-- Instructions -->
                        <p class="instruction-text">
                            Please enter this code in the verification page to activate your account.
                        </p>
                    </td>
                </tr>

                <!-- Footer Section -->
                <tr>
                    <td class="footer-section">
                        <p class="footer-text">If you did not request this verification, please ignore this email.</p>
                        <p class="footer-text">©${data.get("year")} <span class="company-highlight">ExpenseWise</span>.
                            All rights
                            reserved.</p>
                    </td>
                </tr>

            </table>
        </td>
    </tr>
</table>
</body>
</html>
