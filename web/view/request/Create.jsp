<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Tạo đơn xin nghỉ phép</title>
        <style>
            body {
                font-family: "Segoe UI", Arial, sans-serif;
                background-color: #f7f8fa;
                margin: 0;
                padding: 0;
            }

            .container {
                width: 420px;
                margin: 60px auto;
                background: #ffffff;
                padding: 25px 30px;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            }

            h2 {
                text-align: center;
                color: #2c3e50;
                font-size: 22px;
                margin-bottom: 18px;
            }

            form label {
                display: block;
                margin-bottom: 5px;
                color: #34495e;
                font-size: 14px;
            }

            input[type="text"],
            input[type="date"],
            input[type="email"],
            select,
            textarea {
                width: 100%;
                padding: 8px 10px;
                margin-bottom: 12px;
                border: 1px solid #ccc;
                border-radius: 6px;
                font-size: 14px;
                box-sizing: border-box;
                resize: vertical;
            }

            input[type="checkbox"],
            input[type="radio"] {
                transform: scale(0.9);
                margin-right: 5px;
            }

            .checkbox-label {
                font-size: 13px;
                color: #2c3e50;
                display: flex;
                align-items: center;
                margin-bottom: 15px;
            }

            .buttons {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 15px;
                margin-top: 10px;
            }

            button,
            .btn {
                flex: 1;
                text-align: center;
                padding: 9px 0;
                border: none;
                border-radius: 6px;
                font-size: 14px;
                cursor: pointer;
                transition: background 0.2s, transform 0.1s;
                text-decoration: none; /* bỏ gạch chân cho link */
            }

            .btn-primary {
                background-color: #007bff;
                color: #fff;
            }

            .btn-primary:hover {
                background-color: #0069d9;
                transform: translateY(-1px);
            }

            .btn-secondary {
                background-color: #6c757d;
                color: #fff;
            }

            .btn-secondary:hover {
                background-color: #5a6268;
                transform: translateY(-1px);
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Tạo đơn xin nghỉ phép</h2>

            <form action="${pageContext.request.contextPath}/request/create" method="post">
                <label for="from">Từ ngày:</label>
                <input type="date" id="from" name="from" required>

                <label for="to">Đến ngày:</label>
                <input type="date" id="to" name="to" required>

                <label for="reason">Lý do nghỉ:</label>
                <textarea id="reason" name="reason" rows="3" placeholder="Nhập lý do nghỉ..." required></textarea>

                <label class="checkbox-label">
                    <input type="checkbox" name="online" value="true"> Làm việc online
                </label>

                <div class="buttons">
                    <button type="submit" name="action" value="save" class="btn btn-primary">Lưu</button>               
                    <a href="${pageContext.request.contextPath}/request/list" class="btn btn-secondary">Quay lại danh sách</a>
                </div>
            </form>
        </div>
    </body>
</html>
