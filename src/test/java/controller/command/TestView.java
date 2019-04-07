package controller.command;

import view.View;

public class TestView implements View {

    private String massages = "";
    private String input = null;

    @Override
    public void write(String massage) {
        massages += massage + "\n";
    }

    @Override
    public String read() {
        if (this.input == null) {
            throw new IllegalStateException("Для работы проинициализируйте метод read()");
        }
        String result = this.input;
        this.input = null;
        return result;
    }

    public void addRead(String input) {
        this.input = input;
    }

    public String getContent(){
        return massages;
    }
}
