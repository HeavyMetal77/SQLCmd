package controller.command;

import model.DBManager;
import view.View;

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
        view.write(dbManager.getTables().toString());
    }
}
