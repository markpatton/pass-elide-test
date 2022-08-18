/*
 * Copyright 2018 Johns Hopkins University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eclipse.pass.elide.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.yahoo.elide.annotation.Include;

/**
 * Grant model for the PASS system
 *
 * @author Karen Hanson
 */

@Include
@Entity
public class Grant {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    /**
     * Award number from funder
     */
    private String awardNumber;

    /**
     * Status of award
     */
    @Enumerated(EnumType.STRING)
    private AwardStatus awardStatus;

    /**
     * A local key assigned to the Grant within the researcher's institution to support matching
     * between PASS and a local system. In the case of JHU this is the key assigned by COEUS
     */
    private String localKey;

    /**
     * Title of the research project
     */
    private String projectName;

    /**
     * The URI of the funder.id of the sponsor that is the original source of the funds
     */
    @ManyToOne
    private Funder primaryFunder;

    /**
     * The URI of the funder.id of the organization from which funds are directly received
     */
    @ManyToOne
    private Funder directFunder;

    /**
     * URI of the User who is the Principal investigator
     */
    @ManyToOne
    private User pi;

    /**
     * List of URIs of the [User] who are the co-principal investigators
     */
    @ManyToMany
    private List<User> coPis = new ArrayList<>();

    /**
     * Date the grant was awarded
     */
    private LocalDateTime awardDate;

    /**
     * Date the grant started
     */
    private LocalDateTime startDate;

    /**
     * Date the grant ended
     */
    private LocalDateTime endDate;

    /**
     * Status of award/grant
     */
    public enum AwardStatus {

        /**
         * Active award
         */
        ACTIVE("active"),

        /**
         * Pre-award
         */
        PRE_AWARD("pre-award"),

        /**
         * Terminated
         */
        TERMINATED("terminated");

        private static final Map<String, AwardStatus> map = new HashMap<>(values().length, 1);

        static {
            for (AwardStatus a : values()) {
                map.put(a.value, a);
            }
        }

        private String value;

        private AwardStatus(String value) {
            this.value = value;
        }

        /**
         * Parse award status
         *
         * @param status Serialized status
         * @return Parsed status
         */
        public static AwardStatus of(String status) {
            AwardStatus result = map.get(status);
            if (result == null) {
                throw new IllegalArgumentException("Invalid Award Status: " + status);
            }
            return result;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}