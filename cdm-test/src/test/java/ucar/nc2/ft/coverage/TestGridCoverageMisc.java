/* Copyright */
package ucar.nc2.ft.coverage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.CoordinateAxis1DTime;
import ucar.nc2.dt.GridCoordSystem;
import ucar.nc2.dt.GridDatatype;
import ucar.nc2.dt.grid.GridDataset;
import ucar.nc2.ft2.coverage.*;
import ucar.nc2.time.CalendarDate;
import ucar.unidata.test.util.NeedsCdmUnitTest;
import ucar.unidata.test.util.TestDir;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Read specific Grid Coverage fields and compare results with dt.GridDataset.
 * Generally the ones that are failing in TestGridCoverageReading
 *
 * @author caron
 * @since 7/17/2015
 */
@RunWith(Parameterized.class)
@Category(NeedsCdmUnitTest.class)
public class TestGridCoverageMisc {

  @Parameterized.Parameters(name = "{0}")
  public static List<Object[]> getTestParameters() {
    List<Object[]> result = new ArrayList<>();

    //result.add(new Object[]{"C:/data/gfsanl_3_20040302_1800_000.grb", null, "Geopotential_height_zeroDegC_isotherm", null, null, "2004-03-02T18:00:00Z", null, null});
    //result.add(new Object[]{"C:/data/gfsanl_3_20040302_1800_000.grb", null, "Vertical_velocity_pressure_isobaric", null, null, "2004-03-02T18:00:00Z", null, null});
    //result.add(new Object[]{"C:/data/gfsanl_3_20040302_1800_000.grb", null, "Vertical_velocity_pressure_isobaric", null, null, "2004-03-02T18:00:00Z", 400.0, null});

    //    Slice runtime=2015-03-01T06:00:00Z (1) ens=0.000000 (-1) time=2015-03-01T03:00:00Z (1) vert=0.000000 (-1)
    result.add(new Object[]{TestDir.cdmUnitTestDir + "gribCollections/gfs_2p5deg/gfs_2p5deg.ncx3", CoverageCoordSys.Type.Fmrc,  "Momentum_flux_u-component_surface_Mixed_intervals_Average",
            "2015-03-01T06:00:00Z", null, "2015-03-01T03:00:00Z", null, 46});

    result.add(new Object[]{TestDir.cdmUnitTestDir + "ft/fmrc/rtofs/ofs.20091122/ofs_atl.t00z.F024.grb.grib2", null, "Vertical_velocity_geometric_depth_below_sea", null, null, "2009-11-23T00:00:00Z", null, null});

    // Test Dataset Q:/cdmUnitTest/gribCollections/gfs_2p5deg/gfs_2p5deg.ncx3  Grid TwoD/Total_ozone_entire_atmosphere_single_layer
    //  Slice runtime=2015-03-01T06:00:00Z (1) ens=0.000000 (-1) time=2015-03-01T00:00:00Z (0) vert=0.000000 (-1)
    result.add(new Object[]{TestDir.cdmUnitTestDir + "gribCollections/gfs_2p5deg/gfs_2p5deg.ncx3", CoverageCoordSys.Type.Fmrc,  "Total_ozone_entire_atmosphere_single_layer",
            "2015-03-01T06:00:00Z", null, "2015-03-01T12:00:00Z", null, null});
    // Slice runtime=2015-03-01T06:00:00Z (1) ens=0.000000 (-1) time=2015-03-01T03:00:00Z (1) vert=0.000000 (-1)
    result.add(new Object[]{TestDir.cdmUnitTestDir + "gribCollections/gfs_2p5deg/gfs_2p5deg.ncx3", CoverageCoordSys.Type.Fmrc,  "Total_ozone_entire_atmosphere_single_layer",
            "2015-03-01T12:00:00Z", null, "2015-03-01T12:00:00Z", null, null});
    //    Slice runtime=2015-03-01T18:00:00Z (3) ens=0.000000 (-1) time=2015-03-17T18:00:00Z (92) vert=30000.000000 (9)
    result.add(new Object[]{TestDir.cdmUnitTestDir + "gribCollections/gfs_2p5deg/gfs_2p5deg.ncx3", CoverageCoordSys.Type.Fmrc,  "Total_ozone_entire_atmosphere_single_layer",
            "2015-03-01T18:00:00Z", null, "2015-03-17T18:00:00Z", null, null});
    //    Slice runtime=2015-03-01T00:00:00Z (0) ens=0.000000 (-1) time=2015-03-06T19:30:00Z (46) vert=0.000000 (-1)
    result.add(new Object[]{TestDir.cdmUnitTestDir + "gribCollections/gfs_2p5deg/gfs_2p5deg.ncx3", CoverageCoordSys.Type.Fmrc,  "Momentum_flux_u-component_surface_Mixed_intervals_Average",
            "2015-03-01T00:00:00Z", null, "2015-03-06T19:30:00Z", null, 46});

    result.add(new Object[]{TestDir.cdmUnitTestDir + "ft/coverage/03061219_ruc.nc",  null, "RH_lpdg", null, null, "2003-06-12T19:00:00Z", 150.0, null});  // NUWG - has CoordinateAlias
    result.add(new Object[]{TestDir.cdmUnitTestDir + "gribCollections/tp/GFS_Global_onedeg_ana_20150326_0600.grib2.ncx3",  null, "Relative_humidity_sigma_layer", "2015-03-26T06:00:00Z", null, "2015-03-26T06:00:00Z", 0.580000, null}); // SRC                               // TP
    result.add(new Object[]{TestDir.cdmUnitTestDir + "gribCollections/tp/GFS_Global_onedeg_ana_20150326_0600.grib2.ncx3",  null, "Relative_humidity_sigma_layer", "2015-03-26T06:00:00Z", null, "2015-03-26T06:00:00Z", 0.665000, null}); // SRC                               // TP
    result.add(new Object[]{TestDir.cdmUnitTestDir + "gribCollections/tp/GFS_Global_onedeg_ana_20150326_0600.grib2.ncx3",  null, "Absolute_vorticity_isobaric", "2015-03-26T06:00:00Z", null, "2015-03-26T06:00:00Z", 100000.0, null}); // SRC                               // TP
    result.add(new Object[]{TestDir.cdmUnitTestDir + "ft/coverage/Run_20091025_0000.nc", null, "elev",  null, null, "2009-10-24T04:14:59.121Z", null, null});  // x,y axis but no projection
    // */
    return result;
  }

