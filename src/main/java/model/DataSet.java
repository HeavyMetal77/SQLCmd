package model;

import java.util.Arrays;

public class DataSet {

    static class Data{
        private String name;
        private Object value;

        public Data(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    public Data[] data = new Data[100];
    //переменная хранит значение количества элементов в датасете
    public int indexElement = 0;

    public void put(String name, Object value) {
        //ищем совпадение по имени атрибута, если есть - присваиваем value
        for (int index = 0; index < indexElement; index++) {
            if (data[index].getName().equals(name)) {
                data[index].value = value;
                return;
            }
        }
        //если нет совпадения - создаем новый элемент Data в массиве
        data[indexElement++] = new Data(name, value);
    }

    public Object[] getValues() {
        Object[] result = new Object[indexElement];
        for (int i = 0; i < indexElement; i++) {
            result[i] = data[i].getValue();
        }
        return result;
    }

    public String[] getNames() {
        String[] result = new String[indexElement];
        for (int i = 0; i < indexElement; i++) {
            result[i] = data[i].getName();
        }
        return result;
    }

    public Object get(String name) {
        for (int i = 0; i < indexElement; i++) {
            if (data[i].getName().equals(name)) {
                return data[i].getValue();
            }
        }
        return null;
    }

    public void updateFrom(DataSet newValue) {
        for (int index = 0; index < newValue.indexElement; index++) {
            Data data = newValue.data[index];
            this.put(data.name, data.value);
        }
    }

    @Override
    public String toString() {
        return "\t" + "{" +
                "names:" + Arrays.toString(getNames()) + ", " +
                "values:" + Arrays.toString(getValues()) +
                "}" + "\n";
    }
}
