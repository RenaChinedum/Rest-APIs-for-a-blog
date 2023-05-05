package com.task9.task9.utility;

import com.task9.task9.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionChecker {
    public static long checkUserSession(HttpServletRequest request){
        HttpSession session = request.getSession();
        long userID = (long) session.getAttribute("id");
        return userID;
    }
}
