package com.project.jeeproject_springboot.util;

import com.project.jeeproject_springboot.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ServletUtil {

    public static String defaultErrorPage = "WEB-INF/commonPages/error.jsp";

    public static String getResultPage(HttpServletRequest req, String defaultForwardPage) {
        return (req.getParameter("result-page") != null) ? req.getParameter("result-page") : defaultForwardPage;
    }

    public static void forward(HttpServletRequest req, HttpServletResponse resp, String forwardPage, String errorPage, String errorMessage) throws ServletException, IOException {
        if (errorMessage==null) {
            if (req.getParameter("successMessage")!=null) {
                req.setAttribute("successMessage", req.getParameter("successMessage"));
            }
            if (req.getParameter("errorMessage")!=null) {
                req.setAttribute("errorMessage", req.getParameter("errorMessage"));
            }
            req.getRequestDispatcher(forwardPage).forward(req, resp);
        } else {
            String encodedMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
            errorPage += (errorPage.contains("?"))? "&": "?";
            errorPage += "errorMessage=" + encodedMessage;
            resp.sendRedirect(errorPage);
            /*req.setAttribute("errorMessage", errorMessage);
            req.getRequestDispatcher(errorPage).forward(req, resp);*/
        }
    }

    public static String redirect(Model model, String resultPage) {
        if (model.getAttribute("successMessage")!=null) {
            String successMessage = (String) model.getAttribute("successMessage");
            String encodedMessage = URLEncoder.encode(successMessage, StandardCharsets.UTF_8);
            resultPage += (resultPage.contains("?"))? "&": "?";
            resultPage += "successMessage=" + encodedMessage;
        }
        if (model.getAttribute("errorMessage")!=null) {
            String errorMessage = (String) model.getAttribute("errorMessage");
            String encodedMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
            resultPage += (resultPage.contains("?"))? "&": "?";
            resultPage += "errorMessage=" + encodedMessage;
        }
        return "redirect:" + resultPage;
    }

    public static String unauthorized(){
        return "/commonPages/unauthorized";
    }

    public static boolean validString(String str) {
        return (str!=null && !str.isEmpty() && str.length()<100);
    }

    public static boolean notAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");
        return user == null || !user.getRole().equals("admin");
    }

    public static boolean notProfessor(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");
        return user == null || !user.getRole().equals("professor");
    }

    public static boolean notStudent(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");
        return user == null || !user.getRole().equals("student");
    }




}
