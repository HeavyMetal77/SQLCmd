package controller;

import model.DBManager;
import model.JDBCDBManager;
import view.Console;
import view.View;

public class Main {
    public static void main(String[] args) {
        /*String database = "sqlcmd";
        String login = "sqlcmd";
        String password = "";*/
        DBManager dbmanager = new JDBCDBManager();
        View view = new Console();
        MainController mainController = new MainController(view, dbmanager);
        mainController.run();
    }
}
