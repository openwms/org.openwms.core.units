/*
 * Copyright 2005-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.core.units.jsr385.api;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A NumberUtils.
 *
 * @author Heiko Scherrer
 */
public final class NumberUtils {

    /**
     * Answers <code>true</code> iff the given number is an instance of
     * <code>java.math.BigDecimal</code> or <code>java.math.BigInteger</code>.
     *
     * @param number
     * @return boolean
     */
    public static boolean isBig(Number number) {
        return number instanceof BigDecimal || number instanceof BigInteger;
    }

    /**
     * Answers <code>true</code> iff the given number is an instance of
     * <code>Byte</code>, <code>Short</code>, <code>Integer</code> or <code>Long</code>.
     *
     * @param number
     * @return boolean
     */
    public static boolean isLongCompatible(Number number) {
        return number instanceof Byte || number instanceof Short || number instanceof Integer || number instanceof Long;
    }

    /**
     * Answers <code>true</code> iff the given number is an instance of
     * <code>Float</code> or <code>Double</code>.
     *
     * @param number
     * @return boolean
     */
    public static boolean isDoubleCompatible(Number number) {
        return number instanceof Float || number instanceof Double;
    }

    /**
     * Answers <code>true</code> iff the given number is infinite (i.e., is
     * a <code>Float</code> or <code>Double</code> containing one of the
     * predefined constant values representing positive or negative infinity).
     *
     * @param number
     * @return boolean
     */
    public static boolean isInfinite(Number number) {
        if (number instanceof Double && ((Double)number).isInfinite())
            return true;
        if (number instanceof Float && ((Float)number).isInfinite())
            return true;
        return false;
    }

    /**
     * Answers <code>true</code> iff the given number is 'not a number'
     * (i.e., is a <code>Float</code> or <code>Double</code> containing
     * one of the predefined constant values representing <code>NaN</code>).
     *
     * @param number
     * @return boolean
     */
    public static boolean isNaN(Number number) {
        if (number instanceof Double && ((Double)number).isNaN())
            return true;
        if (number instanceof Float && ((Float)number).isNaN())
            return true;
        return false;
    }

    /**
     * Answers the signum function of the given number
     * (i.e., <code>-1</code> if it is negative, <code>0</code>
     * if it is zero and <code>1</code> if it is positive).
     *
     * @param number
     * @return int
     * @throws ArithmeticException The given number is <code>null</code> or 'not a number'.
     */
    public static int signum(Number number) throws ArithmeticException {
        if (number == null || isNaN(number))
            throw new ArithmeticException("Argument must not be null or NaN.");

        if (isLongCompatible(number)) {
            long value = number.longValue();
            return value < 0 ? -1 : value == 0 ? 0 : 1;
        } else if (number instanceof BigInteger)
            return ((BigInteger)number).signum();
        else   if (number instanceof BigDecimal)
            return ((BigDecimal)number).signum();
        else {  // => isDoubleCompatible(number) or unknown Number type
            double value = number.doubleValue();
            return value < 0 ? -1 : value == 0 ? 0 : 1;
        }
    }

    /**
     * Converts the given number to a <code>Byte</code> (by using <code>byteValue()</code>).
     *
     * @param number
     * @return java.lang.Byte
     * @throws IllegalArgumentException The given number is 'not a number' or infinite.
     */
    public static Byte toByte(Number number) throws IllegalArgumentException {
        if (number == null || number instanceof Byte)
            return (Byte)number;
        if (isNaN(number) || isInfinite(number))
            throw new IllegalArgumentException("Argument must not be NaN or infinite.");
        return new Byte(number.byteValue());
    }

    /**
     * Converts the given number to a <code>Short</code> (by using <code>shortValue()</code>).
     *
     * @param number
     * @return java.lang.Short
     * @throws IllegalArgumentException The given number is 'not a number' or infinite.
     */
    public static Short toShort(Number number) throws IllegalArgumentException {
        if (number == null || number instanceof Short)
            return (Short)number;
        if (isNaN(number) || isInfinite(number))
            throw new IllegalArgumentException("Argument must not be NaN or infinite.");
        return new Short(number.shortValue());
    }

    /**
     * Converts the given number to a <code>Integer</code> (by using <code>intValue()</code>).
     *
     * @param number
     * @return java.lang.Integer
     * @throws IllegalArgumentException The given number is 'not a number' or infinite.
     */
    public static Integer toInteger(Number number) throws IllegalArgumentException {
        if (number == null || number instanceof Integer)
            return (Integer)number;
        if (isNaN(number) || isInfinite(number))
            throw new IllegalArgumentException("Argument must not be NaN or infinite.");
        return new Integer(number.intValue());
    }

    /**
     * Converts the given number to a <code>Long</code> (by using <code>longValue()</code>).
     *
     * @param number
     * @return java.lang.Long
     * @throws IllegalArgumentException The given number is 'not a number' or infinite.
     */
    public static Long toLong(Number number) throws IllegalArgumentException {
        if (number == null || number instanceof Long)
            return (Long)number;
        if (isNaN(number) || isInfinite(number))
            throw new IllegalArgumentException("Argument must not be NaN or infinite.");
        return new Long(number.longValue());
    }

