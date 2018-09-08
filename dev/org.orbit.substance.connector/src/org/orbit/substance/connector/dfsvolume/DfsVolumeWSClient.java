package org.orbit.substance.connector.dfsvolume;

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
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClient;
import org.origin.common.rest.client.WSClientConfiguration;

public class DfsVolumeWSClient extends WSClient {

	/**
	 * 
	 * @param config
	 */
	public DfsVolumeWSClient(WSClientConfiguration config) {
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
	public Response upload(String accountId, String blockId, String fileId, long checksum, File file) throws ClientException {
		if (file == null || !file.exists()) {
			throw new ClientException(401, "File doesn't exist.");
		}
		if (!file.isFile()) {
			throw new ClientException(401, "File " + file.getAbsolutePath() + " is not a single file.");
		}

		int partId = 0;
		long size = file.length();

		Response response = null;
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
			if (accountId != null && !accountId.isEmpty()) {
				target = target.queryParam("accountId", "accountId");
			}
			target = target.queryParam("blockId", blockId);
			target = target.queryParam("fileId", fileId);
			target = target.queryParam("partId", partId);
			target = target.queryParam("size", size);
			target = target.queryParam("checksum", checksum);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			MediaType requestContentType = multipart.getMediaType(); // multipart/form-data
			response = updateHeaders(builder).post(Entity.entity(multipart, requestContentType));
			checkResponse(target, response);

		} catch (ClientException e) {
			handleException(e);
		} catch (UnsupportedEncodingException e) {
			handleException(e);
		}
		return response;
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
	public Response upload(String accountId, String blockId, String fileId, int partId, long size, long checksum, InputStream inputStream) throws ClientException {
		// if (file == null || !file.exists()) {
		// throw new ClientException(401, "File doesn't exist.");
		// }
		// if (!file.isFile()) {
		// throw new ClientException(401, "File " + file.getAbsolutePath() + " is not a single file.");
		// }

		Response response = null;
		// boolean succeed = false;
		// FileMetadataDTO newFileDTO = null;
		try {
			MultiPart multipart = new FormDataMultiPart();
			{
				// FileDataBodyPart filePart = new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE);
				// {
				// FormDataContentDisposition.FormDataContentDispositionBuilder formBuilder = FormDataContentDisposition.name("file");
				// formBuilder.fileName(URLEncoder.encode(file.getName(), "UTF-8"));
				// formBuilder.size(file.length());
				// formBuilder.modificationDate(new Date(file.lastModified()));
				// filePart.setFormDataContentDisposition(formBuilder.build());
				// }
				// multipart.bodyPart(filePart);
				StreamDataBodyPart streamPart = new StreamDataBodyPart("file", inputStream);
				multipart.bodyPart(streamPart);
			}

			WebTarget target = getRootPath().path("file").path("content");
			if (accountId != null && !accountId.isEmpty()) {
				target = target.queryParam("accountId", "accountId");
			}
			target = target.queryParam("blockId", blockId);
			target = target.queryParam("fileId", fileId);
			target = target.queryParam("partId", partId);
			target = target.queryParam("size", size);
			target = target.queryParam("checksum", checksum);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			MediaType requestContentType = multipart.getMediaType(); // multipart/form-data
			response = updateHeaders(builder).post(Entity.entity(multipart, requestContentType));
			checkResponse(target, response);

			// newFileDTO = response.readEntity(FileMetadataDTO.class);

		} catch (ClientException e) {
			handleException(e);
		}
		return response;
	}

	/**
	 * Download app file.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/file/content
	 * 
	 * @param accountId
	 * @param blockId
	 * @param fileId
	 * @param partId
	 * @param output
	 * @return
	 * @throws ClientException
	 */
	public boolean download(String accountId, String blockId, String fileId, int partId, OutputStream output) throws ClientException {
		InputStream input = null;
		try {
			WebTarget target = getRootPath().path("app").path("content");
			if (accountId != null && !accountId.isEmpty()) {
				target = target.queryParam("accountId", "accountId");
			}
			target = target.queryParam("blockId", blockId);
			target = target.queryParam("fileId", fileId);
			target = target.queryParam("partId", partId);

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
