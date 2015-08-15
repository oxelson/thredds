/*
 * Copyright 1998-2015 John Caron and University Corporation for Atmospheric Research/Unidata
 *
 *  Portions of this software were developed by the Unidata Program at the
 *  University Corporation for Atmospheric Research.
 *
 *  Access and use of this software shall impose the following obligations
 *  and understandings on the user. The user is granted the right, without
 *  any fee or cost, to use, copy, modify, alter, enhance and distribute
 *  this software, and any derivative works thereof, and its supporting
 *  documentation for any purpose whatsoever, provided that this entire
 *  notice appears in all copies of the software, derivative works and
 *  supporting documentation.  Further, UCAR requests that the user credit
 *  UCAR/Unidata in any publications that result from the use of this
 *  software or in any product that includes this software. The names UCAR
 *  and/or Unidata, however, may not be used in any advertising or publicity
 *  to endorse or promote any products or commercial entity unless specific
 *  written permission is obtained from UCAR/Unidata. The user also
 *  understands that UCAR/Unidata is not obligated to provide the user with
 *  any support, consulting, training or assistance of any kind with regard
 *  to the use, operation and performance of this software nor to provide
 *  the user with any updates, revisions, new versions or "bug fixes."
 *
 *  THIS SOFTWARE IS PROVIDED BY UCAR/UNIDATA "AS IS" AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL UCAR/UNIDATA BE LIABLE FOR ANY SPECIAL,
 *  INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 *  FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 *  NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION
 *  WITH THE ACCESS, USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package ucar.nc2.ft2.coverage;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Describes a subset of a Coverage.
 * Coordinate values only, no indices.
 *
 * @author caron
 * @since 5/6/2015
 */
public class SubsetParams {
  public static final String latlonBB = "latlonBB";     // value = LatLonRect
  public static final String projBB = "projBB";         // value = ProjRect
  public static final String horizStride = "horizStride";  // value = Integer
  public static final String vertCoord = "vertCoord";   // value = Double
  public static final String vertIndex = "vertIndex";   // value = Integer    LOOK BAD
  public static final String timeRange = "timeRange";   // value = CalendarDateRange
  public static final String time = "time";             // value = CalendarDate
  public static final String timeWindow = "timeWindow"; // value = TimeDuration
  public static final String timeOffset = "timeOffset"; // value = Double
  public static final String timeStride = "timeStride"; // value = Integer
  public static final String allTimes = "allTimes";     // value = Boolean
  public static final String latestTime = "latestTime"; // value = Boolean
  public static final String runtime = "runtime";       // value = CalendarDate
  public static final String runtimeRange = "runtimeRange"; // value = CalendarDateRange
  public static final String latestRuntime = "latestRuntime"; // value = Boolean
  public static final String ensCoord = "ensCoord";     // value = double ??

  private final Map<String, Object> req = new HashMap<>();

  public Set<Map.Entry<String, Object>> getEntries() {
    return req.entrySet();
  }
  public Set<String> getKeys() {
    return req.keySet();
  }

  public SubsetParams set(String key, Object value) {
    req.put(key, value);
    return this;
  }

  public Object get(String key) {
     return req.get(key);
   }

  public boolean isTrue(String key) {
     Boolean val = (Boolean) req.get(key);
    return (val != null) && val;
   }

  public Double getDouble(String key) {
    Object val = req.get(key);
    if (val == null) return null;
    Double dval;
    if (val instanceof Double) dval = (Double) val;
    else dval = Double.parseDouble((String) val);
    return dval;
  }

  public Integer getInteger(String key) {
    Object val = req.get(key);
    if (val == null) return null;
    Integer dval;
    if (val instanceof Integer) dval = (Integer) val;
    else dval = Integer.parseInt((String) val);
    return dval;
  }

  @Override
  public String toString() {
    Formatter f = new Formatter();
    for (Map.Entry<String,Object> entry : req.entrySet())
      f.format(" %s == %s%n", entry.getKey(), entry.getValue());
    return f.toString();
  }
}
