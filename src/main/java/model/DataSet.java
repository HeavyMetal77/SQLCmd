package model;

import java.util.TreeMap;

public class DataSet {

        private String name;
        private Object value;

        public DataSet(String name, Object value) {
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
