/*
 *      Copyright (c) 2004-2014 Stuart Boston
 *
 *      This file is part of TheMovieDB API.
 *
 *      TheMovieDB API is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      TheMovieDB API is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with TheMovieDB API.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.omertron.themoviedbapi.model.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Stuart
 */
public class PersonCrew extends PersonBasic {

    private static final long serialVersionUID = 2L;

    @JsonProperty("department")
    private String department;
    @JsonProperty("job")
    private String job;

    public String getDepartment() {
        return department;
    }

    public String getJob() {
        return job;
    }

    public void setDepartment(String department) {
        this.department = StringUtils.trimToEmpty(department);
    }

    public void setJob(String job) {
        this.job = StringUtils.trimToEmpty(job);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        if (!super.equals(obj)) {
            return false;
        }

        final PersonCrew other = (PersonCrew) obj;

        if (!StringUtils.equals(this.department, other.department)) {
            return false;
        }
        return StringUtils.equals(this.job, other.job);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.getId();
        hash = 59 * hash + (this.department != null ? this.department.hashCode() : 0);
        hash = 59 * hash + (this.job != null ? this.job.hashCode() : 0);
        hash = 59 * hash + (this.getName() != null ? this.getName().hashCode() : 0);
        hash = 59 * hash + (this.getProfilePath() != null ? this.getProfilePath().hashCode() : 0);
        return hash;
    }
}
