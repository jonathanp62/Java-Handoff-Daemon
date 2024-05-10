package net.jmp.handoff.daemon;

/*
 * (#)TestVersionContent.java   0.8.0   05/10/2024
 * (#)TestVersionContent.java   0.6.0   04/20/2024
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
 * The version content test class.
 */
public class TestVersionContent {
    private static final VersionContent versionContent = new VersionContent();

    @BeforeClass
    public static void beforeClass() {
        versionContent.setAppName("Handoff Daemon");
        versionContent.setAppVersion(Version.VERSION);
    }

    @Test
    public void testGetAppName() {
        assertEquals("Handoff Daemon", versionContent.getAppName());
    }

    @Test
    public void testGetAppVersion() {
        assertEquals(Version.VERSION, versionContent.getAppVersion());
    }

    @Test
    public void testGetType() {
        assertEquals("Version", versionContent.getType());
    }

    @Test
    public void testBuilder() {
        final VersionContent content = Builder.of(VersionContent::new)
                .with(VersionContent::setAppVersion, "1.2.3")
                .with(VersionContent::setAppName, "My App Name")
                .build();

        assertEquals("Version", content.getType());
        assertEquals("1.2.3", content.getAppVersion());
        assertEquals("My App Name", content.getAppName());
    }
}
