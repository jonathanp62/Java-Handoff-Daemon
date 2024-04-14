package net.jmp.handoff.daemon;

/*
 * (#)ResponseCode.java 0.4.0   04/13/2024
 *
 * @author    Jonathan Parker
 * @version   0.4.0
 * @since     0.4.0
 *
 * MIT License
 *
 * Copyright (c) 2024 Jonathan M. Parker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

enum ResponseCode {
    OK      (Constants.OK,      Constants.OK),
    NOT_OK  (Constants.NOT_OK,  Constants.NOT_OK);

    /** The identifier of the enumerated value. */
    private final String value;

    /** A description of the enumerated value. */
    private final String descriptor;

    /**
     * Constructor that takes the descriptor.
     *
     * @param   value       java.lang.String
     * @param   descriptor  java.lang.String
     */
    private ResponseCode(final String value, final String descriptor) {
        this.value = value;
        this.descriptor = descriptor;
    }

    String getValue() {
        return this.value;
    }

    String getDescriptor() {
        return this.descriptor;
    }

    /**
     * The overridden to-string method.
     *
     * @return  java.lang.String
     */
    @Override
    public String toString() {
        return this.descriptor;
    }

    /**
     * A static inner class of constants.
     */
    static class Constants {
        static final String OK     = "OK";
        static final String NOT_OK = "Not OK";

        private Constants() {
            super();
        }
    }
}
