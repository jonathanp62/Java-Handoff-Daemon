package net.jmp.handoff.daemon;

/*
 * (#)Server.java   0.5.0   04/17/2024
 * (#)Server.java   0.4.0   04/13/2024
 *
 * @author    Jonathan Parker
 * @version   0.5.0
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

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import java.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

/**
 * The server class.
 */
final class Server {
    /** The logger. */
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    /** The object with which the stop is serialized. */
    private final Object stopSerializer = new Object();

    /** The host name. */
    private final String hostName;

    /** The port. */
    private final int port;

    /** The socket IO server object. */
    private SocketIOServer socketIOServer;

    /** True whe a stop event is received. */
    private boolean isStopEventReceived;

    /**
     * The constructor.
     *
     * @param   hostName    java.lang.String
     * @param   port        int
     */
    Server(final String hostName, final int port) {
        super();

        this.hostName = hostName;
        this.port = port;
    }

    /**
     * Set up and start the socket IO server.
     */
    void setupAndRunServer() {
        this.logger.entry();

        this.startServer();
        this.waitAndStopServer();

        this.logger.exit();
    }

    /**
     * Start the socket IO server.
     */
    private void startServer() {
        this.logger.entry();

        final var socketIoConfig = new Configuration();

        socketIoConfig.setPort(this.port);
        socketIoConfig.setHostname(this.hostName);

        this.socketIOServer = new SocketIOServer(socketIoConfig);

        this.socketIOServer.addConnectListener(
                this::connectEventHandler);     // client -> this.connectEventHandler(client));

        this.socketIOServer.addDisconnectListener(
                this::disconnectEventHandler);  // client -> this.disconnectEventHandler(client))

        this.socketIOServer.addEventListener(SocketEvents.VERSION.getValue(), String.class,
                (client, message, ackRequest) -> this.versionEventHandler(client, message));

        this.socketIOServer.addEventListener(SocketEvents.STOP.getValue(), String.class,
                (client, message, ackRequest) -> this.stopEventHandler(client, message));

        this.socketIOServer.start();

        this.logger.exit();
    }

    /**
     * The connect event handler.
     *
     * @param   client  com.corundumstudio.socketio.SocketIOClient
     */
    private void connectEventHandler(final SocketIOClient client) {
        this.logger.entry(client);

        final var sessionId = client.getSessionId().toString();

        this.logEvent(SocketEvents.CONNECT.getValue(), sessionId);

        final var response = new Response();
        final var gson = new Gson();

        response.setId(UUID.randomUUID().toString());
        response.setSessionId(sessionId);
        response.setDateTime(this.getUTCDateTime());
        response.setEvent(SocketEvents.CONNECT);
        response.setCode(ResponseCode.OK);

        client.sendEvent(SocketEvents.CONNECT.getValue(), gson.toJson(response));

        this.logger.exit();
    }

    /**
     * The disconnect event handler.
     *
     * @param   client  com.corundumstudio.socketio.SocketIOClient
     */
    private void disconnectEventHandler(final SocketIOClient client) {
        this.logger.entry(client);

        this.logEvent(SocketEvents.DISCONNECT.getValue(), client.getSessionId().toString());

        this.logger.exit();
    }

    /**
     * The version event handler.
     *
     * @param   client  com.corundumstudio.socketio.SocketIOClient
     * @param   message java.lang.String
     */
    private void versionEventHandler(final SocketIOClient client, final String message) {
        this.logger.entry(client, message);

        final var sessionId = client.getSessionId().toString();
        final var request = new Gson().fromJson(message, Request.class);

        this.logEvent(SocketEvents.VERSION.getValue(), sessionId, message);
        this.logRequest(request);

        final var response = new Response();
        final var gson = new Gson();

        response.setId(UUID.randomUUID().toString());
        response.setRequestId(request.getId());
        response.setSessionId(sessionId);
        response.setDateTime(this.getUTCDateTime());
        response.setEvent(SocketEvents.VERSION);
        response.setContent("Handoff daemon version " + Version.VERSION);
        response.setCode(ResponseCode.OK);

        client.sendEvent(SocketEvents.VERSION.getValue(), gson.toJson(response));

        this.logger.exit();
    }

