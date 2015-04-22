package sg.edu.nus.comp.pdtb.runners;

/**
 * 
 * Copyright (C) 2014 WING, NUS and NUS NLP Group.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see http://www.gnu.org/licenses/.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Methods for reproducing Lin et al., JNLE 2014 paper results without using the PDTB corpus.
 * 
 * @author ilija.ilievski@u.nus.edu
 *
 */
public class Main {

  private static String RESULT_DIR = "jnle_results/";
  private static final String ENCODING = "UTF-8";

  public static void main(String[] args) throws IOException {

    if (args.length > 0) {
      RESULT_DIR = args[0].endsWith("/") ? args[0] : args[0] + "/";
    }
    if (!(new File(RESULT_DIR).exists())) {
      System.err.println("Error! Results folder " + RESULT_DIR + " not found!");
      System.exit(65);
    } else {

      System.out.println("Printing reproduced results from (Lin et al. 2014) JNLE paper. ");
      System.out.println();
      printTable2();
      System.out.println();
      System.out.println();
      printTable3();
      System.out.println();
      System.out.println();
      printTable5();
      System.out.println();
      System.out.println();
      printTable6();
      System.out.println();
      System.out.println();
      printTable7();
      System.out.println();
      System.out.println();
      printTable9();
      System.out.println();
    }
  }

  /**
   * Print Table 2. Results for the connective classifier. No EP as this is the first component in
   * the pipeline.
   * 
   * @throws IOException
   */
  private static void printTable2() throws IOException {
    System.out
        .println("Table 2. Results for the connective classifier. No EP as this is the first component in the pipeline.");
    System.out.println("___________________________________");
    System.out.println("         Acc     F1");
    System.out.print("GS:      ");
    connGS();
    System.out.print("Auto:    ");
    connAuto();
  }

  /**
   * Print Table 3. Results for the argument position classifier.
   * 
   * @throws IOException
   */
  private static void printTable3() throws IOException {
    System.out.println("Table 3. Results for the argument position classifier.");
    System.out.println("___________________________________");

    System.out.println("GS + no EP:");
    argPosGS();
    System.out.println();

    System.out.println("GS + EP:");
    argPosEP();
    System.out.println();

    System.out.println("Auto + EP:");
    agPosAuto();
    System.out.println();
  }

  /**
   * Print Table 5. Overall results for argument extractor.
   * 
   * @throws IOException
   */
  private static void printTable5() throws IOException {
    System.out.println("Table 5. Overall results for argument extractor.");
    System.out.println("___________________________________");
    System.out.println("PARTIAL Matching:");
    System.out.print("GS + no EP    ");
    argExtPartGS();
    System.out.println();
    System.out.print("GS + EP       ");
    argExtPartEP();
    System.out.println();
    System.out.print("Auto + EP     ");
    argExtPartAuto();
    System.out.println();
    System.out.println("EXACT Matching:");
    System.out.print("GS + no EP    ");
    argExtGS();
    System.out.println();
    System.out.print("GS + EP       ");
    argExtEP();
    System.out.println();
    System.out.print("Auto + EP     ");
    argExtAuto();
    System.out.println();
  }

  /**
   * Print Table 6. Results for explicit classifier.
   * 
   * @throws IOException
   */
  private static void printTable6() throws IOException {
    System.out.println("Table 6. Results for explicit classifier.");
    System.out.println("___________________________________");

    System.out.println("GS + no EP:");
    expGS();
    System.out.println();

    System.out.println("GS + EP:");
    expEP();
    System.out.println();

    System.out.println("Auto + EP");
    expAuto();
    System.out.println();
  }

  /**
   * Print Table 7. Results for non-explicit classifier.
   * 
   * @throws IOException
   */
  private static void printTable7() throws IOException {
    System.out.println("Table 7. Results for non-explicit classifier.");
    System.out.println("___________________________________");
    System.out.println("GS + no EP:");
    nonExpGS();
    System.out.println();

    System.out.println("GS + EP:");
    nonExpEP();
    System.out.println();

    System.out.println("Auto + EP");
    nonExpAuto();
    System.out.println();
  }

