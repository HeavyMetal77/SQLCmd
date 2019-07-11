package controller.web;

import service.Service;
import service.ServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

public class MainServlet extends HttpServlet {
    private Service service;

    @Override
    public void init() throws ServletException {
        super.init();
        service = new ServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);
        if (req.getSession().getAttribute("connection") == null || action.startsWith("/connect")) {
            req.getRequestDispatcher("connect.jsp").forward(req, resp);
        }
        req.setAttribute("list", service.commands());
        if (action.startsWith("/menu") || action.equals("/")) {
            req.getRequestDispatcher("menu.jsp").forward(req, resp);
        } else if (action.startsWith("/help")) {
            req.getRequestDispatcher("help.jsp").forward(req, resp);
        } else if (action.startsWith("/find")) {
            req.getRequestDispatcher("find.jsp").forward(req, resp);
        } else if (action.startsWith("/clear")) {
            req.getRequestDispatcher("clear.jsp").forward(req, resp);
        }else if (action.startsWith("/delete")) {
            req.getRequestDispatcher("delete.jsp").forward(req, resp);
        }else if (action.startsWith("/drop")) {
            req.getRequestDispatcher("drop.jsp").forward(req, resp);
        } else if (action.startsWith("/tables")) {
            service.tables();
            req.setAttribute("listtable", service.tables());
            req.getRequestDispatcher("tables.jsp").forward(req, resp);
        }else {
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);
        if (action.startsWith("/connect")) {
            String databaseName = req.getParameter("dbname");
            String userName = req.getParameter("username");
            String password = req.getParameter("password");
            try {
                Connection connection = service.connect(databaseName, userName, password);
                req.getSession().setAttribute("connection", connection);
                resp.sendRedirect(resp.encodeRedirectURL("menu"));
            } catch (Exception e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        } else if (action.startsWith("/find")) {
            String nameTable = req.getParameter("nameTable");
            req.setAttribute("listdataset", service.find(nameTable));
            req.getRequestDispatcher("findResult.jsp").forward(req, resp);
        } else if (action.startsWith("/clear")) {
            String tableName = req.getParameter("nameTable");
            service.clear(tableName);
            req.setAttribute("listdataset", service.find(tableName));
            req.getRequestDispatcher("findResult.jsp").forward(req, resp);
        }else if (action.startsWith("/delete")) {
            String tableName = req.getParameter("nameTable");
            String columnName = req.getParameter("columnName");
            String columnValue = req.getParameter("columnValue");
            service.delete(tableName, columnName, columnValue);
            req.setAttribute("listdataset", service.find(tableName));
            req.getRequestDispatcher("findResult.jsp").forward(req, resp);
        }else if (action.startsWith("/drop")) {
            String tableName = req.getParameter("nameTable");
            service.drop(tableName);
            req.setAttribute("listtable", service.tables());
            req.getRequestDispatcher("tables.jsp").forward(req, resp);
        }
    }
}