  String endpoint;
  CoverageCoordSys.Type type;
  String covName, gridName;
  CalendarDate rt_val;
  Double ens_val;
  CalendarDate time_val;
  Double vert_val;
  Integer time_idx;

  public TestGridCoverageMisc(String endpoint, CoverageCoordSys.Type type, String covName, String rt_val, Double ens_val, String time_val, Double vert_val, Integer time_idx) {
    this.endpoint = endpoint;
    this.type = type;
    this.covName = covName;
    this.gridName = (type == CoverageCoordSys.Type.Fmrc) ? "TwoD/"+ covName : covName;
    this.rt_val = rt_val == null ? null : CalendarDate.parseISOformat(null, rt_val);
    this.ens_val = ens_val;
    this.time_val = time_val == null ? null : CalendarDate.parseISOformat(null, time_val);
    this.vert_val = vert_val;
    this.time_idx = time_idx;
  }

  @Test
  public void testReadGridCoverageSlice() throws IOException {    // read single slice
    System.out.printf("Test Dataset %s coverage %s%n", endpoint, covName);

    try (CoverageDatasetCollection cc = CoverageDatasetFactory.open(endpoint)) {
      Assert.assertNotNull(endpoint, cc);
      CoverageDataset gcs = (type == null) ? cc.getCoverageDatasets().get(0) : cc.findCoverageDataset(type);
      Assert.assertNotNull("gcs", gcs);
      Coverage cover = gcs.findCoverage(covName);
      Assert.assertNotNull(covName, cover);

      // check DtCoverageCS
      try (GridDataset ds = GridDataset.open(endpoint)) {
        GridDatatype dt = ds.findGridByName(gridName);
        Assert.assertNotNull(gridName, dt);

        GridCoordSystem csys = dt.getCoordinateSystem();
        CoordinateAxis1DTime rtAxis = csys.getRunTimeAxis();
        CoordinateAxis1D ensAxis = csys.getEnsembleAxis();
        CoordinateAxis1DTime timeAxis = csys.getTimeAxis1D();
        CoordinateAxis1D vertAxis = csys.getVerticalAxis();

        int calcTimeIdx = -1;
        int rt_idx = (rtAxis == null || rt_val == null) ? -1 : rtAxis.findTimeIndexFromCalendarDate(rt_val);
        if (time_idx == null) {
          if (time_val != null) {
            if (timeAxis != null)
              calcTimeIdx = timeAxis.findTimeIndexFromCalendarDate(time_val);
            else if (rt_idx >= 0) {
              timeAxis = csys.getTimeAxisForRun(rt_idx);
              if (timeAxis != null)
                calcTimeIdx = timeAxis.findTimeIndexFromCalendarDate(time_val);  // LOOK theres a bug here, set time_idx as workaround
            }
          }
        } else {
          calcTimeIdx = time_idx;
        }

        int ens_idx = (ensAxis == null || ens_val == null) ? -1 : ensAxis.findCoordElement(ens_val);
        int vert_idx = (vertAxis == null || vert_val == null) ? -1 : vertAxis.findCoordElement(vert_val);


        /* static void readAllVertLevels(Coverage cover, GridDatatype dt, CalendarDate rt_val, int rt_idx, CalendarDate time_val, int time_idx,
        //                              double ens_val, int ens_idx, CoordinateAxis1D vertAxis)
        TestGridCoverageReading.readAllVertLevels(cover, dt, rt_val, rt_idx, time_val, calcTimeIdx,
                 ens_val == null ? 0 : ens_val, ens_idx,
                vertAxis); // */

        TestGridCoverageReading.readOneSlice(cover, dt,
                rt_val, rt_idx,
                time_val, calcTimeIdx,
                ens_val == null ? 0 : ens_val, ens_idx,
                vert_val == null ? 0 : vert_val, vert_idx);  // */

      }
    }
  }

}
