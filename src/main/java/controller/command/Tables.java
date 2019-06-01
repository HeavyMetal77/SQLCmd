package controller.command;

import model.DBManager;
import view.View;

import java.sql.SQLException;

public class Tables implements Command {

    private View view;
    private DBManager dbManager;

    public Tables(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("tables");
    }

    @Override
    public void process(String command) {
        try {
            view.write(dbManager.getTables().toString());
        } catch (SQLException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public String formatCommand() {
        return "tables";
    }

    @Override
    public String describeCommand() {
        return "Вывод списка всех таблиц";
    }
}
