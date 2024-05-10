package net.jmp.handoff.daemon;

/*
 * (#)TestStopContent.java  0.8.0   05/10/2024
 * (#)TestStopContent.java  0.6.0   04/23/2024
 *
 * @author    Jonathan Parker
 * @version   0.8.0
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

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The stop content test class.
 */
public class TestStopContent {
    private static final StopContent stopContent = new StopContent();

    @BeforeClass
    public static void beforeClass() {
        stopContent.setMessage("some arbitrary message text");
        stopContent.setPid(4321);
    }

    @Test
    public void testGetMessage() {
        assertEquals("some arbitrary message text", stopContent.getMessage());
    }

    @Test
    public void testGetPid() {
        assertEquals(4321, stopContent.getPid());
    }

    @Test
    public void testGetType() {
        assertEquals("Stop", stopContent.getType());
    }

    @Test
    public void testBuilder() {
        final StopContent content = Builder.of(StopContent::new)
                .with(StopContent::setPid, 12345L)
                .with(StopContent::setMessage, "Please stop!")
                .build();

        assertEquals("Stop", content.getType());
        assertEquals(12345, content.getPid());
        assertEquals("Please stop!", content.getMessage());
    }
}
