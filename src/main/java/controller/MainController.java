package controller;

import controller.command.*;
import model.DBManager;
import view.View;

public class MainController {
    private View view;
    private Command[] commands;

    public MainController(View view, DBManager dbManager) {
        this.view = view;
        this.commands = new Command[]{
                new Connect(dbManager, view),
                new Help(view),
                new Exit(view),
                new IsConnected(dbManager, view),
                new Tables(dbManager, view),
                new Find(dbManager, view),
                new CreateTable(dbManager, view),
                new Insert(dbManager, view),
                new Drop(dbManager, view),
                new Clear(dbManager, view),
                new Unsupported(view)};
    }

    public void run() {
        view.write("Привет пользователь!");
        view.write("Введи, пожалуйста, имя базы данных, имя пользователя и пароль в формате: " +
                "connect|database|user|password");
        while (true) {
            String input = view.read();
            for (Command command: commands) {
                if (command.canProcess(input)) {
                    command.process(input);
                    break;
                }
            }
            view.write("Введи команду или 'help' для помощи:");
        }
    }
}