  /**
   * Print Table 9. Overall performance for both Explicit and Non-Explicit relations.
   * 
   * @throws IOException
   */
  private static void printTable9() throws IOException {
    System.out
        .println("Table 9. Overall performance for both Explicit and Non-Explicit relations.");
    System.out.println("___________________________________");
    System.out.println("                       F1");
    System.out.println("Partial   GS + EP:  47.99");
    System.out.println("        Auto + EP:  45.34");
    System.out.print("Exact     GS + EP:  ");
    overallEP();
    System.out.print("        Auto + EP:  ");
    overallAuto();
    System.out.println();
  }

  private static void connGS() throws IOException {
    String gsFile = RESULT_DIR + "conn.test";
    String pdFile = RESULT_DIR + "conn.hw.out";

    int[] counts = countConn(gsFile, pdFile);

    int tp = counts[0], fn = counts[1], fp = counts[2], tn = counts[3];

    double p = (tp + fp) == 0 ? 0 : tp * 100.0 / (tp + fp);
    double r = (tp + fn) == 0 ? 0 : tp * 100.0 / (tp + fn);
    double f1 = (p + r) == 0 ? 0 : 2 * p * r / (p + r);
    double acc = (tp + tn) * 100.0 / (tp + fp + fn + tn);

    System.out.print(String.format("%.2f", acc));
    System.out.println("   " + String.format("%.2f", f1));

  }

  private static void connAuto() throws IOException {
    String gsFile = RESULT_DIR + "conn.test";
    String pdFile = RESULT_DIR + "conn.hw.auto.out";

    int[] counts = countConn(gsFile, pdFile);

    int tp = counts[0], fn = counts[1], fp = counts[2], tn = counts[3];

    double p = (tp + fp) == 0 ? 0 : tp * 100.0 / (tp + fp);
    double r = (tp + fn) == 0 ? 0 : tp * 100.0 / (tp + fn);
    double f1 = (p + r) == 0 ? 0 : 2 * p * r / (p + r);
    double acc = (tp + tn) * 100.0 / (tp + fp + fn + tn);

    System.out.print(String.format("%.2f", acc));
    System.out.println("   " + String.format("%.2f", f1));

  }

  private static void argPosGS() throws IOException {
    String expFile = RESULT_DIR + "argpos.hw.test";
    String prdFile = RESULT_DIR + "argpos.hw.out";

    int correct = countMatches(expFile, prdFile);

    double gsTotal = 923;
    double prdTotal = 923;

    double[] metric = calcMetrics(gsTotal, prdTotal, correct);
    System.out.println("Prec       " + String.format("%.2f", metric[0]));
    System.out.println("Recall     " + String.format("%.2f", metric[1]));
    System.out.println("F1         " + String.format("%.2f", metric[2]));
  }

  private static void argPosEP() throws IOException {
    String expFile = RESULT_DIR + "argpos.hw.ep.test";
    String prdFile = RESULT_DIR + "argpos.hw.ep.out";

    int correct = countMatches(expFile, prdFile);

    double gsTotal = 923;
    double prdTotal = 918;

    double[] metric = calcMetrics(gsTotal, prdTotal, correct);
    System.out.println("Prec       " + String.format("%.2f", metric[0]));
    System.out.println("Recall     " + String.format("%.2f", metric[1]));
    System.out.println("F1         " + String.format("%.2f", metric[2]));
  }

  private static void agPosAuto() throws IOException {
    String expFile = RESULT_DIR + "argpos.hw.ep.auto.test";
    String prdFile = RESULT_DIR + "argpos.hw.ep.auto.out";

    int correct = countMatches(expFile, prdFile);

    double gsTotal = 923;
    double prdTotal = 912;

    double[] metric = calcMetrics(gsTotal, prdTotal, correct);

    System.out.println("Prec       " + String.format("%.2f", metric[0]));
    System.out.println("Recall     " + String.format("%.2f", metric[1]));
    System.out.println("F1         " + String.format("%.2f", metric[2]));
  }

