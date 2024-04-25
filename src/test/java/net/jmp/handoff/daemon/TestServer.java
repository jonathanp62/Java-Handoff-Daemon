package net.jmp.handoff.daemon;

/*
 * (#)TestServer.java   0.6.0   04/23/2024
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

import com.google.gson.Gson;

import io.socket.client.IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.util.concurrent.Semaphore;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The server test class.
 */
public class TestServer {
    private static final String SERVER_URL = "http://localhost:10130";

    private static Thread serverThread;

    @BeforeClass
    public static void beforeClass() {
        serverThread = new Thread(() -> {
            final var command = List.of(
                    "/Users/Maestro/.handoff/daemon/bin/start.sh"
            );

            try {
                final var process = new ProcessBuilder(command).redirectErrorStream(true).start();

                try (final var processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;

                    while ((line = processOutputReader.readLine()) != null) {
                        System.out.println(line);
                    }

                    process.waitFor();
                } catch (final InterruptedException ie) {
                    ie.printStackTrace(System.err);
                    Thread.currentThread().interrupt();     // Restore the interrupt status
                }
            } catch (final IOException ioe) {
                ioe.printStackTrace(System.err);
            }
        });

        serverThread.start();
    }

    @AfterClass
    public static void afterClass() throws Throwable {
        if (serverThread != null) {
            final var stopSemaphore = new Semaphore(1);
            final var stopSerializer = new Object();
            final var isStopEventHandled = new AtomicBoolean(false);

            final var options = new IO.Options();

            options.forceNew = false;
            options.reconnection = true;
            options.timeout = 5000;
            options.transports = new String[1];
            options.transports[0] = "websocket";

            final var socket = IO.socket(SERVER_URL, options);

            socket.on(SocketEvents.CONNECT.getValue(), objects -> {
                System.out.println("Server sent " + SocketEvents.CONNECT.getValue() + " event");

                for (final var object : objects)
                    System.out.println("Object: " + object.toString());

                if (socket.connected()) {
                    System.out.println("Socket is connected");

                    if (stopSemaphore.tryAcquire()) {
                        final var request = Request.getBuilder()
                                .id(UUID.randomUUID().toString())
                                .dateTime(getUTCDateTime())
                                .event(SocketEvents.STOP)
                                .build();

                        socket.emit(SocketEvents.STOP.getValue(), new Gson().toJson(request));
                    }
                } else {
                    System.out.println("Socket is not yet connected");
                }
            });

            socket.on(SocketEvents.DISCONNECT.getValue(), args -> {
                System.out.println("Server sent " + SocketEvents.DISCONNECT.getValue() + " event");

                for (final var arg : args)
                    System.out.println("Arg: " + arg.toString());
            });

            socket.on(SocketEvents.STOP.getValue(), args -> {
                System.out.println("Server sent " + SocketEvents.STOP.getValue() + " event");

                for (final var arg : args)
                    System.out.println("Arg: " + arg.toString());

                isStopEventHandled.compareAndSet(false, true);

                synchronized (stopSerializer) {
                    stopSerializer.notifyAll();
                }
            });

            socket.connect();

            while (!isStopEventHandled.get()) {
                synchronized (stopSerializer) {
                    try {
                        stopSerializer.wait();
                    } catch (final InterruptedException ie) {
                        ie.printStackTrace(System.err);
                        Thread.currentThread().interrupt();
                    }
                }
            }

            socket.disconnect();
            socket.close();

            System.out.println("Socket disconnected and closed");

            try {
                serverThread.join();
            } catch (final InterruptedException ie) {
                ie.printStackTrace(System.err);
                Thread.currentThread().interrupt();
            }

            serverThread = null;
        }
    }

    @Test
    public void testEchoEvent() throws Throwable {
        assertNotNull(serverThread);

        final var echoSemaphore = new Semaphore(1);
        final var echoSerializer = new Object();
        final var isEchoEventHandled = new AtomicBoolean(false);

        final var options = new IO.Options();

        options.forceNew = false;
        options.reconnection = true;
        options.timeout = 5000;
        options.transports = new String[1];
        options.transports[0] = "websocket";

        final var socket = IO.socket(SERVER_URL, options);

        socket.on(SocketEvents.CONNECT.getValue(), objects -> {
            System.out.println("Server sent " + SocketEvents.CONNECT.getValue() + " event");

            for (final var object : objects)
                System.out.println("Object: " + object.toString());

            if (socket.connected()) {
                System.out.println("Socket is connected");

                if (echoSemaphore.tryAcquire()) {
                    final var request = Request.getBuilder()
                            .id(UUID.randomUUID().toString())
                            .dateTime(getUTCDateTime())
                            .event(SocketEvents.ECHO)
                            .content("My string to echo back")
                            .build();

                    socket.emit(SocketEvents.ECHO.getValue(), new Gson().toJson(request));
                }
            } else {
                System.out.println("Socket is not yet connected");
            }
        });

        socket.on(SocketEvents.DISCONNECT.getValue(), args -> {
            System.out.println("Server sent " + SocketEvents.DISCONNECT.getValue() + " event");

            for (final var arg : args)
                System.out.println("Arg: " + arg.toString());
        });

        socket.on(SocketEvents.ECHO.getValue(), args -> {
            System.out.println("Server sent " + SocketEvents.ECHO.getValue() + " event");

            assertEquals(1, args.length);

            try {
                final var response = new Gson().fromJson(args[0].toString(), EchoResponse.class);

                assertEquals(SocketEvents.ECHO.getValue(), response.event);
                assertEquals("OK", response.code);

                assertNotNull(response.id);
                assertNotNull(response.requestId);
                assertNotNull(response.sessionId);
                assertNotNull(response.dateTime);

                final var content = response.content;

                assertEquals("Echo", content.getType());
                assertEquals("Echo: My string to echo back", content.getMessage());
            } finally {
                isEchoEventHandled.compareAndSet(false, true);

                synchronized (echoSerializer) {
                    echoSerializer.notifyAll();
                }
            }
        });

        socket.connect();

        while (!isEchoEventHandled.get()) {
            synchronized (echoSerializer) {
                try {
                    echoSerializer.wait();
                } catch (final InterruptedException ie) {
                    ie.printStackTrace(System.err);
                    Thread.currentThread().interrupt();
                }
            }
        }

        socket.disconnect();
        socket.close();

        System.out.println("Socket disconnected and closed");
    }

    @Test
    public void testVersionEvent() {

    }

    @Test
    public void testGetLocalDateTime() throws Throwable {
        final var server = new Server("localhost", 8088);
        final var method = Server.class.getDeclaredMethod("getLocalDateTime", String.class);

        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        final Optional<String> result = (Optional<String>) method.invoke(server, "2024-04-22T20:02:09.952Z");

        assertTrue(result.isPresent());
        assertEquals("2024-04-22T16:02:09 (Eastern Daylight Time)", result.get());
    }

    /**
     * Get the UTC date time expressed as ISO-8601
     *
     * @return  java.lang.String
     */
    private static String getUTCDateTime() {
        return ZonedDateTime
                .now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
    }

    /**
     * A variation on the response class that
     * contains a concrete content class for
     * GSON to deserialize.
     */
    static class EchoResponse {
        String type;
        String id;
        String requestId;
        String sessionId;
        String dateTime;
        String event;
        EchoContent content;
        String code;
    }

    /**
     * A variation on the response class that
     * contains a concrete content class for
     * GSON to deserialize.
     */
    static class VersionResponse {
        String type;
        String id;
        String requestId;
        String sessionId;
        String dateTime;
        String event;
        VersionContent content;
        String code;
    }
}
