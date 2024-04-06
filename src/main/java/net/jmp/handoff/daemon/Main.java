package net.jmp.handoff.daemon;

/*
 * (#)Main.java 0.2.0   04/06/2024
 * (#)Main.java 0.1.0   04/05/2024
 *
 * @author    Jonathan Parker
 * @version   0.2.0
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
import com.corundumstudio.socketio.SocketIOServer;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

public final class Main {
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

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

        final var serializer = new Object();

        final var config = new Configuration();

        config.setPort(8080);
        config.setHostname("localhost");

        final var server = new SocketIOServer(config);

        server.addConnectListener(
                client -> {
                    final var sessionId = client.getSessionId().toString();

                    this.logger.info("Client has connected: {}", sessionId);

                    client.sendEvent("EVENT_CONNECT", "connected");
                });

        server.addDisconnectListener(
                client -> {
                    final var sessionId = client.getSessionId().toString();

                    this.logger.info("Client has disconnected: {}", sessionId);
                });

        server.addEventListener("VERSION", String.class,
                (client, message, ackRequest) -> {
                    final var sessionId = client.getSessionId().toString();

                    this.logger.info("Client sent VERSION event: {}", sessionId);
                    client.sendEvent("VERSION", "Handoff daemon version 0.2.0");
                });

        server.addEventListener("STOP", String.class,
                (client, message, ackRequest) -> {
                    final var sessionId = client.getSessionId().toString();

                    this.logger.info("Client sent STOP event: {}", sessionId);
                    client.sendEvent("STOP", "Handoff daemon stopping...");

                    synchronized (serializer) {
                        serializer.notifyAll();
                    }
                });

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));

        synchronized (serializer) {
            try {
                serializer.wait();
            } catch (final InterruptedException ie) {
                this.logger.catching(ie);
            }

            server.stop();
        }

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
