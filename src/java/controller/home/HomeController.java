package controller;

import dal.FeatureDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import model.iam.Feature;
import model.iam.User;

@WebServlet(name = "HomeController", urlPatterns = {"/home"})
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // không tạo session mới
        User user = (User) (session != null ? session.getAttribute("auth") : null);

        if (user == null) {
            // ⚠️ Nếu chưa login hoặc session hết hạn
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        FeatureDAO fdao = new FeatureDAO();
        ArrayList<Feature> features = (ArrayList<Feature>) fdao.getFeaturesByUser(user.getId());
        request.setAttribute("features", features);

        request.getRequestDispatcher("Home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
