@import java.util.UUID
@param UUID shareId
@param boolean isAndroid
@param boolean isIos
@param String androidScheme
@param String androidPackage
@param String frontendPath

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shared Ledger</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            background: #f0f2f5;
        }
        .loading-box {
            background: white;
            border-radius: 16px;
            padding: 48px 40px;
            text-align: center;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }
        .spinner {
            border: 4px solid #eee;
            border-top: 4px solid #667eea;
            border-radius: 50%;
            width: 44px;
            height: 44px;
            animation: spin 0.8s linear infinite;
            margin: 0 auto 16px;
        }
        @keyframes spin {
            to {
                transform: rotate(360deg);
            }
        }
        .loading-box p {
            color: #999;
            font-size: 15px;
        }
    </style>
</head>
<body>
<div class="loading-box">
    <div class="spinner"></div>
    <p>Opening...</p>
</div>
<script>
    const shareId = '${shareId.toString()}';
    const isAndroid = ${isAndroid};
    const isIos = ${isIos};
    const androidPkg = '${androidPackage}';
    const androidScheme = '${androidScheme}';
    const frontendUrl = '${frontendPath}';

    if (isAndroid) {
        const intentUrl =
            'intent://share/' + shareId +
            '#Intent;scheme=' + androidScheme +
            ';package=' + androidPkg +
            ';S.browser_fallback_url=' + encodeURIComponent(frontendUrl + '/ledger/share/' + shareId) +
            ';end';
        window.location.href = intentUrl;

        setTimeout(() => {
            window.location.href = frontendUrl + '/ledger/share/' + shareId;
        }, 2000);
    } else {
        window.location.href = frontendUrl + '/ledger/share/' + shareId;
    }
</script>
</body>
</html>