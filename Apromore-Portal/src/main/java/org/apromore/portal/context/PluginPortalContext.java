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

package org.apromore.portal.context;

import org.apache.commons.io.IOUtils;
import org.apromore.model.FolderType;
import org.apromore.model.ProcessSummaryType;
import org.apromore.model.UserType;
import org.apromore.model.VersionSummaryType;
import org.apromore.plugin.portal.*;
import org.apromore.portal.common.UserSessionManager;
import org.apromore.portal.dialogController.MainController;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the PortalContext that is use by portal plug-ins to communicate with the portal.
 */
public class PluginPortalContext implements PortalContext {

    /**
     * Implementation of the PortalUI communication interface
     */
    private final static class PortalUIImpl implements PortalUI {

        @Override
        public Component createComponent(ClassLoader bundleClassLoader, String uri, Component parent, Map<?, ?> arguments) throws IOException {
            InputStream is = bundleClassLoader.getResourceAsStream(uri);
            return Executions.createComponentsDirectly(IOUtils.toString(is, "UTF-8"), "zul", parent, arguments);
        }

    }

    /**
     * Implementation of the MessageHandler communication interface
     */
    private class PortalMessageHandler implements MessageHandler {

        @Override
        public void displayInfo(String message) {
            displayMessage(Level.INFO, message);
        }

        @Override
        public void displayError(String message, Exception exception) {
            displayMessage(Level.ERROR, message + " caused by " + exception.toString());
        }

        @Override
        public void displayMessage(Level level, String message) {
            //TODO implement different colors for different message levels
            mainController.displayMessage(message);
        }

    }

    private final MainController mainController;
    private final MessageHandler messageHandler;
    private final PortalUI portalUI;

    /**
     * Create a new PluginPortalContext
     *
     * @param mainController
     */
    public PluginPortalContext(MainController mainController) {
        this.mainController = mainController;
        this.messageHandler = new PortalMessageHandler();
        this.portalUI = new PortalUIImpl();
    }

    @Override
    public MessageHandler getMessageHandler() {
        return new PortalMessageHandler();
    }

    @Override
    public PortalSelection getSelection() {
        return new PortalSelection() {
            @Override
            public Map<ProcessSummaryType, List<VersionSummaryType>> getSelectedProcessModelVersions() {
                return mainController.getSelectedProcessVersions();
            }

            @Override
            public Set<ProcessSummaryType> getSelectedProcessModels() {
                return mainController.getSelectedProcesses();
            }

        };
    }

    @Override
    public PortalUI getUI() {
        return new PortalUIImpl();
    }


    // Scratch area for methods required during porting

    @Override
    public void displayNewProcess(ProcessSummaryType process) {
        mainController.displayNewProcess(process);
    }

    @Override
    public FolderType getCurrentFolder() {
        return UserSessionManager.getCurrentFolder();
    }

    @Override
    public UserType getCurrentUser() {
        return UserSessionManager.getCurrentUser();
    }

    @Override
    public MainControllerInterface getMainController() {
        return mainController;
    }

    @Override
    public void refreshContent() {
        mainController.refresh();
    }

}
