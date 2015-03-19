package be.fluid_it.tools.dw.wiz2war.ci.webapp.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="checkLoggingServlet", urlPatterns={"/check/logging"})
public class CheckSlf4JLoggerFactoryServlet extends HttpServlet {
    private Logger logger = LoggerFactory.getLogger(getClass());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Executing get ...");
        PrintWriter out = response.getWriter();
        out.println("LoggerFactory type = " + StaticLoggerBinder.getSingleton().getLoggerFactory().getClass().getName());
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

}