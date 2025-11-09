<%-- 
    Document   : login
    Created on : Oct 18, 2025, 11:09:21 AM
    Author     : sonnt
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(135deg, #6a11cb, #2575fc);
                height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
                margin: 0;
            }
            .login-container {
                background-color: white;
                padding: 40px 35px;
                border-radius: 15px;
                box-shadow: 0 5px 25px rgba(0, 0, 0, 0.2);
                width: 350px;
                text-align: center;
                transition: all 0.3s ease;
            }
            .login-container:hover {
                transform: translateY(-3px);
                box-shadow: 0 8px 30px rgba(0, 0, 0, 0.25);
            }
            h2 {
                margin-bottom: 25px;
                color: #333;
            }
            .form-group {
                margin-bottom: 20px;
                text-align: left;
            }
            label {
                display: block;
                font-weight: bold;
                color: #555;
                margin-bottom: 5px;
            }
            input[type="text"],
            input[type="password"] {
                width: 100%;
                padding: 10px 12px;
                border: 1px solid #ccc;
                border-radius: 8px;
                font-size: 14px;
                box-sizing: border-box;
                transition: border-color 0.3s;
            }
            input[type="text"]:focus,
            input[type="password"]:focus {
                outline: none;
                border-color: #2575fc;
                box-shadow: 0 0 5px rgba(37, 117, 252, 0.3);
            }
            #btnLogin {
                width: 100%;
                padding: 10px 0;
                background: linear-gradient(90deg, #2575fc, #6a11cb);
                color: white;
                font-size: 16px;
                font-weight: bold;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                transition: background 0.3s ease, transform 0.1s;
            }
            #btnLogin:hover {
                background: linear-gradient(90deg, #6a11cb, #2575fc);
            }
            #btnLogin:active {
                transform: scale(0.98);
            }
            .footer-text {
                margin-top: 15px;
                font-size: 13px;
                color: #777;
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <h2>Đăng nhập hệ thống</h2>
            <form action="${pageContext.request.contextPath}/login" method="POST">
                <div class="form-group">
                    <label for="txtUsername">Tên đăng nhập</label>
                    <input type="text" name="username" id="txtUsername" required/>
                </div>
                <div class="form-group">
                    <label for="txtPassword">Mật khẩu</label>
                    <input type="password" name="password" id="txtPassword" required/>
                </div>
                <input type="submit" id="btnLogin" value="Đăng nhập"/>
            </form>
            <div class="footer-text">
                © 2025 - Secure Login Page
            </div>
        </div>
    </body>
</html>
