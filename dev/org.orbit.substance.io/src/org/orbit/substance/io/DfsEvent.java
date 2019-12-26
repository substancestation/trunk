package org.orbit.substance.io;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 */
public class DfsEvent {

	public static int CREATE = 1;
	public static int DELETE = 2;
	public static int CONTENT = 3;
	public static int RENAME = 4;
	public static int MOVE = 5;
	public static int REFRESH = 6;

	public DFS dfs;
	public int eventType;
	public Object source;
	public Object oldValue;
	public Object newValue;

	public DfsEvent() {
	}

	public DfsEvent(DFS dfs, int eventType, Object source, Object oldValue, Object newValue) {
		this.dfs = dfs;
		this.eventType = eventType;
		this.source = source;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

}