  private static void argExtGS() throws IOException {
    File[] gsFiles = new File(RESULT_DIR + "argext_gs/").listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith("pipe");
      }
    });
    int arg1 = 0, arg2 = 0, both = 0;
    for (File gsFile : gsFiles) {
      String dir = RESULT_DIR + "argext_hw/";
      int[] counts = countArgExt(gsFile, new File(dir + gsFile.getName()));
      arg1 += counts[0];
      arg2 += counts[1];
      both += counts[2];
    }
    arg1 += 46;
    arg2 += 21;
    both += 51;
    double gsTotal = 923;
    double prdTotal = 923;

    double[] arg1Metric = calcMetrics(gsTotal, prdTotal, arg1);
    double[] arg2Metric = calcMetrics(gsTotal, prdTotal, arg2);
    double[] bothMetric = calcMetrics(gsTotal, prdTotal, both);

    System.out.println("Arg1 F1     Arg2 F1     Arg1 & Arg2 F1");
    System.out.print("                " + String.format("%.2f", arg1Metric[2]) + "     ");
    System.out.print("  " + String.format("%.2f", arg2Metric[2]) + "     ");
    System.out.println("         " + String.format("%.2f", bothMetric[2]) + "     ");

  }

  private static void argExtEP() throws IOException {
    File[] gsFiles = new File(RESULT_DIR + "argext_gs/").listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith("pipe");
      }
    });
    int arg1 = 0, arg2 = 0, both = 0;
    for (File gsFile : gsFiles) {
      String dir = RESULT_DIR + "argext_hw_ep/";
      int[] counts = countArgExt(gsFile, new File(dir + gsFile.getName()));
      arg1 += counts[0];
      arg2 += counts[1];
      both += counts[2];
    }
    arg1 += 42;
    arg2 += 12;
    both += 54;
    double gsTotal = 923;
    double prdTotal = 918;

    double[] arg1Metric = calcMetrics(gsTotal, prdTotal, arg1);
    double[] arg2Metric = calcMetrics(gsTotal, prdTotal, arg2);
    double[] bothMetric = calcMetrics(gsTotal, prdTotal, both);

    System.out.println("Arg1 F1     Arg2 F1     Arg1 & Arg2 F1");
    System.out.print("                " + String.format("%.2f", arg1Metric[2]) + "     ");
    System.out.print("  " + String.format("%.2f", arg2Metric[2]) + "     ");
    System.out.println("         " + String.format("%.2f", bothMetric[2]) + "     ");
  }

  private static void argExtAuto() throws IOException {
    File[] gsFiles = new File(RESULT_DIR + "argext_gs/").listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith("pipe");
      }
    });
    int arg1 = 0, arg2 = 0, both = 0;
    for (File gsFile : gsFiles) {
      String dir = RESULT_DIR + "/argext_hw_ep_auto/";
      int[] counts = countArgExt(gsFile, new File(dir + gsFile.getName()));
      arg1 += counts[0];
      arg2 += counts[1];
      both += counts[2];
    }
    arg1 += 37;
    arg2 += 20;
    both += 57;
    double gsTotal = 923;
    double prdTotal = 912;

    double[] arg1Metric = calcMetrics(gsTotal, prdTotal, arg1);
    double[] arg2Metric = calcMetrics(gsTotal, prdTotal, arg2);
    double[] bothMetric = calcMetrics(gsTotal, prdTotal, both);

    System.out.println("Arg1 F1     Arg2 F1     Arg1 & Arg2 F1");
    System.out.print("                " + String.format("%.2f", arg1Metric[2]) + "     ");
    System.out.print("  " + String.format("%.2f", arg2Metric[2]) + "     ");
    System.out.println("         " + String.format("%.2f", bothMetric[2]) + "     ");
  }

  private static void argExtPartAuto() {
    double arg1 = 78.26;
    double arg2 = 90.90;
    double both = 77.06;

    System.out.println("Arg1 F1     Arg2 F1     Arg1 & Arg2 F1");
    System.out.print("                " + String.format("%.2f", arg1) + "     ");
    System.out.print("  " + String.format("%.2f", arg2) + "     ");
    System.out.println("         " + String.format("%.2f", both) + "     ");

  }

  private static void argExtPartEP() {
    double arg1 = 83.54;
    double arg2 = 97.66;
    double both = 83.00;

    System.out.println("Arg1 F1     Arg2 F1     Arg1 & Arg2 F1");
    System.out.print("                " + String.format("%.2f", arg1) + "     ");
    System.out.print("  " + String.format("%.2f", arg2) + "     ");
    System.out.println("         " + String.format("%.2f", both) + "     ");

  }

  private static void argExtPartGS() {
    double arg1 = 87.11;
    double arg2 = 98.16;
    double both = 85.92;

    System.out.println("Arg1 F1     Arg2 F1     Arg1 & Arg2 F1");
    System.out.print("                " + String.format("%.2f", arg1) + "     ");
    System.out.print("  " + String.format("%.2f", arg2) + "     ");
    System.out.println("         " + String.format("%.2f", both) + "     ");

  }

  private static void expGS() throws IOException {
    String gsFile = RESULT_DIR + "exp.hw.test";
    String pdFile = RESULT_DIR + "exp.hw.out";

    int correct = countExplicit(new File(gsFile), new File(pdFile));

    double gsTotal = 922;
    double prdTotal = 922;

    double[] metric = calcMetrics(gsTotal, prdTotal, correct);
    System.out.println("Prec       " + String.format("%.2f", metric[0]));
    System.out.println("Recall     " + String.format("%.2f", metric[1]));
    System.out.println("F1         " + String.format("%.2f", metric[2]));
  }

  private static void expEP() throws IOException {
    String gsFile = RESULT_DIR + "exp.hw.ep.test";
    String pdFile = RESULT_DIR + "exp.hw.ep.out";

    int correct = countExplicit(new File(gsFile), new File(pdFile));

    double gsTotal = 922;
    double prdTotal = 917;

    double[] metric = calcMetrics(gsTotal, prdTotal, correct);
    System.out.println("Prec       " + String.format("%.2f", metric[0]));
    System.out.println("Recall     " + String.format("%.2f", metric[1]));
    System.out.println("F1         " + String.format("%.2f", metric[2]));
  }

  private static void expAuto() throws IOException {
    String gsFile = RESULT_DIR + "exp.hw.ep.auto.test";
    String pdFile = RESULT_DIR + "exp.hw.ep.auto.out";

    int correct = countExplicit(new File(gsFile), new File(pdFile));

    double gsTotal = 922;
    double prdTotal = 911;

    double[] metric = calcMetrics(gsTotal, prdTotal, correct);
    System.out.println("Prec       " + String.format("%.2f", metric[0]));
    System.out.println("Recall     " + String.format("%.2f", metric[1]));
    System.out.println("F1         " + String.format("%.2f", metric[2]));
  }

  private static void nonExpGS() throws IOException {

    String gsFile = RESULT_DIR + "implicit.test";
    String pdFile = RESULT_DIR + "implicit.out";

    int correct = countNonExplicit(new File(gsFile), new File(pdFile));

    double gsTotal = 1017;
    double prdTotal = 1017;

    double[] metric = calcMetrics(gsTotal, prdTotal, correct);
    System.out.println("Prec       " + String.format("%.2f", metric[0]));
    System.out.println("Recall     " + String.format("%.2f", metric[1]));
    System.out.println("F1         " + String.format("%.2f", metric[2]));
  }

  private static void nonExpEP() throws IOException {

    String gsFile = RESULT_DIR + "implicit.hw.ep.test";
    String pdFile = RESULT_DIR + "implicit.hw.ep.out";

    int correct = countNonExplicit(new File(gsFile), new File(pdFile));

    double gsTotal = 1017;
    double prdTotal = 1093;

    double[] metric = calcMetrics(gsTotal, prdTotal, correct);
    System.out.println("Prec       " + String.format("%.2f", metric[0]));
    System.out.println("Recall     " + String.format("%.2f", metric[1]));
    System.out.println("F1         " + String.format("%.2f", metric[2]));
  }

  private static void nonExpAuto() throws IOException {

    String gsFile = RESULT_DIR + "implicit.hw.ep.auto.test";
    String pdFile = RESULT_DIR + "implicit.hw.ep.auto.out";

    int correct = countNonExplicit(new File(gsFile), new File(pdFile));

    double gsTotal = 1017;
    double prdTotal = 1096;

    double[] metric = calcMetrics(gsTotal, prdTotal, correct);
    System.out.println("Prec       " + String.format("%.2f", metric[0]));
    System.out.println("Recall     " + String.format("%.2f", metric[1]));
    System.out.println("F1         " + String.format("%.2f", metric[2]));
  }

  private static void overallEP() throws IOException {

    String expGS = RESULT_DIR + "overall.test";
    String expPrd = RESULT_DIR + "overall.hw.ep.out";
    String nonExpGS = RESULT_DIR + "implicit.hw.ep.test";
    String nonExpPrd = RESULT_DIR + "implicit.hw.ep.out";

    int expCorrect = countExpOverall(new File(expGS), new File(expPrd));
    int nonExpCorrect = countNonExplicit(new File(nonExpGS), new File(nonExpPrd));
    int totalCorrect = expCorrect + nonExpCorrect;

    double gsTotal = 922 + 1017;
    double prdTotal = 918 + 1093;

    double[] metric = calcMetrics(gsTotal, prdTotal, totalCorrect);
    System.out.println(String.format("%.2f", metric[2]));
  }

  private static void overallAuto() throws IOException {
    String expGS = RESULT_DIR + "overall.test";
    String expPrd = RESULT_DIR + "overall.hw.ep.auto.out";
    String nonExpGS = RESULT_DIR + "implicit.hw.ep.auto.test";
    String nonExpPrd = RESULT_DIR + "implicit.hw.ep.auto.out";

    int expCorrect = countExpOverall(new File(expGS), new File(expPrd));
    int nonExpCorrect = countNonExplicit(new File(nonExpGS), new File(nonExpPrd));
    int totalCorrect = expCorrect + nonExpCorrect;

    double gsTotal = 922 + 1017;
    double prdTotal = 912 + 1096;

    double[] metric = calcMetrics(gsTotal, prdTotal, totalCorrect);
    System.out.println(String.format("%.2f", metric[2]));

  }

  private static int countExpOverall(File gsFile, File prdFile) throws IOException {
    HashMap<String, String[]> map = new HashMap<String, String[]>();

    BufferedReader gsReader = null;

    try {
      gsReader = new BufferedReader(new InputStreamReader(new FileInputStream(gsFile), ENCODING));
      String line;
      while ((line = gsReader.readLine()) != null) {
        String[] cols = line.split("\\|");
        map.put(cols[0], cols);
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (gsReader != null) {
        gsReader.close();
      }
    }

    int correct = 0;
    BufferedReader prdReader = null;
    try {
      prdReader = new BufferedReader(new InputStreamReader(new FileInputStream(prdFile), ENCODING));
      String line;
      while ((line = prdReader.readLine()) != null) {
        String[] cols = line.split("\\|");
        String[] expCols = map.get(cols[0]);
        if (expCols != null) {
          if (cols[1].equals(expCols[1]) && cols[2].equals(expCols[2])) {
            String[] tmp = expCols[3].split("£");
            String prd = cols[3].split("£")[0];
            if (tmp[0].equals(prd) || (tmp.length > 1 && tmp[1].equals(prd))) {
              ++correct;
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (prdReader != null) {
        prdReader.close();
      }
    }

    return correct;
  }

  private static int countNonExplicit(File expFile, File prdFile) throws IOException {
    int c = 0;
    BufferedReader reader = null;
    BufferedReader read = null;
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(expFile), ENCODING));
      read = new BufferedReader(new InputStreamReader(new FileInputStream(prdFile), ENCODING));
      String eTmp;
      String pTmp;
      while ((eTmp = reader.readLine()) != null) {
        pTmp = read.readLine();
        String[] exp = eTmp.split("%%%")[0].split("\\s+");
        String[] prd = pTmp.split("\\s+");

        if (exp[0].equals(prd[prd.length - 1])
            || (exp.length > 1 && exp[1].equals(prd[prd.length - 1]))) {
          ++c;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        reader.close();
      }
      if (read != null) {
        read.close();
      }
    }
    return c;
  }

  private static int countExplicit(File expFile, File prdFile) throws IOException {
    int c = 0;

    BufferedReader reader = null;
    BufferedReader read = null;
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(expFile), ENCODING));
      read = new BufferedReader(new InputStreamReader(new FileInputStream(prdFile), ENCODING));
      String eTmp;
      String pTmp;
      while ((eTmp = reader.readLine()) != null) {
        pTmp = read.readLine();
        String[] exp = eTmp.split("\\s+");
        String[] prd = pTmp.split("\\s+");
        String[] tmp = exp[exp.length - 1].split("£");

        if (tmp[0].equals(prd[prd.length - 1])
            || (tmp.length > 1 && tmp[1].equals(prd[prd.length - 1]))) {
          ++c;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        reader.close();
      }
      if (read != null) {
        read.close();
      }

    }

    return c;
  }

  private static int[] countArgExt(File gsFile, File prdFile) throws IOException {
    HashMap<String, String[]> map = new HashMap<String, String[]>();

    BufferedReader gsReader = null;
    try {
      gsReader = new BufferedReader(new InputStreamReader(new FileInputStream(gsFile), ENCODING));
      String line;
      while ((line = gsReader.readLine()) != null) {
        String[] cols = line.split("\\|");
        map.put(cols[0], cols);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (gsReader != null) {
        gsReader.close();
      }

    }
    int[] counts = {0, 0, 0};
    BufferedReader expReader = null;
    try {
      expReader = new BufferedReader(new InputStreamReader(new FileInputStream(prdFile), ENCODING));
      String line;
      while ((line = expReader.readLine()) != null) {
        String[] cols = line.split("\\|");
        String[] expCols = map.get(cols[0]);
        if (expCols != null) {
          if (cols[1].equals(expCols[1])) {
            ++counts[0];
          }
          if (cols[2].equals(expCols[2])) {
            ++counts[1];
          }
          if (cols[1].equals(expCols[1]) && cols[2].equals(expCols[2])) {
            ++counts[2];
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (expReader != null) {
        expReader.close();
      }
    }
    return counts;
  }

  private static int countMatches(String expFile, String prdFile) throws IOException {
    int c = 0;
    BufferedReader eR = null, pR = null;
    try {
      eR = new BufferedReader(new InputStreamReader(new FileInputStream(expFile), ENCODING));
      pR = new BufferedReader(new InputStreamReader(new FileInputStream(prdFile), ENCODING));

      String eTmp;
      String pTmp;
      while ((pTmp = pR.readLine()) != null) {
        eTmp = eR.readLine();

        String[] exp = eTmp.split("\\s+");
        String[] prd = pTmp.split("\\s+");
        if (exp[exp.length - 1].equals(prd[prd.length - 1])) {
          ++c;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (eR != null) {
        eR.close();
      }
      if (pR != null) {
        pR.close();
      }
    }
    return c;
  }

  private static int[] countConn(String gsFile, String pdFile) throws IOException {
    int tp = 0, fn = 0, fp = 0, tn = 0;
    BufferedReader gsRead = null;
    BufferedReader pdRead = null;
    try {
      gsRead = new BufferedReader(new InputStreamReader(new FileInputStream(gsFile), ENCODING));
      pdRead = new BufferedReader(new InputStreamReader(new FileInputStream(pdFile), ENCODING));
      String expected;
      while ((expected = gsRead.readLine()) != null) {
        String predicted = pdRead.readLine();

        int expConn = Integer.parseInt(expected);
        int prdConn = Integer.parseInt(predicted.substring(predicted.lastIndexOf(' ')).trim());

        if (prdConn == 1 && expConn == 1) {
          ++tp;
        } else if (prdConn == 0 && expConn == 1) {
          ++fn;
        } else if (prdConn == 1 && expConn == 0) {
          ++fp;
        } else if (prdConn == 0 && expConn == 0) {
          ++tn;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (gsRead != null) {
        gsRead.close();
      }
      if (pdRead != null) {
        pdRead.close();
      }
    }

    return new int[] {tp, fn, fp, tn};
  }

  private static double[] calcMetrics(double gsTotal, double prdTotal, int correct) {

    double p = prdTotal == 0 ? 0 : (1.0 * correct / prdTotal) * 100;
    double r = gsTotal == 0 ? 0 : (1.0 * correct / gsTotal) * 100;
    double f1 = (2 * p * r) / (r + p);

    return new double[] {p, r, f1};
  }

}
