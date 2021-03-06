package com.lanbing.spring.xnolscan.log.formatter;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Formatter implementation that simply returns the logback message.
 *
 * @author fengqi
 */
public class MessageFormatter implements Formatter {

    public String format(ILoggingEvent event) {
        return event.getFormattedMessage();
    }

}
