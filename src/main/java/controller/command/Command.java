package controller.command;

public interface Command {
    //проверка можна ли обработать строку
    boolean canProcess(String command);

    //обработать строку
    void process(String command) throws Exception;
}