    /**
     * The stop event handler.
     *
     * @param   client  com.corundumstudio.socketio.SocketIOClient
     * @param   message java.lang.String
     */
    private void stopEventHandler(final SocketIOClient client, final String message) {
        this.logger.entry(client, message);

        final var sessionId = client.getSessionId().toString();
        final var request = new Gson().fromJson(message, Request.class);

        this.logEvent(SocketEvents.STOP.getValue(), sessionId, message);
        this.logRequest(request);

        final var response = new Response();
        final var gson = new Gson();

        response.setId(UUID.randomUUID().toString());
        response.setRequestId(request.getId());
        response.setSessionId(sessionId);
        response.setDateTime(this.getUTCDateTime());
        response.setEvent(SocketEvents.STOP);
        response.setContent("Handoff daemon stopping");
        response.setCode(ResponseCode.OK);

        client.sendEvent(SocketEvents.STOP.getValue(), gson.toJson(response));

        this.isStopEventReceived = true;

        synchronized (this.stopSerializer) {
            this.stopSerializer.notifyAll();
        }

        this.logger.exit();
    }

    /**
     * Method to log a received event.
     *
     * @param   eventName   java.lang.String
     * @param   sessionId   java.lang.String
     * @param   args        java.lang.String[]
     */
    private void logEvent(final String eventName, final String sessionId, final String ... args) {
        this.logger.entry(eventName, sessionId, args);

        this.logger.info("Client sent {} event: {}", eventName, sessionId);

        if (args.length > 0) {
            if (this.logger.isInfoEnabled()) {
                for (final var arg : args) {
                    this.logger.info("Client arg: {}", arg);
                }
            }
        }

        this.logger.exit();
    }

    /**
     * Log the request object.
     *
     * @param   request net.jmp.handoff.daemon.Request
     */
    private void logRequest(final Request request) {
        this.logger.entry(request);

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Type     : {}", request.getType());
            this.logger.debug("ID       : {}", request.getId());

            final var localDateTime = this.getLocalDateTime(request.getDateTime());

            if (localDateTime.isPresent())
                this.logger.debug("Date-Time: {} ({})", request.getDateTime(), localDateTime.get());
            else
                this.logger.debug("Date-Time: {}", request.getDateTime());

            this.logger.debug("Event    : {}", request.getEvent());

            if (request.getContent() != null)
                this.logger.debug("Content  : {}", request.getContent());
        }

        this.logger.exit();
    }

    /**
     * Return a UTC timestamp formatted in local date and time.
     *
     * @param   utcDateTime java.lang.String
     * @return              java.util.Optional&lt;java.lang.String&gt;
     */
    private Optional<String> getLocalDateTime(final String utcDateTime) {
        this.logger.entry(utcDateTime);

        final var utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date localDate = null;

        try {
            localDate = utcFormat.parse(utcDateTime);
        } catch (final ParseException pe) {
            this.logger.catching(pe);
        }

        String localDateTimeForatted = null;

        if (localDate != null) {
            final var localFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            localDateTimeForatted = localFormat.format(localDate);

            final var timeZone = TimeZone.getDefault();

            localDateTimeForatted = localDateTimeForatted + " (" + timeZone.getDisplayName() + ')';
        }

        this.logger.exit(localDateTimeForatted);

        return Optional.ofNullable(localDateTimeForatted);
    }

    /**
     * Wait for and stop the socket IO server.
     */
    private void waitAndStopServer() {
        this.logger.entry();

        while (!this.isStopEventReceived) {
            synchronized (this.stopSerializer) {
                try {
                    this.stopSerializer.wait();
                } catch (final InterruptedException ie) {
                    this.logger.catching(ie);

                    Thread.currentThread().interrupt();
                }
            }
        }

        this.socketIOServer.stop();

        this.logger.exit();
    }

    /**
     * Get the UTC date time expressed as ISO-8601
     *
     * @return  java.lang.String
     */
    private String getUTCDateTime() {
        this.logger.entry();

        final var formattedDate = ZonedDateTime
                .now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));

        this.logger.exit(formattedDate);

        return formattedDate;
    }
}
