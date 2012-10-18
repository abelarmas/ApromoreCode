package org.apromore.canoniser.bpmn;

// Java 2 Standard packages
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

// Local packages
import org.apromore.anf.AnnotationsType;
import org.apromore.canoniser.DefaultAbstractCanoniser;
import org.apromore.canoniser.bpmn.anf.AnfAnnotationsType;
import org.apromore.canoniser.bpmn.cpf.CpfCanonicalProcessType;
import org.apromore.canoniser.exception.CanoniserException;
import org.apromore.canoniser.result.CanoniserMetadataResult;
import org.apromore.cpf.CanonicalProcessType;
import org.apromore.plugin.PluginRequest;
import org.apromore.plugin.PluginResult;
import org.apromore.plugin.impl.PluginResultImpl;
import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.springframework.stereotype.Component;

/**
 * Canoniser for Business Process Model and Notation (BPMN) 2.0.
 *
 * @see <a href="http://www.bpmn.org">Object Management Group BPMN site</a>
 * @author <a href="mailto:simon.raboczi@uqconnect.edu.au">Simon Raboczi</a>
 * @since 0.4
 */
@Component("bpmnCanoniser")
public class BPMN20Canoniser extends DefaultAbstractCanoniser {

    /** CPF schema version. */
    public static final String CPF_VERSION = "1.0";

    // Methods implementing Canoniser interface

    /** {@inheritDoc} */
    @Override
    public PluginResult canonise(final InputStream                bpmnInput,
                                 final List<AnnotationsType>      annotationFormat,
                                 final List<CanonicalProcessType> canonicalFormat,
                                 final PluginRequest request) throws CanoniserException {

        try {
            BpmnDefinitions definitions = BpmnDefinitions.newInstance(bpmnInput, false);
            CanoniserResult result = canonise(definitions);
            for (int i = 0; i < result.size(); i++) {
                annotationFormat.add(result.getAnf(i));
                canonicalFormat.add(result.getCpf(i));
            }
            return new PluginResultImpl();
        } catch (Exception e) {
            throw new CanoniserException("Could not canonise to BPMN stream", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Each empty document generated by this method will have a UUID as its target namespace, such that
     * BPMN QName identifiers will be unique even for short IDs within the document.
     * In other words, IDs need not be universally unique, only unique within the document.
     */
    @Override
    public PluginResult createInitialNativeFormat(final OutputStream bpmnOutput,
                                                  final String processName,
                                                  final String processVersion,
                                                  final String processAuthor,
                                                  final Date processCreated,
                                                  final PluginRequest request) {

        PluginResultImpl result = newPluginResult();
        try {
            // Construct an empty BPMN model
            BpmnDefinitions definitions = new BpmnDefinitions();
            definitions.setId("dummy-id");
            definitions.setName(processName);
            definitions.setExporter(getClass().getCanonicalName());
            definitions.setExporterVersion("1.0");
            definitions.setTargetNamespace("http://apromore.org/" + UUID.randomUUID() + "#");

            // Serialize out the BPMN model
            definitions.marshal(bpmnOutput, false);

        } catch (Exception e) {
            result.addPluginMessage("Failed to create empty BPMN model: {0}", e.getMessage());
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public PluginResult deCanonise(final CanonicalProcessType canonicalFormat,
                           final AnnotationsType      annotationFormat,
                           final OutputStream         bpmnOutput,
                           final PluginRequest request) throws CanoniserException {

        try {
            new BpmnDefinitions(canonicalFormat, annotationFormat).marshal(bpmnOutput, false);

            return new PluginResultImpl();
        } catch (Exception e) {
            throw new CanoniserException("Could not decanonise from BPMN stream", e);
        }
    }

    /**
     * {@inheritDoc}
     * @return a result expressing just the name of the BPMN process, or <code>null</code> if any exception occurs internally
     */
    @Override
    public CanoniserMetadataResult readMetaData(final InputStream bpmnInput, final PluginRequest request) {
        try {
            BpmnDefinitions definitions = BpmnDefinitions.newInstance(bpmnInput, false);

            // Fill in the metadata
            CanoniserMetadataResult result = new CanoniserMetadataResult();
            result.setProcessName(definitions.getName());

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    // Implementation of canonisation

    /**
     * Convert this BPMN document into an equivalent collection of CPF and ANF documents.
     *
     * @param definitions  the BPMN document to translate
     * @throws CanoniserException  if the translation can't be performed
     * @return a result containing CPF and ANF documents equivalent to this BPMN
     */
    static CanoniserResult canonise(final BpmnDefinitions definitions) throws CanoniserException {

        // Generate identifiers for @uri scoped across all generated CPF and ANF documents
        final IdFactory linkUriFactory = new IdFactory();

        // This instance will be populated and returned at the end of this method
        final CanoniserResult result = new CanoniserResult();

        // Create the CPF
        CanonicalProcessType cpf = new CpfCanonicalProcessType(definitions);
        cpf.setUri(linkUriFactory.newId(null));

        // Create the ANF
        for (BPMNDiagram diagram : definitions.getBPMNDiagram()) {
            final AnnotationsType anf = new AnfAnnotationsType(diagram);
            anf.setUri(cpf.getUri());
            result.put(cpf, anf);
        }

        // Dummy return value
        return result;
    }

    /**
     * This method centralizes the policy of filling in absent names with a zero-length
     * string in cases where CPF requires a name which is optional in BPMN.
     *
     * @param name  a name which might be absent in the source language
     * @return <code>name</code> if present, otherwise <code>""</code> (the zero-length string).
     */
    public static String requiredName(final String name) {
        return (name == null ? "" : name);
    }
}
