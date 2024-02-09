package com.aljun.zombiegame.work.datamanager.datamanager;

import com.aljun.zombiegame.work.keyset.KeySet;
import net.minecraft.nbt.*;

public class DataManager {
    public static <V> V getOrDefault(CompoundNBT tag, String key, V value) {

        if (!tag.contains(key)) {
            return value;
        }

        V v;

        if (value instanceof Integer) {

            try {
                v = (V) Integer.valueOf(tag.getInt(key));
            } finally {
            }

        } else if (value instanceof Boolean) {
            try {
                v = (V) Boolean.valueOf(tag.getBoolean(key));
            } finally {
            }
        } else if (value instanceof String) {
            try {
                v = (V) tag.getString(key);
            } finally {
            }
        } else if (value instanceof Short) {
            try {
                v = (V) Short.valueOf(tag.getShort(key));
            } finally {
            }
        } else if (value instanceof Double) {
            try {
                v = (V) Double.valueOf(tag.getDouble(key));
            } finally {
            }
        } else if (value instanceof Float) {
            try {
                v = (V) Float.valueOf(tag.getFloat(key));
            } finally {
            }
        } else if (value instanceof Long) {
            try {
                v = (V) Long.valueOf(tag.getLong(key));
            } finally {
            }
        } else {
            throw new IllegalArgumentException("getOrCreate<V>(CompoundNBT tag, String key, V value) : \""
                                               + value.getClass().getName()
                                               + "\" is "
                                               + "not allowed.\n"
                                               + "Integer, Short, Long, Float, Double, Boolean and String are "
                                               + "permitted.");
        }

        return v;


    }

    public static <V> V getOrDefault(CompoundNBT tag, KeySet<V> keySet) {
        return getOrDefault(tag, keySet.KEY, keySet.DEFAULT_VALUE);
    }

    public static <V> V getOrCreate(CompoundNBT tag, String key, V value) {

        checkToCreate(tag, key, value);
        V v;

        if (value instanceof Integer) {

            try {
                v = (V) Integer.valueOf(tag.getInt(key));
            } finally {
            }

        } else if (value instanceof Boolean) {
            try {
                v = (V) Boolean.valueOf(tag.getBoolean(key));
            } finally {
            }
        } else if (value instanceof String) {
            try {
                v = (V) tag.getString(key);
            } finally {
            }
        } else if (value instanceof Short) {
            try {
                v = (V) Short.valueOf(tag.getShort(key));
            } finally {
            }
        } else if (value instanceof Double) {
            try {
                v = (V) Double.valueOf(tag.getDouble(key));
            } finally {
            }
        } else if (value instanceof Float) {
            try {
                v = (V) Float.valueOf(tag.getFloat(key));
            } finally {
            }
        } else if (value instanceof Long) {
            try {
                v = (V) Long.valueOf(tag.getLong(key));
            } finally {
            }
        } else {
            throw new IllegalArgumentException("getOrCreate<V>(CompoundNBT tag, String key, V value) : \""
                                               + value.getClass().getName()
                                               + "\" is "
                                               + "not allowed.\n"
                                               + "Integer, Short, Long, Float, Double, Boolean and String are "
                                               + "permitted.");
        }

        return v;


    }

    public static <V> V getOrCreate(CompoundNBT tag, KeySet<V> keySet) {
        return getOrCreate(tag, keySet.KEY, keySet.DEFAULT_VALUE);
    }

    public static <V> void set(CompoundNBT tag, String key, V value) {
        tag.put(key, toTag(value));
    }

    public static <V> void set(CompoundNBT tag, KeySet<V> keySet, V value) {
        set(tag, keySet.KEY, value);
    }

    public static <V> void checkToCreate(CompoundNBT tag, String key, V value) {
        if (!tag.contains(key)) {
            set(tag, key, value);
        }
    }

    public static <V> void checkToCreate(CompoundNBT tag, KeySet<V> keySet) {
        checkToCreate(tag, keySet.KEY, keySet.DEFAULT_VALUE);
    }

    public static <V> INBT toTag(V value) {
        if (value instanceof Integer) {
            return IntNBT.valueOf((Integer) value);
        } else if (value instanceof Boolean) {
            return ByteNBT.valueOf((Boolean) value);
        } else if (value instanceof String) {
            return StringNBT.valueOf((String) value);
        } else if (value instanceof Short) {
            return ShortNBT.valueOf((Short) value);
        } else if (value instanceof Double) {
            return DoubleNBT.valueOf((Double) value);
        } else if (value instanceof Float) {
            return FloatNBT.valueOf((Float) value);
        } else if (value instanceof Long) {
            return LongNBT.valueOf((Long) value);
        } else {
            throw new IllegalArgumentException("toTag(V value) : \""
                                               + value.getClass().getName()
                                               + "\" is not allowed.\n"
                                               + "Integer, Short, Long,"
                                               + " Float, Double, Boolean and String are permitted.");

        }
    }
}
