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

import java.util.Optional;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The server test class.
 */
public class TestServer {
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
}
