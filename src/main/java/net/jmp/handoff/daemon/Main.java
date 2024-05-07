package net.jmp.handoff.daemon;

/*
 * (#)Main.java 0.7.0   05/07/2024
 * (#)Main.java 0.4.0   04/12/2024
 * (#)Main.java 0.3.0   04/12/2024
 * (#)Main.java 0.2.0   04/06/2024
 * (#)Main.java 0.1.0   04/05/2024
 *
 * @author    Jonathan Parker
 * @version   0.7.0
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

import com.google.gson.Gson;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Optional;

import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;

import org.slf4j.ext.XLogger;

/**
 * The main class.
 */
public final class Main {
    /** The location of the configuration file. */
    private static final String APP_CONFIG_FILE = "config/config.json";

    /** A regular expression pattern to get the port number from a command line argument. */
    private static final Pattern PORT_PATTERN = Pattern.compile("(?<port>^\\d{1,5}$)");

    /** The logger. */
    private final XLogger logger = new XLogger(LoggerFactory.getLogger(this.getClass().getName()));

    /**
     * The constructor.
     */
    private Main() {
        super();
    }

    /**
     * The run method.
     *
     * @param   port    int
     */
    private void run(final int port) {
        this.logger.entry(port);

        this.getAppConfig().ifPresent(appConfig -> {
            final var server = new Server(appConfig.getHostName(), (port != 0) ? port : appConfig.getPort());

            server.setupAndRunServer();
        });

        this.logger.exit();
    }

    /**
     * Get the application configuration.
     *
     * @return  java.lang.Optional&lt;net.jmp.handoff.daemon.Config&gt;
     */
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

    /**
     * The main method.
     *
     * @param   arguments   java.lang.String[]
     */
    public static void main(final String[] arguments) {
        int port = 0;

        if (arguments.length > 0) {
            final var matcher = PORT_PATTERN.matcher(arguments[0]);

            if (matcher.find()) {
                final var portStr = matcher.group("port");

                if (portStr != null) {
                    port = Integer.parseInt(portStr);

                    if (port <= 0 || port > 65535)
                        throw new IllegalArgumentException("Port must be between 0 and 65535");
                } else {
                    System.out.printf("Group 'port' not found in %s%n", arguments[0]);
                }
            } else {
                System.out.printf("Command line argument %s was not recognized as a port number%n", arguments[0]);
            }
        }

        new Main().run(port);
    }
}
