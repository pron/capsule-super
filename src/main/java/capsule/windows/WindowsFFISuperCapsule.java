package capsule.windows;

import capsule.CapsuleAPI;
import capsule.posix.PosixSuperCapsule;
import jnr.ffi.LibraryLoader;

/**
 * @author circlespainter
 */
public class WindowsFFISuperCapsule extends PosixSuperCapsule {
	public interface LibC {
		int _execve(String path, String[] argv, String[] envp);
	}

	private final LibC libc;

	public WindowsFFISuperCapsule(CapsuleAPI capsule) {
		super(capsule);
		this.libc = LibraryLoader.create(LibC.class).stdcall().load("msvcrt");
	}

	@Override
	public int launch(ProcessBuilder pb) {
		return libc._execve(path(pb), argv(pb), envp(pb));
	}
}