    /**
     * Converts the given number to a <code>Float</code> (by using <code>floatValue()</code>).
     *
     * @param number
     * @return java.lang.Float
     */
    public static Float toFloat(Number number) {
        return number == null || number instanceof Float ? (Float)number : new Float(number.floatValue());
    }

    /**
     * Converts the given number to a <code>Double</code> (by using <code>doubleValue()</code>).
     *
     * @param number
     * @return java.lang.Double
     */
    public static Double toDouble(Number number) {
        return number == null || number instanceof Double ? (Double)number : new Double(number.doubleValue());
    }

    /**
     * Converts the given number to a <code>java.math.BigInteger</code>.
     *
     * @param number
     * @return java.math.BigInteger
     * @throws IllegalArgumentException The given number is 'not a number' or infinite.
     */
    public static BigInteger toBigInteger(Number number) throws IllegalArgumentException {
        if (number == null || number instanceof BigInteger)
            return (BigInteger)number;
        if (number instanceof BigDecimal)
            return ((BigDecimal)number).toBigInteger();
        if (isDoubleCompatible(number)) {
            if (isNaN(number) || isInfinite(number))
                throw new IllegalArgumentException("Argument must not be NaN or infinite.");
            return new BigDecimal(number.toString()).toBigInteger();
        } // => isLongCompatible(number) or unknown Number type
        return BigInteger.valueOf(number.longValue());
    }

    /**
     * Converts the given number to a <code>java.math.BigDecimal</code>.
     *
     * @param number
     * @return java.math.BigDecimal
     * @throws IllegalArgumentException The given number is 'not a number' or infinite.
     */
    public static BigDecimal toBigDecimal(Number number) throws IllegalArgumentException {
        if (number == null || number instanceof BigDecimal)
            return (BigDecimal)number;
        if (number instanceof BigInteger)
            return new BigDecimal((BigInteger)number);
        if (isDoubleCompatible(number)) {
            if (isNaN(number) || isInfinite(number))
                throw new IllegalArgumentException("Argument must not be NaN or infinite.");
            return new BigDecimal(number.toString());
        }
        if (isLongCompatible(number))
            return BigDecimal.valueOf(number.longValue());
        // => unknown Number type
        return new BigDecimal(String.valueOf(number.doubleValue()));
    }

    /**
     * Compares the first number to the second one numerically and
     * returns an integer depending on the comparison result:
     * a negative value if the first number is the smaller one,
     * a zero value if they are equal, and
     * a positive value if the first number is the larger one.
     *
     * The main strategy goes like follows:
     * 1. If one of the arguments is <code>null</code> or 'not a number',
     *    throw an exception.
     * 2. If both values are 'long compatible', compare their <code>longValue()</code>
     *    using the usual comparison operators for primitive types (&lt;, ==, &gt;).
     * 3. If both values are 'double compatible', compare their <code>doubleValue()</code>
     *    using the usual comparison operators for primitive types (&lt;, ==, &gt;).
     * 4. If one of the values is infinite (and the other is finite),
     *    determine the result depending on the sign of the infinite value.
     * 5. Otherwise convert both values to <code>java.math.BigDecimal</code> and
     *    return the result of the <code>BigDecimal.compareTo(BigDecimal)</code> method.
     *
     * As a consequence, the method is not suitable to implement a
     * <code>java.util.Comparator</code> for numbers. To achieve this,
     * one had to accept 'not a number' arguments and place them somewhere
     * in the row of numbers (probably at the upper end, i.e. larger than
     * positive infinity, as <code>Double.compare(double, double)</code>
     * does it).
     * So the behavior of this method is like that of the comparison
     * operator for primitive types and not like that of the related
     * <code>compareTo(...)</code> methods. Besides the handling of
     * 'not a number' values this makes a difference, when comparing
     * the float or double values <code>-0.0</code> and <code>0.0</code>:
     * again, like the operators, we consider them as equal (whereas
     * according to <code>Double.compareTo(...)</code> <code>-0.0</code>
     * is less than <code>0.0</code>).
     *
     * @param first
     * @param second
     * @return int
     * @throws ArithmeticException One or both of the given numbers is <code>null</code> or 'not a number'.
     */
    public static int compare(Number first, Number second) throws ArithmeticException {
        if (first == null || second == null || isNaN(first) || isNaN(second))
            throw new ArithmeticException("Arguments must not be null or NaN.");

        int result = -2;

        if (isLongCompatible(first) && isLongCompatible(second)) {
            long v1 = first.longValue(), v2 = second.longValue();
            result = v1 < v2 ? -1 : v1 == v2 ? 0 : v1 > v2 ? 1 : 2;
        } else if (isDoubleCompatible(first) && isDoubleCompatible(second)) {
            double v1 = first.doubleValue(), v2 = second.doubleValue();
            result = v1 < v2 ? -1 : v1 == v2 ? 0 : v1 > v2 ? 1 : 2;
        }

        if (result == 2)    // should not happen
            throw new ArithmeticException("Arguments " + first + " and " + second + " are not comparable.");
        if (result > -2)
            return result;

        if (isInfinite(first))    // => second is finite
            return first.doubleValue() == Double.NEGATIVE_INFINITY ? -1 : 1;
        if (isInfinite(second))   // => first is finite
            return second.doubleValue() == Double.POSITIVE_INFINITY ? -1 : 1;

        return toBigDecimal(first).compareTo(toBigDecimal(second));
    }
}
