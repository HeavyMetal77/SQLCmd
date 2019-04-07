package controller.command;

import model.DBManager;
import view.View;

public class Drop implements Command {
    private DBManager dbManager;
    private View view;

    public Drop(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("drop|");
    }

    @Override
    public void process(String command) {
        String[] commandWithParam = command.split("[|]");
        try {
            if (commandWithParam.length == 2) {
                dbManager.drop(commandWithParam[1]);
                view.write("TABLE " + commandWithParam[1] + " was successfully deleted!");

            } else {
                throw new IllegalArgumentException("Количество параметров не соответствует шаблону!");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
