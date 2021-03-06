/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.jstojava.cml.vjetv.core.impl;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.ebayopensource.dsf.jsgen.shared.ids.VjoSyntaxProbIds;
import org.ebayopensource.dsf.jsgen.shared.validation.vjo.VjoSemanticProblem;
import org.ebayopensource.dsf.jsgen.shared.validation.vjo.VjoValidationDriver;
import org.ebayopensource.dsf.jsgen.shared.validation.vjo.VjoValidationResult;
import org.ebayopensource.dsf.jst.IJstNode;
import org.ebayopensource.dsf.jst.IJstType;
import org.ebayopensource.dsf.jst.IScriptProblem;
import org.ebayopensource.dsf.jst.IScriptUnit;
import org.ebayopensource.dsf.jst.JstProblemId;
import org.ebayopensource.dsf.jst.declaration.JstBlock;
import org.ebayopensource.dsf.jst.declaration.JstCache;
import org.ebayopensource.dsf.jst.ts.JstTypeSpaceMgr;
import org.ebayopensource.dsf.jstojava.cml.vjetv.core.IHeadLessLauncher;
import org.ebayopensource.dsf.jstojava.cml.vjetv.model.IHeadLessLauncherResult;
import org.ebayopensource.dsf.jstojava.cml.vjetv.model.IHeadlessLauncherConfigure;
import org.ebayopensource.dsf.jstojava.cml.vjetv.model.IHeadlessParserConfigure;
import org.ebayopensource.dsf.jstojava.cml.vjetv.model.impl.EVLauncherResult;
import org.ebayopensource.dsf.jstojava.cml.vjetv.model.impl.VjetvHeadlessConfigure;
import org.ebayopensource.dsf.jstojava.cml.vjetv.reporter.IHeadLessReporter;
import org.ebayopensource.dsf.jstojava.cml.vjetv.reporter.ReporterFactory;
import org.ebayopensource.dsf.jstojava.cml.vjetv.reporter.impl.BaseReporter;
import org.ebayopensource.dsf.jstojava.cml.vjetv.reporter.impl.VjetVReporter;
import org.ebayopensource.dsf.jstojava.cml.vjetv.util.FileOperator;
import org.ebayopensource.dsf.jstojava.controller.JstParseController;
import org.ebayopensource.dsf.jstojava.loader.OnDemandAllTypeLoader;
import org.ebayopensource.dsf.jstojava.parser.VjoParser;
import org.ebayopensource.dsf.ts.event.group.AddGroupEvent;
import org.ebayopensource.dsf.ts.type.TypeName;
import org.ebayopensource.vjo.lib.LibManager;
import org.ebayopensource.vjo.lib.TsLibLoader;

/**
 * Class/Interface description
 * 
 * @author <a href="mailto:liama@ebay.com">liama</a>
 * @since JDK 1.5
 */
public class EVHeadlessLauncher implements IHeadLessLauncher {

    private static final String ONDEMAND = "ONDEMAND";

    HashMap<File, Object> resultDataMap = new LinkedHashMap<File, Object>();

    // Step1: init parser and controller
    VjoParser p = new VjoParser();

    JstParseController c = new JstParseController(p);

    /**
     * 
     * Do semantic validiation
     * 
     * @param unit
     *            {@link IScriptUnit}
     * @param ts
     *            {@link JstTypeSpaceMgr}
     * @return {@link VjoSemanticProblem}
     * @throws AssertionError
     */
    private List<VjoSemanticProblem> doSemanticValidate(final IScriptUnit unit,
            JstTypeSpaceMgr ts) throws AssertionError {
        IJstType jstType = null;
        VjoValidationDriver driver = new VjoValidationDriver();
        if (unit == null)
            throw new AssertionError("Unable to find specified test file.");
        jstType = ts.getTypeSpace().getType(
                new TypeName(ONDEMAND, unit.getType().getName()));
        if (jstType == null)
            throw new AssertionError("Unable to parse specified test file.");
        if (unit.getProblems().size() > 0) {
            throw new AssertionError("Test js file has syntax errors.");
        }
        driver.setTypeSpaceMgr(ts);

        List<IScriptUnit> types = new ArrayList<IScriptUnit>();
        final IJstType resolvedType = jstType;

        types.add(new IScriptUnit() {

            @Override
            public List<JstBlock> getJstBlockList() {
                return unit.getJstBlockList();
            }

            @Override
            public IJstNode getNode(int startOffset) {
                return unit.getNode(startOffset);
            }

            @Override
            public List<IScriptProblem> getProblems() {
                return unit.getProblems();
            }

            @Override
            public JstBlock getSyntaxRoot() {
                return unit.getSyntaxRoot();
            }

            @Override
            public IJstType getType() {
                return resolvedType;
            }

        });
        VjoValidationResult result = driver.validateComplete(types, ONDEMAND);
        return result.getAllProblems();
    }

