package net.jmp.handoff.daemon;

/*
 * (#)TestResponse.java 0.6.0   04/23/2024
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
 * The response test class.
 */
public class TestResponse {
    private static final Response response = new Response();
    private static final String id = UUID.randomUUID().toString();
    private static final String sessionId = UUID.randomUUID().toString();
    private static final String requestId = UUID.randomUUID().toString();

    private static Response builtResponse;

    @BeforeClass
    public static void beforeClass() {
        final var content = new EchoContent();

        content.setMessage("my echo content");

        response.setContent(content);
        response.setEvent(SocketEvents.ECHO);
        response.setId(id);
        response.setRequestId(requestId);
        response.setSessionId(sessionId);
        response.setDateTime("2024-04-22T20:02:09.952Z");
        response.setCode(ResponseCode.OK);

        builtResponse = Response.getBuilder()
                .code(ResponseCode.OK)
                .dateTime("2024-04-22T20:02:09.952Z")
                .event(SocketEvents.ECHO)
                .id(id)
                .requestId(requestId)
                .sessionId(sessionId)
                .content(content)
                .build();
    }

    @Test
    public void testGetCode() {
        assertEquals(ResponseCode.OK.getValue(), response.getCode());
        assertEquals(ResponseCode.OK.getValue(), builtResponse.getCode());
    }

    @Test
    public void testGetDateTime() {
        assertEquals("2024-04-22T20:02:09.952Z", response.getDateTime());
        assertEquals("2024-04-22T20:02:09.952Z", builtResponse.getDateTime());
    }

    @Test
    public void testGetEvent() {
        assertEquals(SocketEvents.ECHO.getValue(), response.getEvent());
        assertEquals(SocketEvents.ECHO.getValue(), builtResponse.getEvent());
    }

    @Test
    public void testGetId() {
        assertEquals(id, response.getId());
        assertEquals(id, builtResponse.getId());
    }

    @Test
    public void testGetRequestId() {
        assertEquals(requestId, response.getRequestId());
        assertEquals(requestId, builtResponse.getRequestId());
    }

    @Test
    public void testGetSessionId() {
        assertEquals(sessionId, response.getSessionId());
        assertEquals(sessionId, builtResponse.getSessionId());
    }

    @Test
    public void testGetContent() {
        final EchoContent content = (EchoContent) response.getContent();
        final EchoContent builtContent = (EchoContent) builtResponse.getContent();

        assertEquals("my echo content", content.getMessage());
        assertEquals("my echo content", builtContent.getMessage());

        assertEquals("Echo", content.getType());
        assertEquals("Echo", builtContent.getType());
    }
}
