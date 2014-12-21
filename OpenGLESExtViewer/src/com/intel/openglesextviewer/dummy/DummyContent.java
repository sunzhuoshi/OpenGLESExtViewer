package com.intel.openglesextviewer.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {
        // Add 3 sample items.
        addItem(new DummyItem("1", "OpenGLES 2.0", 2, 0));
        addItem(new DummyItem("2", "OpenGLES 3.0", 3, 0));
        addItem(new DummyItem("3", "OpenGLES 3.1", 3, 1));
    }

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public String content;
        public int majorVersion;
        public int minorVersion;
        public boolean supported;
        public Object[] extensions;
        public Object[] intelExtensions;
        public DummyItem(String id, String content, int majorVersion, int minorVersion) {
            this.id = id;
            this.content = content;
            this.majorVersion = majorVersion;
            this.minorVersion = minorVersion;
            this.supported = false;
            this.extensions = new String[0];
            this.intelExtensions = new String[0];
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
