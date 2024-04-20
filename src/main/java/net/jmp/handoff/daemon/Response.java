package net.jmp.handoff.daemon;

/*
 * (#)Response.java 0.6.0   04/20/2024
 * (#)Response.java 0.4.0   04/13/2024
 *
 * @author    Jonathan Parker
 * @version   0.6.0
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
 * The response class.
 */
final class Response {
    /** The type. It is typically 'Response'. */
    @SerializedName("type")
    private final String type;

    /** The response identifier. */
    @SerializedName("id")
    private String id;

    /** The request identifier. */
    @SerializedName("requestId")
    private String requestId;

    /** The session identifier. */
    @SerializedName("sessionId")
    private String sessionId;

    /** The date and time expressed in ISO-8601. */
    @SerializedName("dateTime")
    private String dateTime;

    /** The name of the event. */
    @SerializedName("event")
    private String event;

    /** The content, if any. */
    @SerializedName("content")
    private Content content;

    /** The response code, typically 'OK' or 'Not OK'. */
    @SerializedName("code")
    private String code;

    /**
     * The constructor.
     */
    Response() {
        super();

        this.type = "Response";
        this.requestId = "";
    }

    /**
     * A constructor that takes a builder.
     *
     * @param   builder net.jmp.handoff.daemon.Response.ResponseBuilder
     */
    Response(final ResponseBuilder builder) {
        super();

        this.type = "Response";
        this.id = builder.id;
        this.requestId = builder.requestId;
        this.sessionId = builder.sessionId;
        this.dateTime = builder.dateTime;
        this.event = builder.event.getValue();
        this.content = builder.content;
        this.code = builder.code.getValue();
    }

    /**
     * Get the builder.
     *
     * @return  net.jmp.handoff.daemon.Response.ResponseBuilder
     */
    static ResponseBuilder getBuilder() {
        return new ResponseBuilder();
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
     * Get the session identifier.
     *
     * @return  java.lang.String
     */
    String getSessionId() {
        return this.sessionId;
    }

    /**
     * Set the session identifier.
     *
     * @param   sessionId   java.lang.String
     */
    void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
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
     * Get the type, typically 'Response'.
     *
     * @return  java.lang.String
     */
    String getType() {
        return this.type;
    }

    /**
     * Get the content, if any.
     *
     * @return  net.jmp.handoff.daemon.Content
     */
    Content getContent() {
        return this.content;
    }

    /**
     * Set the content.
     *
     * @param   content net.jmp.handoff.daemon.Content
     */
    void setContent(final Content content) {
        this.content = content;
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
     * Get the request identifier.
     *
     * @return  java.lang.String
     */
    String getRequestId() {
        return this.requestId;
    }

    /**
     * Set the request identifier.
     *
     * @param   requestId   java.lang.String
     */
    void setRequestId(final String requestId) {
        this.requestId = requestId;
    }

    /**
     * Get the response code, typically 'OK' or 'Not OK'.
     *
     * @return  java.lang.String
     */
    String getCode() {
        return this.code;
    }

    /**
     * Set the response code.
     *
     * @param   responseCode    net.jmp.handoff.daemon.ResponseCode
     */
    void setCode(final ResponseCode responseCode) {
        this.code = responseCode.getValue();
    }

    /**
     * A class that uses the builder pattern
     * to construct new instances of the
     * response object.
     */
    static class ResponseBuilder {
        /** The response identifier. */
        private String id;

        /** The request identifier. */
        private String requestId;

        /** The session identifier. */
        private String sessionId;

        /** The date and time expressed in ISO-8601. */
        private String dateTime;

        /** The name of the event. */
        private SocketEvents event;

        /** The content, if any. */
        private Content content;

        /** The response code, typically 'OK' or 'Not OK'. */
        private ResponseCode code;

        /**
         * The default constructor.
         */
        private ResponseBuilder() {
            super();
        }

        /**
         * Set the response identifier.
         *
         * @param   id  java.lang.String
         */
        ResponseBuilder id(final String id) {
            this.id = id;

            return this;
        }

        /**
         * Set the request identifier.
         *
         * @param   requestId   java.lang.String
         */
        ResponseBuilder requestId(final String requestId) {
            this.requestId = requestId;

            return this;
        }

        /**
         * Set the session identifier.
         *
         * @param   sessionId   java.lang.String
         */
        ResponseBuilder sessionId(final String sessionId) {
            this.sessionId = sessionId;

            return this;
        }

        /**
         * Set the date and time expressed in ISO-8601.
         *
         * @param   dateTime    java.lang.String
         */
        ResponseBuilder dateTime(final String dateTime) {
            this.dateTime = dateTime;

            return this;
        }

        /**
         * Set the event.
         *
         * @param   event   net.jmp.handoff.daemon.SocketEvents
         */
        ResponseBuilder event(final SocketEvents event) {
            this.event = event;

            return this;
        }

        /**
         * Set the content.
         *
         * @param   content net.jmp.handoff.daemon.Content
         */
        ResponseBuilder content(final Content content) {
            this.content = content;

            return this;
        }

        /**
         * Set the code.
         *
         * @param   code    net.jmp.handoff.daemon.ResponseCode
         */
        ResponseBuilder code(final ResponseCode code) {
            this.code = code;

            return this;
        }

        /**
         * Build and return the new instance.
         *
         * @return  net.jmp.handoff.daemon.Response
         */
        Response build() {
            return new Response(this);
        }
    }
}
