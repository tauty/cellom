package tetz42.cellom.common.exception;

import static tetz42.cellom.common.Const.*;

public class ResourceClosingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2600393047737652150L;

	public ResourceClosingException(Throwable cause) {
		super("Additional exception has occured while closing resources :"
				+ CRLF + cause.getMessage(), cause);
	}
}
