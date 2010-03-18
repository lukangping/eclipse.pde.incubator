package org.eclipse.pde.ds.builder.internal.validation;

import java.util.StringTokenizer;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.emfforms.builder.IncrementalModelBuilder;
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
								componentFile, new SubProgressMonitor(monitor,
										1));
					}
				} // end while
			}
		}
	}
}
