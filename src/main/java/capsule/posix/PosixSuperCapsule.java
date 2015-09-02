/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capsule.posix;

import capsule.CapsuleAPI;
import capsule.SuperCapsuleImpl;
import java.util.Arrays;
import java.util.IllegalFormatException;
import jnr.posix.POSIX;
import jnr.posix.POSIXFactory;
import jnr.posix.util.DefaultPOSIXHandler;

import static capsule.CapsuleAPI.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pron
 */
public class PosixSuperCapsule extends SuperCapsuleImpl {
    private final POSIX posix;
    private final CapsuleAPI capsule;

    public PosixSuperCapsule(CapsuleAPI capsule) {
        this.capsule = capsule;
        this.posix = POSIXFactory.getNativePOSIX(new POSIXHandler());
    }

    @Override
    public int launch(ProcessBuilder pb) {
        return posix.execve(path(pb), argv(pb), envp(pb));
    }

    protected static String path(ProcessBuilder pb) {
        return pb.command().get(0);
    }

    protected static String[] argv(ProcessBuilder pb) {
        return toArray(pb.command().subList(0, pb.command().size()));
    }

    protected static String[] envp(ProcessBuilder pb) {
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
        public void warn(WARNING_ID id, String message, Object... data) {
            String msg;
            try {
                msg = String.format(message, data);
            } catch (IllegalFormatException e) {
                msg = message + " " + Arrays.toString(data);
            }
            capsule.log1(LOG_QUIET, msg);
        }
    }
}
