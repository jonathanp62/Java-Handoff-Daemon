package net.jmp.handoff.daemon;

/*
 * (#)Main.java 0.4.0   04/12/2024
 * (#)Main.java 0.3.0   04/12/2024
 * (#)Main.java 0.2.0   04/06/2024
 * (#)Main.java 0.1.0   04/05/2024
 *
 * @author    Jonathan Parker
 * @version   0.4.0
 * @since     0.1.0
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

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Optional;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

public final class Main {
    private static final String APP_CONFIG_FILE = "config/config.json";

    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));
    private final Object stopSerializer = new Object();

    private SocketIOServer server;

    /**
     * The constructor.
     */
    private Main() {
        super();
    }

    /**
     * The run method.
     */
    private void run() {
        this.logger.entry();

        this.getAppConfig().ifPresent(appConfig -> {
            this.startServer(appConfig.getHostName(), appConfig.getPort());
            this.waitAndStopServer();
        });

        this.logger.exit();
    }

    private Optional<Config> getAppConfig() {
        this.logger.entry();

        Config appConfig = null;

        try {
            appConfig = new Gson().fromJson(Files.readString(Paths.get(APP_CONFIG_FILE)), Config.class);
        } catch (final IOException ioe) {
            this.logger.catching(ioe);
        }

        this.logger.exit(appConfig);

        return Optional.ofNullable(appConfig);
    }

    private void startServer(final String hostName, final int port) {
        this.logger.entry(hostName, port);

        final var socketIoConfig = new Configuration();

        socketIoConfig.setPort(port);
        socketIoConfig.setHostname(hostName);

        this.server = new SocketIOServer(socketIoConfig);

        this.server.addConnectListener(
                this::connectEventHandler);     // client -> this.connectEventHandler(client));

        this.server.addDisconnectListener(
                this::disconnectEventHandler);  // client -> this.disconnectEventHandler(client))

        this.server.addEventListener(SocketEvents.VERSION, String.class,
                (client, message, ackRequest) -> this.versionEventHandler(client));

        this.server.addEventListener(SocketEvents.STOP, String.class,
                (client, message, ackRequest) -> this.stopEventHandler(client));

        this.server.start();

        this.logger.exit();
    }

    private void connectEventHandler(final SocketIOClient client) {
        this.logger.entry(client);

        final var sessionId = client.getSessionId().toString();

        this.logEvent(SocketEvents.CONNECT, sessionId);

        client.sendEvent(SocketEvents.CONNECT, "connected");

        this.logger.exit();
    }

    private void disconnectEventHandler(final SocketIOClient client) {
        this.logger.entry(client);

        this.logEvent(SocketEvents.DISCONNECT, client.getSessionId().toString());

        this.logger.exit();
    }

    private void versionEventHandler(final SocketIOClient client) {
        this.logger.entry(client);

        final var sessionId = client.getSessionId().toString();

        this.logEvent(SocketEvents.VERSION, sessionId);

        client.sendEvent(SocketEvents.VERSION, "Handoff daemon version " + Version.VERSION);

        this.logger.exit();
    }

    private void stopEventHandler(final SocketIOClient client ) {
        this.logger.entry(client);

        final var sessionId = client.getSessionId().toString();

        this.logEvent(SocketEvents.STOP, sessionId);

        client.sendEvent(SocketEvents.STOP, "Handoff daemon stopping...");

        synchronized (this.stopSerializer) {
            this.stopSerializer.notifyAll();
        }

        this.logger.exit();
    }

    private void waitAndStopServer() {
        this.logger.entry();

        synchronized (this.stopSerializer) {
            try {
                this.stopSerializer.wait();
            } catch (final InterruptedException ie) {
                this.logger.catching(ie);
            }
        }

        this.server.stop();

        this.logger.exit();
    }

    private void logEvent(final String eventName, final String sessionId, final String ... args) {
        this.logger.entry(eventName, sessionId, args);

        this.logger.info("Client sent {} event: {}", eventName, sessionId);

        this.logger.exit();
    }

    /**
     * The main method.
     *
     * @param   arguments   java.lang.String[]
     */
    public static void main(final String[] arguments) {
        new Main().run();
    }
}
