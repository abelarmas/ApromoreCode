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

package org.apromore.service.bpmnminer.impl;

import java.util.*;
import javax.inject.Inject;

import au.edu.qut.context.FakePluginContext;
import au.edu.qut.util.LogOptimizer;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIContext;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.conceptualmodels.ConceptualModel;
import org.processmining.models.graphbased.directed.conceptualmodels.Entity;
import org.processmining.plugins.bpmn.BpmnDefinitions;
import org.processmining.plugins.bpmn.miner.preprocessing.functionaldependencies.Data;
import org.processmining.plugins.bpmn.miner.preprocessing.functionaldependencies.DiscoverERmodel;
import org.processmining.plugins.bpmn.miner.preprocessing.functionaldependencies.DiscoverERmodel.ForeignKeyData;
import org.processmining.plugins.bpmn.miner.preprocessing.functionaldependencies.NoEntityException;
import org.processmining.plugins.bpmn.miner.subprocessminer.BPMNSubProcessMiner;
import org.processmining.plugins.bpmn.miner.subprocessminer.EntityDiscoverer;
import org.processmining.plugins.bpmn.miner.subprocessminer.ui.SelectMinerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.apromore.service.bpmnminer.BPMNMinerService;
import org.apromore.service.ibpstruct.IBPStructService;

/**
 * Created by Raffaele Conforti on 17/04/2015.
 */
@Service
public class BPMNMinerServiceImpl implements BPMNMinerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BPMNMinerServiceImpl.class);

    private final IBPStructService ibpstructService;

    @Inject
    public BPMNMinerServiceImpl(final IBPStructService ibpstructService) {
        this.ibpstructService = ibpstructService;
    }

    @Override
    public String discoverBPMNModel(XLog log, boolean sortLog, boolean structProcess, int miningAlgorithm, int dependencyAlgorithm, double interruptingEventTolerance, double timerEventPercentage,
                                    double timerEventTolerance, double multiInstancePercentage, double multiInstanceTolerance,
                                    double noiseThreshold, List<String> listCandidates, Map<Set<String>, Set<String>> primaryKeySelections) throws Exception {

		//this.LOGGER.info("discoverBPMNModel - started.");

        String xmlProcessModel;

        LogOptimizer logOptimizer = new LogOptimizer();
        XLog optimizedLog = logOptimizer.optimizeLog(log);
        log = optimizedLog;

        EntityDiscoverer entityDiscoverer = new EntityDiscoverer();

        DiscoverERmodel erModel = new DiscoverERmodel();
        ConceptualModel concModel = null;

        //ui choose artifacts from entities
        //------------------------------------------
        List<Entity> groupEntities = new ArrayList<Entity>();
        List<Entity> candidatesEntities = new ArrayList<Entity>();
        List<Entity> selectedEntities = new ArrayList<Entity>();

        if(listCandidates != null && primaryKeySelections != null && listCandidates.size() > 0 && primaryKeySelections.size() > 0) {
            try {
                List<String> allAttributes = erModel.generateAllAttributes(log);
                allAttributes.removeAll(listCandidates);
                HashMap<String, Data> data = erModel.generateData(log, allAttributes);
                Map<Set<String>, String> primaryKeys_entityName = generateEntitiesNames(erModel, primaryKeySelections);
                erModel.setPrimaryKeysEntityName(primaryKeys_entityName);
                concModel = erModel.createConceptualModel(primaryKeySelections, data);

                List<ForeignKeyData> fkeyData = null;
                boolean[] selectedFKeys = null;

                if (dependencyAlgorithm == 1) {
                    fkeyData = erModel.discoverForeignKeys(concModel);
                    selectedFKeys = new boolean[fkeyData.size()];
                    for (int i = 0; i < selectedFKeys.length; i++) {
                        selectedFKeys[i] = true;
                    }
                }
                erModel.updateConceptualModel(primaryKeys_entityName, fkeyData, concModel, selectedFKeys, dependencyAlgorithm);

                groupEntities = entityDiscoverer.discoverGroupEntities(concModel, null, true, true);
                candidatesEntities = entityDiscoverer.discoverCandidatesEntities(concModel, groupEntities);
                selectedEntities = entityDiscoverer.selectEntities(groupEntities, candidatesEntities, true, true);
            } catch (NoEntityException nee) {
                concModel = null;
                groupEntities = new ArrayList<Entity>();
                candidatesEntities = new ArrayList<Entity>();
                selectedEntities = new ArrayList<Entity>();
            }
        }

        if(miningAlgorithm == 0) miningAlgorithm = 1;
        else if(miningAlgorithm == 1) miningAlgorithm = 0;

        SelectMinerResult selectMinerResult = new SelectMinerResult(miningAlgorithm, interruptingEventTolerance, multiInstancePercentage,
                multiInstanceTolerance, timerEventPercentage, timerEventTolerance, noiseThreshold);

        FakePluginContext fakePluginContext = new FakePluginContext();
        BPMNSubProcessMiner bpmnSubProcessMiner = new BPMNSubProcessMiner(fakePluginContext);
        BPMNDiagram diagram = bpmnSubProcessMiner.mineBPMNModel(fakePluginContext, log, sortLog, selectMinerResult, dependencyAlgorithm, concModel,
                groupEntities, candidatesEntities, selectedEntities, true);

        if( structProcess ) diagram = ibpstructService.structureProcess(diagram);

        System.out.println("Output file:");
        UIContext context = new UIContext();
        UIPluginContext uiPluginContext = context.getMainPluginContext();
        BpmnDefinitions.BpmnDefinitionsBuilder definitionsBuilder = new BpmnDefinitions.BpmnDefinitionsBuilder(uiPluginContext, diagram);
        BpmnDefinitions definitions = new BpmnDefinitions("definitions", definitionsBuilder);

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"\n " +
                "xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\"\n " +
                "xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\"\n " +
                "xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\"\n " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n " +
                "targetNamespace=\"http://www.omg.org/bpmn20\"\n " +
                "xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\">");

        sb.append(definitions.exportElements());
        sb.append("</definitions>");
        xmlProcessModel = sb.toString();

        return xmlProcessModel;
    }

    private HashMap<Set<String>, String> generateEntitiesNames(DiscoverERmodel erModel, Map<Set<String>, Set<String>> group) throws NoEntityException {
        HashMap<Set<String>, String> primaryKeys_entityName = new HashMap<Set<String>, String>();
        Scanner console = new Scanner(System.in);
        boolean changeParameters = false;
        for (Map.Entry<Set<String>, Set<String>> setSetEntry : group.entrySet()) {
            String value = "";
            if (changeParameters) {
                while (value.isEmpty()) {
                    value = console.nextLine();
                    if (value.isEmpty()) {
                        value = erModel.keyToString(setSetEntry.getKey());
                    }
                }
            } else {
                value = erModel.keyToString(setSetEntry.getKey());
            }
            primaryKeys_entityName.put(setSetEntry.getKey(), value);
        }
        return primaryKeys_entityName;
    }

}
