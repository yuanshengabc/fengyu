package cn.deepclue.datamaster.streamer.impl;

import junit.framework.TestCase;

/**
 * Created by xuzb on 14/03/2017.
 */
public class StreamerTest extends TestCase {
    public void testStreamerStub() {
        StreamerStub streamer = new StreamerStub();
        assertEquals(streamer.start(), true);
    }
}
