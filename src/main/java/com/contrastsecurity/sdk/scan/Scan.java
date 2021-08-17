package com.contrastsecurity.sdk.scan;

import com.contrastsecurity.exceptions.HttpResponseException;
import com.contrastsecurity.exceptions.ResourceNotFoundException;
import com.contrastsecurity.exceptions.UnauthorizedException;
import com.contrastsecurity.sdk.internal.Refreshable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ScheduledExecutorService;

/** Describes a scan resource. */
public interface Scan extends Refreshable<Scan> {

  /** Builder for defining a new scan. */
  interface Definition {

    /**
     * @param id ID of the code artifact to scan
     * @return this
     */
    Definition withExistingCodeArtifact(String id);

    /**
     * @param codeArtifact the code artifact to scan
     * @return this
     */
    Definition withExistingCodeArtifact(CodeArtifact codeArtifact);

    /**
     * @param label label that distinguishes this scan from others in the project
     * @return this
     */
    Definition withLabel(String label);

    /**
     * @return new started scan
     * @throws IOException when an IO error occurs while making the request to the Contrast Scan API
     * @throws UnauthorizedException when Contrast rejects the credentials used to send the request
     * @throws ResourceNotFoundException when the requested resource does not exist
     * @throws HttpResponseException when Contrast rejects this request with an error code
     */
    Scan create() throws IOException;
  }

  /** @return ID of this scan */
  String id();

  /** @return scan status */
  ScanStatus status();

  /** @return error message for failed scan, or {@code null} if the scan has not failed */
  String errorMessage();

  /** @return true when the scan has completed, failed, or been canceled */
  boolean isFinished();

  /**
   * @return {@code CompletionStage} that resolves successfully with a {@code Scan} record when the
   *     scan has completed, or resolves exceptionally with a {@link ScanException} when the scan
   *     has failed or there was a problem communicating with the Contrast Scan API.
   */
  CompletionStage<Scan> await(ScheduledExecutorService scheduler);

  /**
   * Retrieves a scan's results in <a href="https://sarifweb.azurewebsites.net">SARIF</a>.
   *
   * @return {@link InputStream} for reading the SARIF file. The caller is expected to close the
   *     {@code InputStream}
   */
  InputStream sarif() throws IOException;

  /**
   * Retrieves and saves the scan's results (in <a
   * href="https://sarifweb.azurewebsites.net">SARIF</a>) to the specified file.
   *
   * @param file the file to which to save the results
   */
  void saveSarif(Path file) throws IOException;

  /**
   * Retrieves a summary of the scan results.
   *
   * @return {@link CompletionStage} that completes successfully when the scan has completed and the
   *     summary has been retrieved
   */
  ScanSummary summary() throws IOException;
}
