<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Tạo đơn xin nghỉ phép - Hệ thống quản lý nghỉ phép</title>
        <style>
            body {
                font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', sans-serif;
                font-size: 16px;
                line-height: 1.6;
                font-weight: 400;
                letter-spacing: 0.2px;
                -webkit-font-smoothing: antialiased;
                -moz-osx-font-smoothing: grayscale;
                background: #f4f6f9;
                margin: 0;
                padding: 0;
            }


            .header {
                background: #1a73e8;
                color: white;
                padding: 15px 40px;
                font-size: 22px;
                font-weight: 600;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            }

            .container {
                max-width: 550px;
                background: white;
                margin: 60px auto;
                padding: 40px 50px;
                border-radius: 16px;
                box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
            }

            h2 {
                text-align: center;
                color: #333;
                margin-bottom: 30px;
            }

            label {
                display: block;
                margin-bottom: 6px;
                font-weight: 500;
                color: #333;
            }

            input[type="date"],
            textarea {
                width: 100%;
                padding: 2 5px; /* Giảm padding để sát lề trái và phải */
                margin-bottom: 20px;
                border: 1px solid #ccc;
                border-radius: 8px;
                box-sizing: border-box;
                font-size: 15px;
                resize: none;
                line-height: 1.5;
                transition: border-color 0.3s;
            }

            input[type="date"]:focus,
            textarea:focus {
                border-color: #1a73e8;
                outline: none;
                box-shadow: 0 0 6px rgba(26,115,232,0.3);
            }

            input[type="submit"] {
                background-color: #1a73e8;
                color: white;
                border: none;
                padding: 12px;
                border-radius: 8px;
                width: 100%;
                font-size: 16px;
                font-weight: 600;
                cursor: pointer;
                transition: background 0.3s;
            }

            input[type="submit"]:hover {
                background-color: #155fc1;
            }

            .message {
                text-align: center;
                color: green;
                margin-top: 15px;
                font-weight: 500;
            }

            .back-link {
                display: block;
                text-align: center;
                margin-top: 25px;
                text-decoration: none;
                color: #1a73e8;
                font-weight: 500;
                transition: color 0.3s;
            }

            .back-link:hover {
                color: #0b56a2;
            }

            footer {
                text-align: center;
                color: #777;
                margin-top: 40px;
                font-size: 13px;
            }
        </style>
    </head>
    <body>
        <div class="header">
            Hệ thống quản lý nghỉ phép - Công ty TNHH ABC
        </div>

        <div class="container">
            <h2>Tạo đơn xin nghỉ phép</h2>

            <form action="${pageContext.request.contextPath}/create" method="post">
                <label for="from">Từ ngày:</label>
                <input type="date" name="from" id="from" required>

                <label for="to">Đến ngày:</label>
                <input type="date" name="to" id="to" required>

                <label for="reason">Lý do nghỉ phép:</label>
                <textarea name="reason" id="reason" rows="4" required placeholder="Nhập lý do xin nghỉ..."></textarea>

                <input type="submit" value="Gửi đơn">
            </form>

            <p class="message">${message}</p>

            <a href="${pageContext.request.contextPath}/home" class="back-link">← Quay lại trang chủ</a>
        </div>

        <footer>
