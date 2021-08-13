package com.contrastsecurity.sdk.scan;

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

    /** @return new started scan */
    Scan create() throws IOException;
  }

  /** @return ID of this scan */
  String id();

  /** @return scan status */
  Status status();

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
   * Retrieves and scan's results in <a href="https://sarifweb.azurewebsites.net">SARIF</a>
   *
   * @return {@link InputStream} for reading the SARIF file. The caller is expected to close the
   *     {@code InputStream}
   */
  InputStream sarif();

  /**
   * Retrieves and saves the scan's results (in <a
   * href="https://sarifweb.azurewebsites.net">SARIF</a>) to the specified file
   *
   * @param file the file to which to save the results
   */
  void saveSarif(Path file);

  /**
   * Retrieves a summary of the scan results.
   *
   * @return {@link CompletionStage} that completes successfully when the scan has completed and the
   *     summary has been retrieved
   */
  ScanSummary summary();

  /** Describes the possible states that a scan can have */
  enum Status {
    WAITING,
    RUNNING,
    CANCELLED,
    COMPLETED,
    FAILED
  }
}
