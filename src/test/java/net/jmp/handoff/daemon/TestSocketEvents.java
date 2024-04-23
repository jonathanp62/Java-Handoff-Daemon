package net.jmp.handoff.daemon;

/*
 * (#)TestSocketEvents.java 0.6.0   04/23/2024
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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The socket events test class.
 */
public class TestSocketEvents {
    @Test
    public void testConnect() {
        assertEquals("EVENT_CONNECT", SocketEvents.CONNECT.getDescriptor());
        assertEquals("EVENT_CONNECT", SocketEvents.CONNECT.getValue());
    }

    @Test
    public void testDisconnect() {
        assertEquals("EVENT_DISCONNECT", SocketEvents.DISCONNECT.getDescriptor());
        assertEquals("EVENT_DISCONNECT", SocketEvents.DISCONNECT.getValue());
    }

    @Test
    public void testEcho() {
        assertEquals("ECHO", SocketEvents.ECHO.getDescriptor());
        assertEquals("ECHO", SocketEvents.ECHO.getValue());
    }

    @Test
    public void testStop() {
        assertEquals("STOP", SocketEvents.STOP.getDescriptor());
        assertEquals("STOP", SocketEvents.STOP.getValue());
    }

    @Test
    public void testVersion() {
        assertEquals("VERSION", SocketEvents.VERSION.getDescriptor());
        assertEquals("VERSION", SocketEvents.VERSION.getValue());
    }
}
