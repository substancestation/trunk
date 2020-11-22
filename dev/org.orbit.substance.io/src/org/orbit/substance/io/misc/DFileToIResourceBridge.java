package org.orbit.substance.io.misc;

import java.io.IOException;
import java.util.Set;

import org.orbit.substance.io.DFile;
import org.origin.common.resource.Path;
import org.origin.common.resources.IPath;
import org.origin.common.resources.IResource;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.impl.PathImpl;

/*-
 * Adapt a DFile to a IResource.
 * 
 * Sample code:
 * <code>
 *  // Attach DFileToIResourceBridge as IResource to DFile.
 *	public DFileImpl(DFS dfs, Path path) throws IOException {
 *		this.dfs = dfs;
 *		this.path = path;
 *
 *		adapt(IResource.class, new DFileToIResourceBridge(this));
 * 	}
 * 
 *  // In code getting an instance of IAdaptable, which is DFile object.
 *  IAdaptable adaptable = ...;
 *  IResource resource = adaptable.getAdapter(IResource.class);
 *  if (resource != null) {
 *  	resource.getName();
 *  }
 *  
 * <code>
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class DFileToIResourceBridge implements IResource {

	protected DFile file;

	/**
	 * 
	 * @param file
	 */
	public DFileToIResourceBridge(DFile file) {
		this.file = file;
	}

	@Override
	public IWorkspace getWorkspace() {
		// DFile is for generic file system, like java.io.File. There is no concept of workspace for a generic file system.
		throw new UnsupportedOperationException();
	}

	@Override
	public IResource getParent() throws IOException {
		DFile parentFile = this.file.getParent();
		if (parentFile != null) {
			return new DFileToIResourceBridge(parentFile);
		}
		return null;
	}

	@Override
	public String getName() {
		return this.file.getName();
	}

	@Override
	public String getFileExtension() {
		return this.file.getFileExtension();
	}

	@Override
	public IPath getFullPath() {
		Path dPath = this.file.getPath();
		IPath iPath = new PathImpl(dPath.getPathString());
		return iPath;
	}

	@Override
	public boolean exists() throws IOException {
		return this.file.exists();
	}

	@Override
	public boolean isDirectory() throws IOException {
		return this.file.isDirectory();
	}

	@Override
	public boolean create() throws IOException {
		return this.file.createNewFile();
	}

	@Override
	public boolean delete() throws IOException {
		return this.file.delete();
	}

	@Override
	public boolean rename(String newName) throws IOException {
		return this.file.rename(newName);
	}

	@Override
	public void setFilePermissions(Set<?> permissions) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void dispose() {
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.file.adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.file.adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.file.getAdapter(adapter);
	}

}
