/*
 * Copyright (c) 2013, Contrast Security, Inc.
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
 * Neither the name of the Contrast Security, Inc. nor the names of its contributors may
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

package com.contrastsecurity.sdk;

import com.contrastsecurity.sdk.traces.Trace;
import com.contrastsecurity.sdk.traces.Traces;
import com.google.gson.Gson;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@XmlRootElement(name = "app")
public class App {

    /**
     * Instantiation for iterative setup.
     */
    public App() {
    }

    /**
     * Instantiation for setup when the four required variables are known at once.
     *
     * @param ssID      String Application ID
     * @param ssLicense String License #
     * @param ssPath    String Path Value
     * @param ssName    String Application Name
     */
    public App(String ssID, String ssLicense, String ssPath, String ssName) {
        id = ssID;
        license = ssLicense;
        path = ssPath;
        name = ssName;
    }

    public void setAppStats(AppStats casd) {
        this.data = casd;
    }

    public AppStats getAppStats() {
        return this.data;
    }

    private AppStats data = null;

    /**
     * Method to get the ID of the Application this object represents.
     *
     * @return String
     */
    @XmlElement(name = "id")
    public String getID() {
        return this.id;
    }

    private String id = null;

    /**
     * Method to get the License # of the Application this object represents.
     *
     * @return String
     */
    @XmlElement(name = "license")
    public String getLicense() {
        return this.license;
    }

    private String license = null;

    /**
     * Method to get the Path Value of the Application this object represents.
     *
     * @return String
     */
    @XmlElement(name = "path")
    public String getPath() {
        return this.path;
    }

    private String path = null;

    /**
     * Method to get the Name of the Application this object represents.
     *
     * @return String
     */
    @XmlElement(name = "name")
    public String getName() {
        return this.name;
    }

    private String name = null;

    /**
     * Method to get the total number of Findings that are currently set up within this application object.
     *
     * @return int
     */
    public int getNumTraces() {
        return this.traces.size();
    }

    /**
     * Method to set the ID of the Application this object represents.
     *
     * @param s1
     */
    public void setID(String s1) {
        this.id = s1;
    }

    /**
     * Method to set the License # of the Application this object represents.
     *
     * @param s1
     */
    public void setLicense(String s1) {
        this.license = s1;
    }

    /**
     * Method to set the Path Value of the Application this object represents.
     *
     * @param s1
     */
    public void setPath(String s1) {
        this.path = s1;
    }

    /**
     * Method to set the Name of the Application this object represents.
     *
     * @param s1
     */
    public void setName(String s1) {
        this.name = s1;
    }

    /**
     * Method to set the ContrastTraceData ArrayList of the Application this object represents.
     *
     * @param traces
     */
    public void setTraceList(Traces traces) {
        this.traces = traces.getTraceList();
    }

    /**
     * Method to get the ContrastTraceData ArrayList of the Application this object represents.
     *
     * @return ArrayList of ContrastTraceData
     */
    public List<Trace> getTraces() {
        return this.traces;
    }

    private List<Trace> traces = new ArrayList<Trace>();

    /**
     * Returns a HashMap of String-ArrayList values which represent the unique
     * types of Findings contained within this Application.<br/><br />
     * Example:<br />
     * stored-xss =&gt; ArrayList&lt;ContrastTraceData&gt;<br />
     * reflected-xss =&gt; ArrayList&lt;ContrastTraceData&gt;<br />
     * etc.<br /><br />
     * The ArrayLists contain each Trace object of the specified string type.
     *
     * @return HashMap&lt;String,ArrayList&lt;ContrastTraceData&gt;&gt;
     */
    public Map<String, ArrayList<Trace>> getUniqueTraceTypes() {
        HashMap<String, ArrayList<Trace>> TraceTypes =
                new HashMap<String, ArrayList<Trace>>();
        ArrayList<Trace> temp;
        for (Trace tdo : traces) {
            if (!TraceTypes.containsKey(tdo.getRule())) {
                temp = new ArrayList<Trace>();
            } else {
                temp = TraceTypes.get(tdo.getRule());
            }
            temp.add(tdo);
            TraceTypes.put(tdo.getRule(), temp);
        }
        return TraceTypes;
    }

    /**
     * Method to determine if the Application object is sufficiently populated to represent an
     * Application that is being tracked by Contrast.
     *
     * @return boolean
     */
    public boolean isAppDataPopulated() {
        boolean dataPopulated = false;

        if ((id != null && !id.equals("")) &&
                (license != null && !license.equals("")) &&
                (path != null && !path.equals("")) &&
                (name != null && !name.equals(""))) {
            dataPopulated = true;
        }
        return dataPopulated;
    }

    /**
     * Customized toString method to allow proper printing of the object to a String
     *
     * @return String
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
