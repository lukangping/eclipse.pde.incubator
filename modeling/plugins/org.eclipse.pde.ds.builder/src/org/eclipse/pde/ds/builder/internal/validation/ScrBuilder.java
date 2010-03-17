package org.eclipse.pde.ds.builder.internal.validation;

import java.util.StringTokenizer;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.ds.builder.internal.Activator;
import org.eclipse.pde.emfforms.builder.IncrementalModelBuilder;
import org.eclipse.pde.emfforms.builder.MarkerHelper;
import org.eclipse.pde.internal.core.ibundle.IBundleModel;
import org.eclipse.pde.internal.core.ibundle.IBundlePluginModelBase;
import org.eclipse.pde.internal.core.natures.PDE;
import org.osgi.service.component.ComponentConstants;

public class ScrBuilder extends IncrementalModelBuilder {
	public static final String ID = "org.eclipse.pde.ds.builder.scrBuilder";

	public ScrBuilder() {
	}

	@SuppressWarnings("restriction")
	@Override
	protected void fullBuild(IProgressMonitor monitor) throws CoreException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		if (PDE.hasPluginNature(getProject())) {
			IPluginModelBase bundle = PluginRegistry.findModel(getProject());
			if (bundle instanceof IBundlePluginModelBase) {
				IBundleModel bundleModel = ((IBundlePluginModelBase) bundle)
						.getBundleModel();
				// XXX for some reason, if we don't call load() by hand, some
				// headers are missing (???)
				bundleModel.load();
				String serviceComponents = bundleModel.getBundle().getHeader(
						ComponentConstants.SERVICE_COMPONENT);
				if (serviceComponents != null) {
					StringTokenizer tok = new StringTokenizer(
							serviceComponents, ","); //$NON-NLS-1$
					// process all definition file
					while (tok.hasMoreElements()) {
						String definitionFile = tok.nextToken().trim();
						int ind = definitionFile.lastIndexOf('/');
						String path = ind != -1 ? definitionFile.substring(0,
								ind) : "/"; //$NON-NLS-1$
						// TODO we need to support patterns (path may be equal
						// to something like "/OSGI-INF/comp-*.xml"...)
						IFile componentFile = getProject().getFile(
								definitionFile);
						URI res = URI.createPlatformResourceURI(componentFile
								.getFullPath().toString(), true);
						Resource modelResource = new AdapterFactoryEditingDomain(
								new ComposedAdapterFactory(
										ComposedAdapterFactory.Descriptor.Registry.INSTANCE),
								new BasicCommandStack()).getResourceSet()
								.getResource(res, true);
						build(modelResource.getContents().get(0),
								componentFile, true, new SubProgressMonitor(
										monitor, 1));
					}
				} // end while
			}
		}
	}

	protected void build(EObject modelObject, IResource resource,
			boolean force, IProgressMonitor monitor) throws CoreException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask("Model Validation", 4);

		monitor.subTask("Validation");

		Diagnostic diagnostic = validate(modelObject);

		monitor.worked(3);

		if (resource instanceof IContainer) {
			MarkerHelper.cleanMarkers((IContainer) resource);
		} else if (resource instanceof IFile) {
			MarkerHelper.cleanMarkers((IFile) resource);
		}

		MarkerHelper.createMarkers(diagnostic, new SubProgressMonitor(monitor,
				1));

		monitor.done();
	}

	private Diagnostic validate(final EObject modelObject) {
		TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE
				.createEditingDomain();
		final AdapterFactory adapterFactory = domain instanceof AdapterFactoryEditingDomain ? ((AdapterFactoryEditingDomain) domain)
				.getAdapterFactory()
				: null;

		try {
			return (Diagnostic) domain
					.runExclusive(new RunnableWithResult.Impl<Diagnostic>() {
						public void run() {

							Diagnostic diagnostic = new Diagnostician() {

								@Override
								public String getObjectLabel(EObject eObject) {
									if (adapterFactory != null
											&& !eObject.eIsProxy()) {
										IItemLabelProvider itemLabelProvider = (IItemLabelProvider) adapterFactory
												.adapt(
														eObject,
														IItemLabelProvider.class);
										if (itemLabelProvider != null) {
											return itemLabelProvider
													.getText(eObject);
										}
									}

									return super.getObjectLabel(eObject);
								}
							}.validate(modelObject);
							setResult(diagnostic);
						}
					});
		} catch (InterruptedException ie) {
			// Log and return the exception in a diagnostic
			Activator.log(ie);
			return new BasicDiagnostic(Diagnostic.ERROR, "ModelChecker", 0, ie
					.getMessage(), new Object[] { modelObject });
		}
	}

	@Override
	protected String getContentType() {
		return "org.eclipse.pde.ds.content-type";
	}
}
