/* Copyright */
package ucar.nc2.ft.coverage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ucar.nc2.dataset.CoordinateSystem;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.ft2.coverage.CoverageDatasetCollection;
import ucar.nc2.ft2.coverage.CoverageCoordSys;
import ucar.nc2.ft2.coverage.CoverageDataset;
import ucar.nc2.ft2.coverage.CoverageDatasetFactory;
import ucar.nc2.ft2.coverage.adapter.DtCoverageCS;
import ucar.nc2.ft2.coverage.adapter.DtCoverageCSBuilder;
import ucar.nc2.ft2.coverage.adapter.DtCoverageDataset;
import ucar.unidata.test.util.NeedsCdmUnitTest;
import ucar.unidata.test.util.TestDir;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test GridCoverageDataset, and adapters
 *
 * @author caron
 * @since 5/28/2015
 */
@RunWith(Parameterized.class)
@Category(NeedsCdmUnitTest.class)
public class TestCoverageClassification {

  @Parameterized.Parameters(name = "{0}")
  public static List<Object[]> getTestParameters() {
    List<Object[]> result = new ArrayList<>();

    result.add(new Object[]{TestDir.cdmUnitTestDir + "ft/coverage/03061219_ruc.nc", CoverageCoordSys.Type.Grid, 4, 4, 31});  // NUWG - has CoordinateAlias
    result.add(new Object[]{TestDir.cdmUnitTestDir + "ft/coverage/ECME_RIZ_201201101200_00600_GB", CoverageCoordSys.Type.Grid, 4, 5, 5});  // scalar runtime
    result.add(new Object[]{TestDir.cdmUnitTestDir + "ft/coverage/MM_cnrm_129_red.ncml", CoverageCoordSys.Type.Fmrc, 6, 6, 1}); // ensemble, time-offset
    result.add(new Object[]{TestDir.cdmUnitTestDir + "ft/coverage/ukmo.nc", CoverageCoordSys.Type.Fmrc, 4, 5, 1});              // scalar vert
    result.add(new Object[]{TestDir.cdmUnitTestDir + "ft/coverage/testCFwriter.nc", CoverageCoordSys.Type.Grid, 3, 5, 4});  // both x,y and lat,lon

    result.add(new Object[]{TestDir.cdmUnitTestDir + "ft/coverage/Run_20091025_0000.nc", CoverageCoordSys.Type.Curvilinear, 4, 6, 22});  // x,y axis but no projection
    result.add(new Object[]{TestDir.cdmUnitTestDir + "ft/fmrc/rtofs/ofs.20091122/ofs_atl.t00z.F024.grb.grib2", CoverageCoordSys.Type.Curvilinear, 4, 5, 7});  // GRIB Curvilinear

    result.add(new Object[]{TestDir.cdmUnitTestDir + "gribCollections/tp/GFS_Global_onedeg_ana_20150326_0600.grib2.ncx3", CoverageCoordSys.Type.Grid, 4, 5, 65}); // SRC                               // TP

    return result;
  }

  String endpoint;
  CoverageCoordSys.Type expectType;
  int domain, range, ncoverages;


  public TestCoverageClassification(String endpoint, CoverageCoordSys.Type expectType, int domain, int range, int ncoverages) {
    this.endpoint = endpoint;
    this.expectType = expectType;
    this.domain = domain;
    this.range = range;
    this.ncoverages = ncoverages;
  }

  @Test
  public void testAdapter() throws IOException {

    try (DtCoverageDataset gds = DtCoverageDataset.open(endpoint)) {
      Assert.assertNotNull(endpoint, gds);
      Assert.assertEquals("NGrids", ncoverages, gds.getGrids().size());
      Assert.assertEquals(expectType, gds.getCoverageType());
    }

    // check DtCoverageCS
    try (NetcdfDataset ds = NetcdfDataset.openDataset(endpoint)) {
      DtCoverageCSBuilder builder = DtCoverageCSBuilder.classify(ds, null);
      Assert.assertNotNull(builder);
      DtCoverageCS cs = builder.makeCoordSys();
      Assert.assertEquals(expectType, cs.getCoverageType());
      Assert.assertEquals("NIndCoordAxes", domain, CoordinateSystem.makeDomain(cs.getCoordAxes()).size());
      Assert.assertEquals("NCoordAxes", range, cs.getCoordAxes().size());
    }
  }

  @Test
  public void testFactory() throws IOException {

    try (CoverageDatasetCollection cc = CoverageDatasetFactory.open(endpoint)) {
      assert cc != null;
      Assert.assertEquals(1, cc.getCoverageDatasets().size());
      CoverageDataset gds = cc.getCoverageDatasets().get(0);
      Assert.assertNotNull(endpoint, gds);
      Assert.assertEquals("NGrids", ncoverages, gds.getCoverageCount());
      Assert.assertEquals(expectType, gds.getCoverageType());
    }
  }

}
