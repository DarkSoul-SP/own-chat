package ua.darksoul.testprojects.ownchat.controller;/*
 *
 *@author DarkSoul
 */

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");

        return String.format(
                    "<html>"
                      + "<body style=\"background: url(/static/images/errorArt.jpg) fixed; color: white; font-size: 20px\">"
                        + " <h2>You have opened unexpected page. <br> Press the button for redirect to main page.</h2> "
                        + " <div style=\" background-color: dimgray; border-radius: 10px; border-color: #007bff; "
                          + " height: 25px; width: 90px; padding: 20px; margin-left: 10px\"> "
                          + " <a style=\"text-decoration:none; color: white; height: 25px; width: 90px\" href=\"/main\"> "
                          + " Click here</a> "
                        + " </div> <br> "
                        + " <div>Status code: <b>%s</b></div> "
                        + " <div>Exception Message: <b>%s</b></div> "
                      + "</body>"
                    + "</html>",
                statusCode, exception == null ? "N/A" : exception.getMessage());
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
