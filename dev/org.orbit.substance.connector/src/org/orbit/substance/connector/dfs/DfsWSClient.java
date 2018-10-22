package org.orbit.substance.connector.dfs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.orbit.substance.model.dfs.FileMetadataDTO;
import org.origin.common.io.IOUtil;
import org.origin.common.resource.Path;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClient;
import org.origin.common.rest.client.WSClientConfiguration;

/*
 * Dfs web service client.
 * 
 * {contextRoot}: /orbit/v1/dfs
 * 
 * // Upload a file.
 * URL (POST):   {scheme}://{host}:{port}/{contextRoot}/file/content (FormData: InputStream and FormDataContentDisposition)
 * 
 * // Download a file.
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/file/content
 * 
 * @see AppStoreWSClient
 */
public class DfsWSClient extends WSClient {

	/**
	 * 
	 * @param config
	 */
	public DfsWSClient(WSClientConfiguration config) {
		super(config);
	}

	/**
	 * Upload a file to a parent directory (by parent file id) to dfs server. Parent directory need to be existing file.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/file/content (FormData: InputStream and FormDataContentDisposition)
	 * 
	 * @param parentFileId
	 * @param file
	 * @return
	 * @throws ClientException
	 */
	public FileMetadataDTO upload(String parentFileId, File file) throws ClientException {
		if (file == null || !file.exists()) {
			throw new ClientException(401, "File doesn't exist.");
		}
		if (!file.isFile()) {
			throw new ClientException(401, "File " + file.getAbsolutePath() + " is not a single file.");
		}

		FileMetadataDTO newFileDTO = null;
		try {
			MultiPart multipart = new FormDataMultiPart();
			{
				FileDataBodyPart filePart = new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE);
				{
					FormDataContentDisposition.FormDataContentDispositionBuilder formBuilder = FormDataContentDisposition.name("file");
					formBuilder.fileName(URLEncoder.encode(file.getName(), "UTF-8"));
					formBuilder.size(file.length());
					formBuilder.modificationDate(new Date(file.lastModified()));
					filePart.setFormDataContentDisposition(formBuilder.build());
				}
				multipart.bodyPart(filePart);
			}

			WebTarget target = getRootPath().path("file").path("content");
			target = target.queryParam("parentType", "fileId");
			target = target.queryParam("parentValue", parentFileId);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			MediaType requestContentType = multipart.getMediaType(); // multipart/form-data
			Response response = updateHeaders(builder).post(Entity.entity(multipart, requestContentType));
			checkResponse(target, response);

			newFileDTO = response.readEntity(FileMetadataDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} catch (UnsupportedEncodingException e) {
			handleException(e);
		}
		return newFileDTO;
	}

	/**
	 * Upload a file to a parent directory (by file path) to dfs server. Parent directory may or may not exist.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/file/content (FormData: InputStream and FormDataContentDisposition)
	 * 
	 * @param parentPath
	 * @param file
	 * @return
	 * @throws ClientException
	 */
	public FileMetadataDTO upload(Path parentPath, File file) throws ClientException {
		if (file == null || !file.exists()) {
			throw new ClientException(401, "File doesn't exist.");
		}
		if (!file.isFile()) {
			throw new ClientException(401, "File " + file.getAbsolutePath() + " is not a single file.");
		}

		FileMetadataDTO newFileDTO = null;
		try {
			MultiPart multipart = new FormDataMultiPart();
			{
				FileDataBodyPart filePart = new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE);
				{
					FormDataContentDisposition.FormDataContentDispositionBuilder formBuilder = FormDataContentDisposition.name("file");
					formBuilder.fileName(URLEncoder.encode(file.getName(), "UTF-8"));
					formBuilder.size(file.length());
					formBuilder.modificationDate(new Date(file.lastModified()));
					filePart.setFormDataContentDisposition(formBuilder.build());
				}
				multipart.bodyPart(filePart);
			}

			WebTarget target = getRootPath().path("file").path("content");
			target = target.queryParam("parentType", "path");
			target = target.queryParam("parentValue", parentPath);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			MediaType requestContentType = multipart.getMediaType(); // multipart/form-data
			Response response = updateHeaders(builder).post(Entity.entity(multipart, requestContentType));
			checkResponse(target, response);

			newFileDTO = response.readEntity(FileMetadataDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} catch (UnsupportedEncodingException e) {
			handleException(e);
		}
		return newFileDTO;
	}

	/**
	 * Download a file.
	 * 
	 * type: "fileId" or "path"
	 * 
	 * value: fileId string or path string
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/file/content?type={type}&value={value}
	 * 
	 * @param type
	 * @param value
	 * @param output
	 * @return
	 * @throws ClientException
	 */
	public boolean download(String fileId, OutputStream output) throws ClientException {
		InputStream input = null;
		try {
			WebTarget target = getRootPath().path("file").path("content");
			target = target.queryParam("type", "fileId");
			target = target.queryParam("value", fileId);

			Builder builder = target.request(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM);
			Response response = updateHeaders(builder).get();
			checkResponse(target, response);

			input = response.readEntity(InputStream.class);

			if (input != null) {
				IOUtil.copy(input, output);
			}

		} catch (ClientException | IOException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(input, true);
		}
		return true;
	}

	/**
	 * Download a file.
	 * 
	 * type: "fileId" or "path"
	 * 
	 * value: fileId string or path string
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/file/content?type={type}&value={value}
	 * 
	 * @param type
	 * @param value
	 * @param output
	 * @return
	 * @throws ClientException
	 */
	public boolean download(Path path, OutputStream output) throws ClientException {
		InputStream input = null;
		try {
			WebTarget target = getRootPath().path("file").path("content");
			target = target.queryParam("type", "path");
			target = target.queryParam("value", path);

			Builder builder = target.request(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM);
			Response response = updateHeaders(builder).get();
			checkResponse(target, response);

			input = response.readEntity(InputStream.class);

			if (input != null) {
				IOUtil.copy(input, output);
			}

		} catch (ClientException | IOException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(input, true);
		}
		return true;
	}

}
