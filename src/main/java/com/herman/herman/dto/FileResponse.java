package com.herman.herman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {
  private byte[] resource;
  private long size;
  private String contentType;
  private String fileName;

  public HttpHeaders getResourceHeader() {
    final var headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_TYPE, this.contentType);
    headers.setContentLength(this.size);
    headers.setContentDisposition(ContentDisposition.attachment().filename(this.fileName).build());
    return headers;
  }
}
