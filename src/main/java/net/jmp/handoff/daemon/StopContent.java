package net.jmp.handoff.daemon;

/*
 * (#)StopContent.java  0.6.0   04/20/2024
 *
 * @author    Jonathan Parker
 * @version   0.6.0
 * @since     0.6.0
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

import com.google.gson.annotations.SerializedName;

final class StopContent extends Content {
    /** The type. It is typically 'Stop'. */
    @SerializedName("type")
    private final String type;

    /** The daemon's process identifier. */
    @SerializedName("pid")
    private long pid;

    /** The message. */
    @SerializedName("message")
    private String message;

    /**
     * The default constructor.
     */
    StopContent() {
        super();

        this.type = "Stop";
    }

    /**
     * Get the type.
     *
     * @return  java.lang.String
     */
    String getType() {
        return this.type;
    }

    /**
     * Get the daemon's process identifier.
     *
     * @return  long
     */
    long getPid() {
        return this.pid;
    }

    /**
     * Set the daemon's process identifier.
     *
     * @param   pid long
     */
    void setPid(final long pid) {
        this.pid = pid;
    }

    /**
     * Get the message.
     *
     * @return  java.lang.String
     */
    String getMessage() {
        return this.message;
    }

    /**
     * Set the message.
     *
     * @param   message java.lang.String
     */
    void setMessage(final String message) {
        this.message = message;
    }
}