    /**
     * Get result Object
     * 
     * @param result
     *            {@link EVLauncherResult}
     * @param conf
     *            {@link IHeadlessLauncherConfigure}
     * @param startTime
     *            {@link Date}
     * @param reporter
     *            {@link VjetVReporter}
     * @return {@link IHeadLessLauncherResult}
     */
    private IHeadLessLauncherResult generateResult(EVLauncherResult result,
            IHeadlessLauncherConfigure conf, Date startTime,
            IHeadLessReporter reporter) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String startTimeString = df.format(startTime);
        Date endTime = new Date();
        String endTimeString = df.format(endTime);
        // if (!conf.isBuild() && conf.isVerbose()) {
        result.setResultMap(resultDataMap);
        result.setStartTime(startTimeString);
        result.setEndTime(endTimeString);
        reporter.generateReport(result, conf);
        // }
        setResultInformation(conf, startTime, startTimeString, endTime,
                endTimeString, result);
        reporter.printSummaryInformation(result);

        // DEL Temp folder
        FileOperator.delFolder(FileOperator.TEMPFOLDER);
        return result;
    }

    /**
     * Return syntax problems
     * 
     * @param actualProblemList
     *            {@link ArrayList}
     * @param unit
     *            {@link IScriptUnit}
     */
    private List<VjoSemanticProblem> handleSyntaxProblems(
            List<VjoSemanticProblem> actualProblemList, IScriptUnit unit) {
        List<IScriptProblem> lists = unit.getProblems();
        JstProblemId problemID;
        for (IScriptProblem scriptProblem : lists) {
            if (scriptProblem.getID() != null) {
                problemID = scriptProblem.getID();
            } else {
                problemID = VjoSyntaxProbIds.IncorrectVjoSyntax;
            }
            actualProblemList.add(new VjoSemanticProblem(null, problemID,
                    scriptProblem.getMessage(), scriptProblem
                            .getOriginatingFileName(), scriptProblem
                            .getSourceStart(), scriptProblem.getSourceEnd(),
                    scriptProblem.getSourceLineNumber(), 0, scriptProblem
                            .type()));
        }
        return actualProblemList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ebayopensource.dsf.jstojava.cml.vjetv.core.IHeadLessLauncher#launch(org.ebayopensource.dsf.jstojava.cml.vjetv.model.IHeadlessLauncherConfigure,
     *      org.ebayopensource.dsf.jstojava.cml.vjetv.reporter.IHeadLessReporter)
     */
    @Override
    public IHeadLessLauncherResult launch(IHeadlessLauncherConfigure conf,
            IHeadLessReporter reporter) {
        if (conf instanceof VjetvHeadlessConfigure) {
            // Step1: clear result collection
            resultDataMap.clear();

            Date startTime = new Date();
            EVLauncherResult result = new EVLauncherResult();

            // Step2: validate all JS files
            launchValidate(conf, reporter, result);

            // Step3: create and return result object
            return generateResult(result, conf, startTime, reporter);
        }
        return null;
    }

    /**
     * Launch validate function
     * 
     * @param conf
     *            {@link LinkedHashSet}
     * @param reporter
     *            {@link IHeadLessReporter}
     * @param result
     */
    private void launchValidate(IHeadlessLauncherConfigure conf,
            IHeadLessReporter reporter, EVLauncherResult result) {
        File validateFile = null;
        LinkedHashSet<File> jsFiles = conf.getValidatedJSFiles();
        List<VjoSemanticProblem> actualProblemList = null;
        BaseReporter baseReporter = (BaseReporter) reporter;
        int i = 0;
        final int size = jsFiles.size();
        
        baseReporter.printCurrentStates("<Start to Validate... ==============================" +
                "============================================" +
                "============================================>\n");
        for (Iterator<File> iterator = jsFiles.iterator(); iterator.hasNext();) {
            ++i;
            validateFile = iterator.next();
            
            try {
                actualProblemList = validateFile(validateFile);
                if (null != actualProblemList && actualProblemList.size() > 0) {
                	
                	
                    String printProblems = baseReporter.printProblems(
                            actualProblemList, result, conf.getReportLevel(),
                            true, validateFile);
                    if(printProblems!=null){
                    	baseReporter.printCurrentStates(printProblems);
                    }
                    resultDataMap.put(validateFile, actualProblemList);
                }
            } catch (Throwable e) {
                reporter.printCurrentStates("Error Message :  "
                        + e.getMessage() + "\n");
                reporter
                        .printCurrentStates("Please check verified JS files writtern by VJO syntax. \n");
                resultDataMap.put(validateFile, e.getMessage());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ebayopensource.dsf.jstojava.cml.vjetv.core.IHeadLessLaucher#launch(org.ebayopensource.dsf.jstojava.cml.vjetv.model.IVjetvHeadlessConfigure)
     */
    public IHeadLessLauncherResult launchValidation(
            VjetvHeadlessConfigure conf, VjetVReporter reporter) {
        return launch(conf, reporter);
    }

    /**
     * Set result information
     * 
     * @param conf.getReportPath()
     *            {@link String}
     * @param startTime
     *            {@link Date}
     * @param startTimeString
     *            String
     * @param endTime
     *            {@link Date}
     * @param endTimeString
     *            {@link String}
     * @param result
     *            {@link EVLauncherResult}
     */
    private void setResultInformation(IHeadlessLauncherConfigure conf,
            Date startTime, String startTimeString, Date endTime,
            String endTimeString, EVLauncherResult result) {
        long l = endTime.getTime() - startTime.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        if (conf.getReportPath() != null) {
            sb.append("Result file at : " + conf.getReportPath() + "\n");
        }
        sb.append("Total validated JS files number: "
                + conf.getValidatedJSFiles().size() + "\n");
        sb.append("Total errors number: " + result.getErrorNumber() + " \n" );
        if(!conf.getReportLevel().equals(IHeadlessParserConfigure.ERROR)){
            sb.append("Total warning number: " + result.getWarningNumber() + "\n");
        }
        sb.append("Start Time : " + startTimeString + "\n");
        sb.append("End   Time : " + endTimeString + "\n");
        sb.append("Total use time : " + hour + ":" + min + ":" + s + "\n");
        result.setResultInformation(sb.toString());
    }

    /**
     * VJET validate JS files involving syntax and semantic problems
     * 
     * @param validateFile
     *            {@link File}
     * @return problems {@link ArrayList}
     */
    public List<VjoSemanticProblem> validateFile(File validateFile) {
        List<VjoSemanticProblem> actualProblemList = new ArrayList<VjoSemanticProblem>();

        // Step1: init parser and controller
        // finish line63.64

        // Step2: parse specify JS file
        IScriptUnit unit = c.parse(ONDEMAND, validateFile.getAbsolutePath(),
                VjoParser.getContent(validateFile));

        // Step3: load depend jsttypes from source path.
        JstTypeSpaceMgr mgr = new JstTypeSpaceMgr(c, new OnDemandAllTypeLoader(
                ONDEMAND, unit.getType()));

        // Step4: initilize mgr
        mgr.initialize();
        TsLibLoader.loadDefaultLibs(mgr);
        try {
            JstCache.getInstance().addLib(
                    LibManager.getInstance().getVjoJavaLib());
            mgr.loadLibrary(LibManager.getInstance().getVjoJavaLib(),
                    LibManager.VJO_JAVA_LIB_NAME);
        } catch (Exception e) {
        }

        // Step5: Resolve JS file and do syntax validation.
        unit = c.parseAndResolve(ONDEMAND, validateFile.getAbsolutePath(),
                VjoParser.getContent(validateFile));
        mgr.processEvent(new AddGroupEvent(ONDEMAND, null,
                Collections.EMPTY_LIST, Collections.EMPTY_LIST));

        // Step6: Handle syntax problems if syntax problem exist.
        if (unit.getProblems().size() > 0) {
            return handleSyntaxProblems(actualProblemList, unit);
        }

        // Step7: Do semantic validate
        actualProblemList = doSemanticValidate(unit, mgr);
        return actualProblemList;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ebayopensource.dsf.jstojava.cml.vjetv.core.IHeadLessLauncher#launch(org.ebayopensource.dsf.jstojava.cml.vjetv.model.IHeadlessLauncherConfigure)
     */
    @Override
    public IHeadLessLauncherResult launch(IHeadlessLauncherConfigure conf) {
        return launch(conf, ReporterFactory.getReporter(conf.isVerbose()));
    }
}
