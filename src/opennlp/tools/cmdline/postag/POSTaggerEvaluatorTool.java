/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package opennlp.tools.cmdline.postag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import opennlp.tools.cmdline.AbstractEvaluatorTool;
import opennlp.tools.cmdline.ArgumentParser.OptionalParameter;
import opennlp.tools.cmdline.ArgumentParser.ParameterDescription;
import opennlp.tools.cmdline.CmdLineUtil;
import opennlp.tools.cmdline.TerminateToolException;
import opennlp.tools.cmdline.params.EvaluatorParams;
import opennlp.tools.cmdline.postag.POSTaggerEvaluatorTool.EvalToolParams;
import opennlp.tools.postag.POSEvaluator;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerEvaluationMonitor;

public final class POSTaggerEvaluatorTool
    extends AbstractEvaluatorTool<POSSample, EvalToolParams> {

  interface EvalToolParams extends EvaluatorParams {
    @ParameterDescription(valueName = "outputFile", description = "the path of the fine-grained report file.")
    @OptionalParameter
    File getReportOutputFile();
  }

  public POSTaggerEvaluatorTool() {
    super(POSSample.class, EvalToolParams.class);
  }

  @Override
public String getShortDescription() {
    return "Measures the performance of the POS tagger model with the reference data";
  }

  @Override
public void run(String format, String[] args) {
    super.run(format, args);

    POSModel model = new POSModelLoader().load(params.getModel());
    
    POSTaggerEvaluationMonitor missclassifiedListener = null;
    if (params.getMisclassified()) {
      missclassifiedListener = new POSEvaluationErrorListener();
    }

    POSTaggerFineGrainedReportListener reportListener = null;
    File reportFile = params.getReportOutputFile();
    OutputStream reportOutputStream = null;
    if (reportFile != null) {
      CmdLineUtil.checkOutputFile("Report Output File", reportFile);
      try {
        reportOutputStream = new FileOutputStream(reportFile);
        reportListener = new POSTaggerFineGrainedReportListener(
            reportOutputStream);
      } catch (FileNotFoundException e) {
        throw new TerminateToolException(-1,
            "IO error while creating POS Tagger fine-grained report file: "
                + e.getMessage());
      }
    }

    POSEvaluator evaluator = new POSEvaluator(
        new opennlp.tools.postag.POSTaggerME(model), missclassifiedListener,
        reportListener);

    System.out.print("Evaluating ... ");
    try {
      evaluator.evaluate(sampleStream);
    }
    catch (IOException e) {
      System.err.println("failed");
      throw new TerminateToolException(-1, "IO error while reading test data: " + e.getMessage(), e);
    } finally {
      try {
        sampleStream.close();
      } catch (IOException e) {
        // sorry that this can fail
      }
    }

    System.out.println("done");

    if (reportListener != null) {
      System.out.println("Writing fine-grained report to "
          + params.getReportOutputFile().getAbsolutePath());
      reportListener.writeReport();

      try {
        // TODO: is it a problem to close the stream now?
        reportOutputStream.close();
      } catch (IOException e) {
        // nothing to do
      }
    }

    System.out.println();

    System.out.println("Accuracy: " + evaluator.getWordAccuracy());
  }
}
