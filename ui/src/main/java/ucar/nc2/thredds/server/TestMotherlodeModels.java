package ucar.nc2.thredds.server;

import ucar.nc2.ui.StopButton;
import ucar.nc2.thredds.ThreddsDataFactory;
import ucar.nc2.dataset.NetcdfDataset;

import java.io.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;

import thredds.catalog.crawl.CatalogCrawler;
import thredds.catalog.InvCatalogImpl;
import thredds.catalog.InvDataset;
import thredds.catalog.InvCatalogFactory;
import thredds.catalog.ThreddsMetadata;

import javax.swing.*;

public class TestMotherlodeModels implements CatalogCrawler.Listener {
  private String catUrl;
  private int type;
  private boolean skipDatasetScan;

  private InvCatalogFactory catFactory = InvCatalogFactory.getDefaultFactory(true);
  private ThreddsDataFactory tdataFactory = new ThreddsDataFactory();

  private StopButton stopButton;
  private JLabel label;
  private PrintStream out;
  private int countDatasets, countNoAccess, countNoOpen;
  private boolean verbose = true;

  TestMotherlodeModels(String name, String catURL, int type, boolean skipDatasetScan) throws IOException {
    this.catUrl = catURL;
    this.type = type;
    this.skipDatasetScan = skipDatasetScan;

    JPanel p = new JPanel();
    p.setBorder( BorderFactory.createLineBorder(Color.black ));

    p.add(new JLabel(name+":"));
    label = new JLabel();
    p.add(label);
    stopButton = new StopButton("stopit goddammit!");
    p.add(stopButton);
    main.add( p);

    //FileOutputStream fout = new FileOutputStream(name+".txt");
    out = System.out; // new PrintStream( new BufferedOutputStream( fout));

  }

  public void extract() throws IOException {

    out.println("Read " + catUrl);

    InvCatalogImpl cat = catFactory.readXML(catUrl);
    StringBuffer buff = new StringBuffer();
    boolean isValid = cat.check(buff, false);
    if (!isValid) {
      System.out.println("***Catalog invalid= " + catUrl + " validation output=\n" + buff);
      out.println("***Catalog invalid= " + catUrl + " validation output=\n" + buff);
      return;
    }
    out.println("catalog <" + cat.getName() + "> is valid");
    out.println(" validation output=\n" + buff);

    countDatasets = 0;
    countNoAccess = 0;
    countNoOpen = 0;
    int countCatRefs = 0;
    CatalogCrawler crawler = new CatalogCrawler(type, skipDatasetScan, this);
    long start = System.currentTimeMillis();
    try {
      countCatRefs = crawler.crawl(cat, stopButton, verbose ? out : null);
    } finally {
      int took = (int) (System.currentTimeMillis() - start) / 1000;

      out.println("***Done " + catUrl + " took = " + took + " secs\n" +
              "   datasets=" + countDatasets + " no access=" + countNoAccess + " open failed=" + countNoOpen + " total catalogs=" + countCatRefs);

    }
  }

  public void getDataset(InvDataset ds) {
    countDatasets++;

    ThreddsMetadata.GeospatialCoverage gc = ds.getGeospatialCoverage();
    //assert gc != null;

    NetcdfDataset ncd = null;
    try {
      StringBuffer log = new StringBuffer();
      ncd = tdataFactory.openDataset( ds,  false, null, log);

      if (ncd == null)
        out.println("**** failed= "+ds.getName()+" err="+log);
      else if (verbose)
        out.println("   "+ds.getName()+" ok");

    } catch (IOException e) {
      out.println("**** failed= "+ds.getName()+" err= "+e.getMessage());
    } finally {
      if (ncd != null) try {
        ncd.close();
      } catch (IOException e) {
      }
    }

  }

  public static JPanel main;
  public static void main(String args[]) throws IOException {
    //String server = "http://thredds.cise-nsf.gov:8080/thredds/";
    String server = "http://motherlode.ucar.edu:9080/thredds";
    //String server = "http://lead1.unidata.ucar.edu:8080/thredds";
    String catalog = "/idd/models.xml";
    String catalog2 = "/idd/rtmodel.xml";

    //"http://motherlode.ucar.edu:9080/thredds/idd/models_old.xml"

    // HEY LOOK
    //ucar.nc2.dods.DODSNetcdfFile.setAllowSessions( true);

    JFrame frame = new JFrame("TestTDS");
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    main = new JPanel();
    main.setLayout( new BoxLayout(main, BoxLayout.Y_AXIS));

    TestMotherlodeModels all_models = new TestMotherlodeModels("models", server+catalog, CatalogCrawler.USE_RANDOM_DIRECT, false);
    //TestMotherlodeModels uni_models = new TestMotherlodeModels("model2", server+catalog2, CatalogCrawler.USE_RANDOM_DIRECT, false);
    // TestMotherlodeModels all_models = new TestMotherlodeModels("models", server+catalog, CatalogCrawler.USE_ALL_DIRECT, false);

    frame.getContentPane().add(main);
    frame.pack();
    frame.setLocation(40, 300);
    frame.setVisible(true);

    all_models.extract();
    //uni_models.extract();
  }

}
