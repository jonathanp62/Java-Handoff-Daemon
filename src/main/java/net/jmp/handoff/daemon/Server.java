package net.jmp.handoff.daemon;

/*
 * (#)Server.java   0.4.0   04/13/2024
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

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

import com.google.gson.Gson;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import java.util.UUID;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

final class Server {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));
    private final Object stopSerializer = new Object();

    private final String hostName;
    private final int port;

    private SocketIOServer socketIOServer;
    private boolean isStopEventReceived;

    Server(final String hostName, final int port) {
        super();

        this.hostName = hostName;
        this.port = port;
    }

    void setupAndRunServer() {
        this.logger.entry();

        this.startServer();
        this.waitAndStopServer();

        this.logger.exit();
    }

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
                (client, message, ackRequest) -> this.versionEventHandler(client));

        this.socketIOServer.addEventListener(SocketEvents.STOP.getValue(), String.class,
                (client, message, ackRequest) -> this.stopEventHandler(client));

        this.socketIOServer.start();

        this.logger.exit();
    }

    private void connectEventHandler(final SocketIOClient client) {
        this.logger.entry(client);

        final var sessionId = client.getSessionId().toString();

        this.logEvent(SocketEvents.CONNECT.getValue(), sessionId);

        final var response = new Response();
        final var gson = new Gson();

        response.setId(UUID.randomUUID().toString());
        response.setSessionId(sessionId);
        response.setDateTime(this.getLocalDateTime());
        response.setEvent(SocketEvents.CONNECT);
        response.setCode(ResponseCode.OK);

        client.sendEvent(SocketEvents.CONNECT.getValue(), gson.toJson(response));

        this.logger.exit();
    }

    private void disconnectEventHandler(final SocketIOClient client) {
        this.logger.entry(client);

        this.logEvent(SocketEvents.DISCONNECT.getValue(), client.getSessionId().toString());

        this.logger.exit();
    }

    private void versionEventHandler(final SocketIOClient client) {
        this.logger.entry(client);

        final var sessionId = client.getSessionId().toString();

        this.logEvent(SocketEvents.VERSION.getValue(), sessionId);

        final var response = new Response();
        final var gson = new Gson();

        response.setId(UUID.randomUUID().toString());
        response.setSessionId(sessionId);
        response.setDateTime(this.getLocalDateTime());
        response.setEvent(SocketEvents.VERSION);
        response.setContent("Handoff daemon version " + Version.VERSION);
        response.setCode(ResponseCode.OK);

        client.sendEvent(SocketEvents.VERSION.getValue(), gson.toJson(response));

        this.logger.exit();
    }

    private void stopEventHandler(final SocketIOClient client ) {
        this.logger.entry(client);

        final var sessionId = client.getSessionId().toString();

        this.logEvent(SocketEvents.STOP.getValue(), sessionId);

        final var response = new Response();
        final var gson = new Gson();

        response.setId(UUID.randomUUID().toString());
        response.setSessionId(sessionId);
        response.setDateTime(this.getLocalDateTime());
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

    private void logEvent(final String eventName, final String sessionId, final String ... args) {
        this.logger.entry(eventName, sessionId, args);

        this.logger.info("Client sent {} event: {}", eventName, sessionId);

        this.logger.exit();
    }

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

    private String getLocalDateTime() {
        this.logger.entry();

        final var localDateTime = LocalDateTime.now();
        final var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        final var formattedDate = localDateTime.format(formatter);

        this.logger.exit(formattedDate);

        return formattedDate;
    }
}
