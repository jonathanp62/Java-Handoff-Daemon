package net.jmp.handoff.daemon;

/*
 * (#)TestRequest.java  0.6.0   04/23/2024
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

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The request test class.
 */
public class TestRequest {
    private static final Request request = new Request();
    private static final String id = UUID.randomUUID().toString();

    private static Request builtRequest;

    @BeforeClass
    public static void beforeClass() {
        request.setContent("the free form text content");
        request.setEvent(SocketEvents.ECHO);
        request.setId(id);
        request.setDateTime("2024-04-22T20:02:09.952Z");
        request.setType("Request");

        builtRequest = Request.getBuilder()
                .content("the free form text content")
                .event(SocketEvents.ECHO)
                .id(id)
                .dateTime("2024-04-22T20:02:09.952Z")
                .build();
    }

    @Test
    public void testGetContent() {
        assertEquals("the free form text content", request.getContent());
        assertEquals("the free form text content", builtRequest.getContent());
    }

    @Test
    public void testGetEvent() {
        assertEquals(SocketEvents.ECHO.getValue(), request.getEvent());
        assertEquals(SocketEvents.ECHO.getValue(), builtRequest.getEvent());
    }

    @Test
    public void testGetId() {
        assertEquals(id, request.getId());
        assertEquals(id, builtRequest.getId());
    }

    @Test
    public void testGetDateTime() {
        assertEquals("2024-04-22T20:02:09.952Z", request.getDateTime());
        assertEquals("2024-04-22T20:02:09.952Z", builtRequest.getDateTime());
    }

    @Test
    public void testGetType() {
        assertEquals("Request", request.getType());
        assertEquals("Request", builtRequest.getType());
    }
}
