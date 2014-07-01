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


package opennlp.tools.cmdline;

import opennlp.tools.cmdline.parser.ParserTool;

public final class CLI2 {
  
  public static final String CMD = "opennlp";
  
  /**
   * @return a set which contains all tool names
   */
 
  public static void main(String[] args) {
        
	  ParserTool tool = new ParserTool();
    String[] toolArguments = {"/Users/josepvalls/soft/apache-opennlp-1.5.3/bin/models/en-parser-chunking.bin"};
    try {
          tool.run(toolArguments,"Once upon a time a move.");
    }
    catch (TerminateToolException e) {
      
      if (e.getMessage() != null) {
        System.err.println(e.getMessage());
      }

      if (e.getCause() != null) {
        System.err.println(e.getCause().getMessage());
        e.getCause().printStackTrace(System.err);
      }
      
      System.exit(e.getCode());
    }
  }
}
