package net.jmp.handoff.daemon;

/*
 * (#)Request.java  0.4.0   04/17/2024
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
 * The request class.
 */
final class Request {
    /** The type. It is typically 'Request'. */
    @SerializedName("type")
    private String type;

    /** The request identifier. */
    @SerializedName("id")
    private String id;

    /** The date and time expressed in ISO-8601. */
    @SerializedName("dateTime")
    private String dateTime;

    /** The name of the event. */
    @SerializedName("event")
    private String event;

    /** The content, if any. */
    @SerializedName("content")
    private String content;

    /**
     * The constructor.
     */
    Request() {
        super();
    }

    /**
     * A constructor that takes a builder.
     *
     * @param   builder net.jmp.handoff.daemon.Request.RequestBuilder
     */
    Request(final Request.RequestBuilder builder) {
        super();

        this.type = "Request";
        this.id = builder.id;
        this.dateTime = builder.dateTime;
        this.event = builder.event;
        this.content = builder.content;
    }

    /**
     * Get the builder.
     *
     * @return  net.jmp.handoff.daemon.Request.RequestBuilder
     */
    static Request.RequestBuilder getBuilder() {
        return new Request.RequestBuilder();
    }

    /**
     * Get the response identifier.
     *
     * @return  java.lang.String
     */
    String getId() {
        return this.id;
    }

    /**
     * Set the response identifier.
     *
     * @param   id  java.lang.String
     */
    void setId(final String id) {
        this.id = id;
    }

    /**
     * Get the name of the event.
     *
     * @return  java.lang.String
     */
    String getEvent() {
        return this.event;
    }

    /**
     * Set the event.
     *
     * @param   socketEvent net.jmp.handoff.daemon.SocketEvents
     */
    void setEvent(final SocketEvents socketEvent) {
        this.event = socketEvent.getValue();
    }

    /**
     * Set the type.
     *
     * @param   type    java.lang.String
     */
    void setType(final String type) {
        this.type = type;
    }

    /**
     * Get the type, typically 'Response'.
     *
     * @return  java.lang.String
     */
    String getType() {
        return this.type;
    }

    /**
     * Get the date and time expressed in ISO-8601..
     *
     * @return  java.lang.String
     */
    String getDateTime() {
        return this.dateTime;
    }

    /**
     * Set the date and time.
     *
     * @param   dateTime    java.lang.String
     */
    void setDateTime(final String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Get the content, if any.
     *
     * @return  java.lang.String
     */
    String getContent() {
        return this.content;
    }

    /**
     * Set the content.
     *
     * @param   content java.lang.String
     */
    void setContent(final String content) {
        this.content = content;
    }

    /**
     * A class that uses the builder pattern
     * to construct new instances of the
     * request object.
     */
    static class RequestBuilder {
        /** The response identifier. */
        private String id;

        /** The date and time expressed in ISO-8601. */
        private String dateTime;

        /** The name of the event. */
        private String event;

        /** The content, if any. */
        private String content;

        /**
         * The default constructor.
         */
        private RequestBuilder() {
            super();
        }

        /**
         * Set the response identifier.
         *
         * @param   id  java.lang.String
         */
        RequestBuilder id(final String id) {
            this.id = id;

            return this;
        }

        /**
         * Set the date and time expressed in ISO-8601.
         *
         * @param   dateTime    java.lang.String
         */
        RequestBuilder dateTime(final String dateTime) {
            this.dateTime = dateTime;

            return this;
        }

        /**
         * Set the event.
         *
         * @param   event   net.jmp.handoff.daemon.SocketEvents
         */
        RequestBuilder event(final SocketEvents event) {
            this.event = event.getValue();

            return this;
        }

        /**
         * Set the content.
         *
         * @param   content net.jmp.handoff.daemon.Content
         */
        RequestBuilder content(final String content) {
            this.content = content;

            return this;
        }

        /**
         * Build and return the new instance.
         *
         * @return  net.jmp.handoff.daemon.Request
         */
        Request build() {
            return new Request(this);
        }
    }
}
