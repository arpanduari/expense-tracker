@import java.util.Map
@param Map<String, String> data
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml"
      xmlns:o="urn:schemas-microsoft-com:office:office">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Welcome to ExpenseWise - Account Created Successfully</title>
    <!--[if !mso]><!-->
    <style type="text/css">
        /* Reset styles for email clients */
        body,
        table,
        td,
        p,
        a,
        li,
        blockquote {
            -webkit-text-size-adjust: 100%;
            -ms-text-size-adjust: 100%;
        }

        table,
        td {
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
            padding: 30px 20px 15px 20px !important;
            text-align: center !important;
            background-color: #ffffff;
            border-bottom: 2px solid #e8ddd4;
        }

        .content-section {
            padding: 20px 20px 30px 20px !important;
            text-align: center !important;
            background-color: #ffffff;
        }

        .logo-img {
            width: 240px !important;
            height: auto !important;
            max-width: 100% !important;
            display: block !important;
            margin: 0 auto 25px auto !important;
        }

        .welcome-icon {
            width: 80px !important;
            height: 80px !important;
            display: block !important;
            margin: 0 auto 25px auto !important;
        }

        .main-title {
            font-size: 26px !important;
            font-weight: 600 !important;
            color: #654321 !important;
            margin: 0 0 15px 0 !important;
            line-height: 1.2 !important;
        }

        .brand-accent {
            width: 60px !important;
            height: 4px !important;
            background: linear-gradient(90deg, #8b4513 0%, #cd853f 100%) !important;
            margin: 0 auto 20px auto !important;
            border-radius: 2px !important;
        }

        .welcome-box {
            background-color: #f0fdf4 !important;
            border: 2px solid #bbf7d0 !important;
            border-radius: 12px !important;
            padding: 20px !important;
            margin: 20px 0 !important;
        }

        .welcome-title {
            color: #15803d !important;
            font-size: 18px !important;
            font-weight: 600 !important;
            margin: 0 0 8px 0 !important;
        }

        .welcome-text {
            color: #166534 !important;
            font-size: 15px !important;
            margin: 0 !important;
            line-height: 1.4 !important;
        }

        .greeting-text {
            font-size: 17px !important;
            font-weight: 500 !important;
            color: #654321 !important;
            margin: 0 0 12px 0 !important;
        }

        .description-text {
            font-size: 15px !important;
            color: #4a3728 !important;
            margin: 0 0 25px 0 !important;
            line-height: 1.5 !important;
        }

        .company-highlight {
            color: #654321 !important;
            font-weight: 600 !important;
        }

        .features-section {
            background-color: #fef7f0 !important;
            border-radius: 12px !important;
            padding: 20px !important;
            margin: 25px 0 !important;
            text-align: left !important;
        }

        .features-title {
            color: #654321 !important;
            font-size: 16px !important;
            font-weight: 600 !important;
            margin: 0 0 12px 0 !important;
            text-align: center !important;
        }

        .feature-item {
            color: #8b4513 !important;
            font-size: 14px !important;
            margin: 8px 0 !important;
            line-height: 1.4 !important;
            padding-left: 20px !important;
            position: relative !important;
        }

        .feature-item:before {
            content: "✓" !important;
            color: #15803d !important;
            font-weight: bold !important;
            position: absolute !important;
            left: 0 !important;
        }

        /* Button table for maximum email client compatibility */
        .button-table {
            margin: 25px auto !important;
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

        .support-text {
            color: #A0522D !important;
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
                padding: 15px 15px 20px 15px !important;
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

            .welcome-icon {
                width: 60px !important;
                height: 60px !important;
                margin: 0 auto 20px auto !important;
            }

            .brand-accent {
                width: 50px !important;
                height: 3px !important;
                margin: 0 auto 15px auto !important;
            }

            .welcome-box {
                padding: 15px !important;
                margin: 15px 0 !important;
            }

            .welcome-title {
                font-size: 16px !important;
                margin: 0 0 6px 0 !important;
            }

            .welcome-text {
                font-size: 14px !important;
            }

            .greeting-text {
                font-size: 16px !important;
                margin: 0 0 10px 0 !important;
            }

            .description-text {
                font-size: 14px !important;
                margin: 0 0 20px 0 !important;
            }

            .features-section {
                padding: 15px !important;
                margin: 20px 0 !important;
            }

            .features-title {
                font-size: 14px !important;
                margin: 0 0 10px 0 !important;
            }

            .feature-item {
                font-size: 13px !important;
                margin: 6px 0 !important;
                padding-left: 18px !important;
            }

            .button-table {
                margin: 20px auto !important;
            }

            .button-link {
                padding: 12px 20px !important;
                font-size: 14px !important;
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

                        <!-- Welcome Icon -->
                        <img src="https://cdn-icons-png.flaticon.com/512/14090/14090371.png" alt="Welcome"
                             class="welcome-icon">

                        <!-- Main Title -->
                        <h1 class="main-title">Welcome to ExpenseWise!</h1>

                        <!-- Brand Accent -->
                        <div class="brand-accent"></div>
                    </td>
                </tr>

                <!-- Content Section -->
                <tr>
                    <td class="content-section">
                        <!-- Welcome Message Box -->
                        <div class="welcome-box">
                            <h2 class="welcome-title">🎉 Your account has been created successfully!</h2>
                            <p class="welcome-text">Welcome to the <span
                                        class="company-highlight">ExpenseWise</span> family. You're now ready to take
                                control of your finances!</p>
                        </div>

                        <!-- Greeting -->
                        <p class="greeting-text">Hello, ${data.get("username")},</p>

                        <!-- Description -->
                        <p class="description-text">
                            Thank you for joining <span class="company-highlight">ExpenseWise</span>! We're excited
                            to help you manage your expenses, track your spending, and achieve your financial goals.
                        </p>

                        <!-- Get Started Button -->
                        <table role="presentation" cellspacing="0" cellpadding="0" border="0" class="button-table">
                            <tr>
                                <td class="button-cell">
                                    <a href="${data.get("loginUrl")}" class="button-link">Get Started Now</a>
                                </td>
                            </tr>
                        </table>
                    </td>
            </tr>

                <!-- Footer Section -->
                <tr>
                    <td class="footer-section">
                        <p class="footer-text support-text">Need help getting started? Check out our help center or
                            contact our support team.</p>
                        <p class="footer-text">©${data.get("year")}<span class="company-highlight">ExpenseWise</span>. All
                            rights reserved.</p>
                    </td>
                </tr>

            </table>
        </td>
    </tr>
</table>
</body>

</html>