package com.igknighters.util;

import org.littletonrobotics.junction.Logger;

import com.igknighters.GlobalState;

public class BootupLogger {
    private static final String println_prefix = "[Bootup] ";

    /**
     * Logs a message to the bootup log.
     * 
     * The bootup log is sent to console and to AKit logger
     * 
     * @param message The message to log
     */
    public static synchronized void bootupLog(String message) {
        Logger.recordOutput("/Bootup", message);

        if (!GlobalState.isUnitTest()) {
            System.out.println(println_prefix + message);
        }
    }
}
