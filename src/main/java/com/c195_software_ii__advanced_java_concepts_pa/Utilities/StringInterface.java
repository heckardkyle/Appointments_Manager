package com.c195_software_ii__advanced_java_concepts_pa.Utilities;

/**
 * Functional interface for generating a string with an Int parameter.
 * @author Kyle Heckard
 * @version 1.0
 */
public interface StringInterface {
    /**
     * Lambda 2, generates string from int parameter.
     * Justification for Lambda is this method functions as a flexible general purpose method for creating strings from
     * single integer parameters. This is helpful for portions of code that reuse the same string but with varying
     * integer values.
     * @param n integer
     * @return new String
     */
    String stringFromInt(int n);
}
