package controller.command;

import model.DBManager;
import view.View;

public class Find implements Command {
    private DBManager dbManager;
    private View view;

    public Find(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {
        String[] commandWithParam = command.split("[|]");
        try {
            if (commandWithParam.length == 2) {
                dbManager.find(commandWithParam[1]);
            } else {
                throw new IllegalArgumentException("Количество параметров не соответствует шаблону!");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
