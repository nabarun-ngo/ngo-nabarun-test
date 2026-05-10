package ngo.nabarun.test.ngo_nabarun_test.utils;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Plugin(name = "MemoryAppender", category = "Core", printObject = true)
public class MemoryAppender extends AbstractAppender {
    private static final ThreadLocal<StringBuilder> logBuffer = ThreadLocal.withInitial(StringBuilder::new);

    protected MemoryAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout, true, null);
    }

    @PluginFactory
    public static MemoryAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") Filter filter) {
        return new MemoryAppender(name, filter, layout);
    }

    @Override
    public void append(LogEvent event) {
        if (getLayout() != null) {
            logBuffer.get().append(new String(getLayout().toByteArray(event), StandardCharsets.UTF_8));
        } else {
            logBuffer.get().append(event.getMessage().getFormattedMessage()).append("\n");
        }
    }

    public static String getAndClearLog() {
        String logs = logBuffer.get().toString();
        logBuffer.get().setLength(0);
        return logs;
    }

    public static void clear() {
        logBuffer.get().setLength(0);
    }
}
