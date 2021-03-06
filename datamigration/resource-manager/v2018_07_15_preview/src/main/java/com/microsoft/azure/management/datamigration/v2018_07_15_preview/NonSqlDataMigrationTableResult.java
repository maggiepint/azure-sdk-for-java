/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.datamigration.v2018_07_15_preview;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object used to report the data migration results of a table.
 */
public class NonSqlDataMigrationTableResult {
    /**
     * Result code of the data migration. Possible values include: 'Initial',
     * 'Completed', 'ObjectNotExistsInSource', 'ObjectNotExistsInTarget',
     * 'TargetObjectIsInaccessible', 'FatalError'.
     */
    @JsonProperty(value = "resultCode", access = JsonProperty.Access.WRITE_ONLY)
    private DataMigrationResultCode resultCode;

    /**
     * Name of the source table.
     */
    @JsonProperty(value = "sourceName", access = JsonProperty.Access.WRITE_ONLY)
    private String sourceName;

    /**
     * Name of the target table.
     */
    @JsonProperty(value = "targetName", access = JsonProperty.Access.WRITE_ONLY)
    private String targetName;

    /**
     * Number of rows in the source table.
     */
    @JsonProperty(value = "sourceRowCount", access = JsonProperty.Access.WRITE_ONLY)
    private Long sourceRowCount;

    /**
     * Number of rows in the target table.
     */
    @JsonProperty(value = "targetRowCount", access = JsonProperty.Access.WRITE_ONLY)
    private Long targetRowCount;

    /**
     * Time taken to migrate the data.
     */
    @JsonProperty(value = "elapsedTimeInMiliseconds", access = JsonProperty.Access.WRITE_ONLY)
    private Double elapsedTimeInMiliseconds;

    /**
     * List of errors, if any, during migration.
     */
    @JsonProperty(value = "errors", access = JsonProperty.Access.WRITE_ONLY)
    private List<DataMigrationError> errors;

    /**
     * Get result code of the data migration. Possible values include: 'Initial', 'Completed', 'ObjectNotExistsInSource', 'ObjectNotExistsInTarget', 'TargetObjectIsInaccessible', 'FatalError'.
     *
     * @return the resultCode value
     */
    public DataMigrationResultCode resultCode() {
        return this.resultCode;
    }

    /**
     * Get name of the source table.
     *
     * @return the sourceName value
     */
    public String sourceName() {
        return this.sourceName;
    }

    /**
     * Get name of the target table.
     *
     * @return the targetName value
     */
    public String targetName() {
        return this.targetName;
    }

    /**
     * Get number of rows in the source table.
     *
     * @return the sourceRowCount value
     */
    public Long sourceRowCount() {
        return this.sourceRowCount;
    }

    /**
     * Get number of rows in the target table.
     *
     * @return the targetRowCount value
     */
    public Long targetRowCount() {
        return this.targetRowCount;
    }

    /**
     * Get time taken to migrate the data.
     *
     * @return the elapsedTimeInMiliseconds value
     */
    public Double elapsedTimeInMiliseconds() {
        return this.elapsedTimeInMiliseconds;
    }

    /**
     * Get list of errors, if any, during migration.
     *
     * @return the errors value
     */
    public List<DataMigrationError> errors() {
        return this.errors;
    }

}
