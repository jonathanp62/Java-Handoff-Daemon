package net.jmp.handoff.daemon;

/*
 * (#)Config.java   0.4.0   04/12/2024
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

import com.google.gson.annotations.SerializedName;

/**
 * The configuration class.
 */
public class Config {
    /** The host name. */
    @SerializedName("hostname")
    private String hostName;

    /** The port. */
    @SerializedName("port")
    private int port;

    /**
     * Get the host name.
     *
     * @return  java.lang.String
     */
    String getHostName() {
        return this.hostName;
    }

    /**
     * Set the host name.
     *
     * @param   hostName    java.lang.String
     */
    void setHostName(final String hostName) {
        this.hostName = hostName;
    }

    /**
     * Get the port.
     *
     * @return  int
     */
    int getPort() {
        return this.port;
    }

    /**
     * Set the port.
     *
     * @param   port    int
     */
    void setPort(final int port) {
        this.port = port;
    }
}
