package net.jmp.handoff.daemon;

/*
 * (#)Response.java 0.4.0   04/13/2024
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

final class Response {
    @SerializedName("type")
    private final String type;

    @SerializedName("id")
    private String id;

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("event")
    private String event;

    @SerializedName("content")
    private String content;

    Response() {
        super();

        this.type = "Response";
    }

    String getId() {
        return this.id;
    }

    void setId(final String id) {
        this.id = id;
    }

    String getSessionId() {
        return this.sessionId;
    }

    void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    String getEvent() {
        return this.event;
    }

    void setEvent(final String event) {
        this.event = event;
    }

    String getType() {
        return this.type;
    }

    String getContent() {
        return this.content;
    }

    void setContent(final String content) {
        this.content = content;
    }

    String getDateTime() {
        return this.dateTime;
    }

    void setDateTime(final String dateTime) {
        this.dateTime = dateTime;
    }
}
