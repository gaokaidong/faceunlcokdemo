package com.colorreco.utils;

/**
 * 数字比较帮助类
 */
public class MathUtils
{
    public static float getMaxValue(float value1, float value2, float value3)
    {
        float maxValue = Math.max(value1, value2);
        maxValue = Math.max(maxValue, value3);

        return maxValue;
    }

    /**
     * 将float[]转成String
     *
     * @param floatValue
     * @return
     */
    public static String intToString(int [] floatValue)
    {
        if (floatValue == null || floatValue.length == 0) return null;

        StringBuilder builder = new StringBuilder();

        for (int value : floatValue)
        {
            builder.append(value+",");
        }

        return builder.toString();
    }
}
