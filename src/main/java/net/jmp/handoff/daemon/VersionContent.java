package net.jmp.handoff.daemon;

/*
 * (#)VersionContent.java   0.6.0   04/20/2024
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

final class VersionContent extends Content {
    /** The type. It is typically 'Version'. */
    @SerializedName("type")
    private final String type;

    /** The application name. */
    @SerializedName("name")
    private String appName;

    /** The application version. */
    @SerializedName("version")
    private String appVersion;

    /**
     * The default constructor.
     */
    VersionContent() {
        super();

        this.type = "Version";
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
     * Get the application name.
     *
     * @return  java.lang.String
     */
    String getAppName() {
        return this.appName;
    }

    /**
     * Set the application name.
     *
     * @param   appName java.lang.String
     */
    void setAppName(final String appName) {
        this.appName = appName;
    }

    /**
     * Get the application version.
     *
     * @return  java.lang.String
     */
    String getAppVersion() {
        return this.appVersion;
    }

    /**
     * Set the application version.
     *
     * @param   appVersion  java.lang.String
     */
    void setAppVersion(final String appVersion) {
        this.appVersion = appVersion;
    }
}
