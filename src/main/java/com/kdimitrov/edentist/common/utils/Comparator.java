package com.kdimitrov.edentist.common.utils;

import com.kdimitrov.edentist.common.models.Appointment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Comparator {

    public static List<Field> compare(Appointment f, Appointment s) throws IllegalAccessException {
        List<Field> result = new ArrayList<>();

        Field[] f_fields = f.getClass().getDeclaredFields();
        Field[] s_fields = s.getClass().getDeclaredFields();

        for (int i = 0; i < f_fields.length; i++) {
            f_fields[i].setAccessible(true);
            s_fields[i].setAccessible(true);
            if (f_fields[i].get(f) != s_fields[i].get(s)) {
                result.add(f_fields[i]);
            }
        }

        return result;
    }
}
