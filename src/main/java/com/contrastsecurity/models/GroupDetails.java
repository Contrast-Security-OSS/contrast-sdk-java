/*
 * Copyright (c) 2014, Contrast Security, LLC.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * Neither the name of the Contrast Security, LLC. nor the names of its contributors may
 * be used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.contrastsecurity.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A Group Details in Contrast.
 */
public class GroupDetails {

    // Inner Class with the actual details returned for a Group
    public class Details {
        /* ddooley Inner Class for User(s) within the Group Details.
           I'd have preferred to re-use the User model, but the API returns different fields
           e.g. uid instead of uuid
        */
        public class GroupUser {
            /**
             * Return the uuid for this user (note the The User returned by the group API is different to the User API), e.g.:
             * @return the uuid of this user
             */
            public String getUUID() {
                return uuid;
            }
            @SerializedName("uid")
            private String uuid;
            /**
             * Return the last_name for this user, e.g.:
             * @return the lastName of this user
             */
            public String getLastName() { return lastName; }
            @SerializedName("last_name")
            private String lastName;

            /**
             * Return the first_name for this user, e.g.:
             * @return the firstName of this user
             */
            public String getFirstName() { return firstName; }
            @SerializedName("first_name")
            private String firstName;
        }
        /* ddooley Inner Classes for Application(s) and role within the Group Details
        * May be empty if no apps tied to the Group*/
        public class GroupApplications {
            /**
             * Return the role for these applications, e.g.:
             * @return the role for these application
             */
            public Role getRole() { return role; }
            @SerializedName("role")
            private Role role;
        }

        /**
         * Return the name for this group, e.g.:
         * @return the name of this group
         */
        public String getName() {
            return name;
        }
        @SerializedName("name")
        private String name;

        /**
         * Return the group user objects
         *
         * @return a list of users in the group
         */
        public List<GroupUser> getUsers() {
            return users;
        }
        @SerializedName("users")
        private List<GroupUser> users;

        /**
         * Return the group application objects
         *
         * @return a list of applications in the group
         */
        public List<GroupApplications> getApplications() {
            return applications;
        }
        @SerializedName("applications")
        private List<GroupApplications> applications;
    }

    /**
     * Return the details of the group
     * @return details of the group
     */
    public Details getDetails() {
        return details;
    }
    @SerializedName("group")
    private Details details;

}