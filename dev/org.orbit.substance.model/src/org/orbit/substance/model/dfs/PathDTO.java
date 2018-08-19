package org.orbit.substance.model.dfs;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PathDTO {

	@XmlElement
	protected String pathString;

	public PathDTO() {
	}

	@XmlElement
	public String getPathString() {
		return pathString;
	}

	public void setPathString(String pathString) {
		this.pathString = pathString;
	}

}
