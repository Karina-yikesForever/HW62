package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

 class Generic {
    public static void main(String[] args) {
        System.out.println(new Value<>(2.));
        System.out.println(new Value<>(2));
        System.out.println(new Value<>(2f));

        Numbers numbers = new Numbers();
        Double[] arrayDouble = {1.1, 2.2, 3.3, 4.4};
        Integer[] arrayInteger = {1, 2, 3, 4, 5};
        System.out.println("avg double array = " + numbers.getAvg(arrayDouble));
        System.out.println("avg integer array = " + numbers.getAvg(arrayInteger));

        List<Double> listDouble = new ArrayList<>(Arrays.asList(arrayDouble));
        List<Integer> listInteger = new ArrayList<>(Arrays.asList(arrayInteger));
        System.out.println("Sum list double = " + numbers.getSumList(listDouble));
        System.out.println("Sum list integer = " + numbers.getSumList(listInteger));
    }
}

class Numbers {
    public Number getSumList(List<? extends Number> list) {
        Number result = 0;
        if (list != null) {
            result = getSumArray(list.toArray(new Number[0]));
        }
        return result;
    }

    private <E extends Number> E getSumArray(E[] array) {
        E result = null;
        if (array != null) {
            Value<E> value = null;
            for (E element : array) {
                value = new Value<>(element).getSum(value);
            }
            if (value != null) {
                result = value.getValue();
            }
        }
        return result;
    }

    public <E extends Number> Double getAvg(E[] array) {
        Double result = 0.;
        if (array != null) {
            result = getSumArray(array).doubleValue() / array.length;
        }
        return result;
    }
}

class Value<E extends Number> {
    private E value;
    private TypeNumber key;

    public E getValue() {
        return value;
    }

    private void setKey(TypeNumber key) {
        if (key == null) {
            throw new IllegalArgumentException(String.format("Incorrect type for class Value. Correct types: %s;",
                    Arrays.toString(TypeNumber.getTypesToString())));
        }
        this.key = key;
    }

    Value(E value) {
        this.setKey(TypeNumber.getType(value));
        this.value = value;
    }

    public Value<E> getSum(Value<E> value) {
        return new Value<>(this.key.getSum(this.value, value != null ? value.value : null));
    }

    @Override
    public String toString() {
        return String.format("type: %s; value: %s", this.key.toString(), this.value);
    }

    enum TypeNumber {
        INTEGER {
            @Override
            public <E extends Number> E getSum(E first, E second) {
                int a = first != null ? first.intValue() : 0;
                int b = second != null ? second.intValue() : 0;
                return (E) new Integer(a + b);
            }
        }, DOUBLE {
            @Override
            public <E extends Number> E getSum(E first, E second) {
                double a = first != null ? first.doubleValue() : 0.;
                double b = second != null ? second.doubleValue() : 0.;
                return (E) new Double(a + b);
            }
        }, FLOAT {
            @Override
            public <E extends Number> E getSum(E first, E second) {
                float a = first != null ? first.floatValue() : 0f;
                float b = second != null ? second.floatValue() : 0f;
                return (E) new Float(a + b);
            }
        };

        public abstract <E extends Number> E getSum(E first, E second);

        public static <E> TypeNumber getType(E value) {
            TypeNumber result = null;
            if (value != null) {
                String str = value.getClass().getSimpleName().toUpperCase();
                for (TypeNumber type : TypeNumber.values()) {
                    if (str.equals(type.name())) {
                        result = type;
                        break;
                    }
                }
            }
            return result;
        }

        public static String[] getTypesToString() {
            String[] array = new String[TypeNumber.values().length];
            int index = 0;
            for (TypeNumber type : TypeNumber.values()) {
                array[index++] = type.toString();
            }
            return array;
        }


        @Override
        public String toString() {
            return String.format("%s%s",
                    Character.toUpperCase(this.name().charAt(0)),
                    this.name().substring(1).toLowerCase());
        }
    }
}