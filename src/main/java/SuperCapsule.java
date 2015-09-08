/*
 * Copyright (c) 2015, Parallel Universe Software Co. and Contributors. All rights reserved.
 * 
 * This program and the accompanying materials are licensed under the terms 
 * of the Eclipse Public License v1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import jnr.posix.POSIX;
import jnr.posix.POSIXFactory;
import jnr.posix.util.DefaultPOSIXHandler;

/**
 *
 * @author pron
 */
public class SuperCapsule extends Capsule {
    private POSIX posix;

    public SuperCapsule(Path jarFile) {
        super(jarFile);
        createImpl();
    }

    public SuperCapsule(Capsule pred) {
        super(pred);
        createImpl();
    }

    private void createImpl() {
        try {
            this.posix = POSIXFactory.getNativePOSIX(new POSIXHandler());
        } catch (Exception e) {
            log(LOG_QUIET, "Cannot load POSIX");
        }
    }

    @Override
    protected int launch(ProcessBuilder pb) throws IOException, InterruptedException {
        if (posix != null)
            return posix.execve(path(pb), argv(pb), envp(pb));
        else
            return super.launch(pb);
    }

    private static String path(ProcessBuilder pb) {
        return pb.command().get(0);
    }

    private static String[] argv(ProcessBuilder pb) {
        return toArray(pb.command().subList(0, pb.command().size()));
    }

    private static String[] envp(ProcessBuilder pb) {
        List<String> env = new ArrayList<>();
        for (Map.Entry<String, String> pair : pb.environment().entrySet())
            env.add(pair.getKey() + '=' + pair.getValue());
        return toArray(env);
    }

    private static String[] toArray(List<String> ss) {
        return ss.toArray(new String[ss.size()]);
    }

    private final class POSIXHandler extends DefaultPOSIXHandler {
        @Override
        public void warn(jnr.posix.POSIXHandler.WARNING_ID id, String message, Object... data) {
            String msg;
            try {
                msg = String.format(message, data);
            } catch (IllegalFormatException e) {
                msg = message + " " + Arrays.toString(data);
            }
            log(LOG_QUIET, msg);
        }
    }
}
