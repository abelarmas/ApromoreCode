/*
 * Copyright © 2009-2016 The Apromore Initiative.
 *
 * This file is part of "Apromore".
 *
 * "Apromore" is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * "Apromore" is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package org.apromore.plugin.portal.metrics;

// Java 2 Standard Edition packages

import org.apromore.graph.canonical.Canonical;
import org.apromore.model.ProcessSummaryType;
import org.apromore.model.VersionSummaryType;
import org.apromore.plugin.portal.PortalContext;
import org.apromore.portal.custom.gui.plugin.PluginCustomGui;
import org.apromore.portal.custom.gui.tab.impl.TabRowValue;
import org.apromore.service.ProcessService;
import org.apromore.service.bpmndiagramimporter.BPMNDiagramImporter;
import org.apromore.service.metrics.MetricsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apromore.helper.Version;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zul.*;

import java.io.IOException;
import java.util.*;

// Java 2 Enterprise Edition packages
// Third party packages
// Local packages

/**
 * Metrics service. Created by Adriano Augusto 18/04/2016
 */
@Component("plugin")
public class MetricsPlugin extends PluginCustomGui {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsPlugin.class);


    private PortalContext portalContext;
    private final MetricsService metricsService;
    private final ProcessService processService;
    private final BPMNDiagramImporter importerService;
    private Map<ProcessSummaryType, List<VersionSummaryType>> processVersions;

    /* zk gui variables */
    private Window settings;
    private Radiogroup size;
    private Radiogroup cfc;
    private Radiogroup acd;
    private Radiogroup mcd;
    private Radiogroup cnc;
    private Radiogroup density;
    private Radiogroup structuredness;
    private Radiogroup separability;
    private Radiogroup duplicates;

    private Button okButton;
    private Button cancelButton;

    @Inject
    public MetricsPlugin(final MetricsService    metricsService,
                         final ProcessService processService,
                         final BPMNDiagramImporter importerService) {

        this.metricsService      = metricsService;
        this.processService      = processService;
        this.importerService     = importerService;
    }

    @Override
    public String getLabel(Locale locale) {
        return "Measure";
    }

    @Override
    public void execute(PortalContext context) {
        this.portalContext = context;
        processVersions = portalContext.getSelection().getSelectedProcessModelVersions();

        if( processVersions.size() != 1 ) {
            Messagebox.show("Please, select exactly one process.", "Wrong Process Selection", Messagebox.OK, Messagebox.INFORMATION);
            return;
        }

        portalContext.getMessageHandler().displayInfo("Executing Metrics service...");
        runComputation();
//
//        try {
//            this.settings = (Window) portalContext.getUI().createComponent(getClass().getClassLoader(), "zul/metrics.zul", null, null);
//
//            this.size = (Radiogroup) this.settings.getFellow("size");
//            this.cfc = (Radiogroup) this.settings.getFellow("cfc");
//            this.acd = (Radiogroup) this.settings.getFellow("acd");
//            this.mcd = (Radiogroup) this.settings.getFellow("mcd");
//            this.cnc = (Radiogroup) this.settings.getFellow("cnc");
//            this.density = (Radiogroup) this.settings.getFellow("density");
//            this.structuredness = (Radiogroup) this.settings.getFellow("structuredness");
//            this.separability = (Radiogroup) this.settings.getFellow("separability");
//            this.duplicates = (Radiogroup) this.settings.getFellow("duplicates");
//
//            this.cancelButton = (Button) this.settings.getFellow("CancelButton");
//            this.okButton = (Button) this.settings.getFellow("OKButton");
//
//            this.cancelButton.addEventListener("onClick", new org.zkoss.zk.ui.event.EventListener<Event>() {
//                public void onEvent(Event event) throws Exception {
//                    cancel();
//                }
//            });
//            this.okButton.addEventListener("onClick", new org.zkoss.zk.ui.event.EventListener<Event>() {
//                public void onEvent(Event event) throws Exception {
//                    runComputation();
//                }
//            });
//            this.settings.doModal();
//        } catch (IOException e) {
//            Messagebox.show("Something went wrong (" + e.getMessage() + ")", "Attention", Messagebox.OK, Messagebox.ERROR);
//        }
    }



    protected void cancel() {
        this.settings.detach();
    }

    protected void runComputation() {
        Map<String, String> bpmnMetrics;
        Map<String, String> canonicalMetrics;

//        this.settings.detach();
//
//        boolean size = this.size.getSelectedIndex() == 0 ? true : false;
//        boolean cfc = this.cfc.getSelectedIndex() == 0 ? true : false;
//        boolean acd  = this.acd.getSelectedIndex() == 0 ? true : false;
//        boolean mcd = this.mcd.getSelectedIndex() == 0 ? true : false;
//        boolean cnc = this.cnc.getSelectedIndex() == 0 ? true : false;
//        boolean density  = this.density.getSelectedIndex() == 0 ? true : false;
//        boolean structuredness = this.structuredness.getSelectedIndex() == 0 ? true : false;
//        boolean separability = this.separability.getSelectedIndex() == 0 ? true : false;
//        boolean duplicates  = this.duplicates.getSelectedIndex() == 0 ? true : false;

        boolean size = true;
        boolean cfc = true;
        boolean acd  = true;
        boolean mcd = true;
        boolean cnc = true;
        boolean density  = true;
        boolean structuredness = true;
        boolean separability = true;
        boolean duplicates  = true;

        try {
            for (ProcessSummaryType process : processVersions.keySet()) {
                for (VersionSummaryType vst : processVersions.get(process)) {
                    int procID = process.getId();
                    String procName = process.getName();
                    String branch = vst.getName();
                    Version version = new Version(vst.getVersionNumber());

                    String model = processService.getBPMNRepresentation(procName, procID, branch, version);
                    BPMNDiagram bpmnDiagram = importerService.importBPMNDiagram(model);
                    bpmnMetrics = metricsService.computeMetrics(bpmnDiagram, size, cfc, acd, mcd, cnc, density,
                                                                structuredness, separability, duplicates);

                    Canonical cpfDiagram = processService.getCanonicalFormat(procID, branch, vst.getVersionNumber());
                    canonicalMetrics = metricsService.computeCanonicalMetrics(cpfDiagram);

                    if(bpmnMetrics == null) LOGGER.info("GOT NULL!");
                    else {
                        List<TabRowValue> rows = new ArrayList<>();
                        for (String key : bpmnMetrics.keySet()) {
                            LOGGER.info(key + " : " + bpmnMetrics.get(key));
                            rows.add(createTabRowValue(key, bpmnMetrics.get(key), canonicalMetrics.get(key)));
                        }

                        List<Listheader> listheaders = new ArrayList<>();
                        listheaders.add(new Listheader("Metric"));
                        listheaders.add(new Listheader("value on BPMN"));
                        listheaders.add(new Listheader("value on CPF"));

                        addTab(procName + ": metrics", "", rows, listheaders, null, portalContext);
                    }

                    LOGGER.info("Metrics - done!");
                }
            }
        } catch(Exception e) {
            StringBuilder sb = new StringBuilder();
            e.printStackTrace();
            for(StackTraceElement element : e.getStackTrace()) {
                sb.append(element.toString() + "\n");
            }
            String message = "Search failed (" + sb.toString() + ")";
            Messagebox.show(message, "Attention", Messagebox.OK, Messagebox.ERROR);
        }
    }

}
